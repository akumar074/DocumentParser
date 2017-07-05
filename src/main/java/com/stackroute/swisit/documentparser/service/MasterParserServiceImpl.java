package com.stackroute.swisit.documentparser.service;

/*------Importing Libraries-----*/
import ch.qos.logback.core.CoreConstants;
import com.stackroute.swisit.documentparser.domain.CrawlerResult;
import com.stackroute.swisit.documentparser.domain.DocumentParserResult;
import com.stackroute.swisit.documentparser.publisher.Publisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.documentparser.domain.ContentSchema;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by user on 30/6/17.
 */
@Service
public class MasterParserServiceImpl implements MasterParserService {

	@Autowired
	KeywordScannerService keywordScannerService;
	
	@Autowired
	Publisher publisher;
    /*@Autowired
    public void setKeywordScannerService(KeywordScannerService keywordScannerService){
        this.keywordScannerService = keywordScannerService;
    }*/

	@Autowired
    IntensityAlgoService intensityAlgoService;
    
    /*@Autowired
    public void setIntensityAlgoService(IntensityAlgoService intensityAlgoService){
    	this.intensityAlgoService = intensityAlgoService;
    }*/
    
	@Autowired
    ConceptNetService conceptNetService;
    
    /*@Autowired
    public void setConceptNetService(ConceptNetService conceptNetService){
    	this.conceptNetService = conceptNetService;
    }*/

    @Autowired
    WordCheckerService wordCheckerService;
    
    /*@Autowired
    public void setWordCheckerService(WordCheckerService wordCheckerService){
    	this.wordCheckerService=wordCheckerService;
    }*/
    @Autowired
    ObjectMapperService objectMapperService;
    
    public Iterable<DocumentParserResult> parseDocument(/*CrawlerResult crawlerResults*/) throws JsonProcessingException , ParseException{

    	List<LinkedHashMap<String,String>> cR = objectMapperService.objectMapping("./src/main/resources/common/sample.json");
    	ArrayList<CrawlerResult> crawlerResults = new ArrayList<>();
    	for(LinkedHashMap<String,String> linkedMap : cR){
    		CrawlerResult crawlerResult = new CrawlerResult();
    		crawlerResult.setConcept(linkedMap.get("concept"));
    		crawlerResult.setLink(linkedMap.get("link"));
    		crawlerResult.setDocument(linkedMap.get("document"));
    		crawlerResult.setQuery(linkedMap.get("query"));
    		crawlerResult.setSnippet(linkedMap.get("snippet"));
    		crawlerResult.setTitle(linkedMap.get("title"));
    		crawlerResult.setLastindexedof(new SimpleDateFormat("dd/MM/yyyy").parse("05/07/2017"));
    		crawlerResults.add(crawlerResult);
    	}
        Document document=null;
        ArrayList<DocumentParserResult> documentParserResults = new ArrayList<DocumentParserResult>();
        for(CrawlerResult crawlerResult : crawlerResults) {
        System.out.println(crawlerResult.getLink());
        document = Jsoup.parse(crawlerResult.getDocument());
	        HashMap<String, String> keywordScannerResult = keywordScannerService.scanDocument(document);
	        
	        HashMap<String, List<String>> wordCheckerResult = wordCheckerService.getWordCheckerByWord(keywordScannerResult);
	        HashMap<String,HashMap<String,Integer>> conceptNetResult = conceptNetService.createDocumentModel(wordCheckerResult);
	        Iterator<HashMap.Entry<String,HashMap<String,Integer>>> ita = conceptNetResult.entrySet().iterator() ;
	        while(ita.hasNext()) {
				HashMap.Entry<String, HashMap<String, Integer>> parentPair = ita.next();
				System.out.println("parentPair.getKey() :   " + parentPair.getKey() + " parentPair.getValue()  :  " + parentPair.getValue());
				Iterator<HashMap.Entry<String, Integer>> child = (parentPair.getValue()).entrySet().iterator();
				while (child.hasNext()) {
					HashMap.Entry childPair = child.next();
					System.out.println("childPair.getKey() :   " + childPair.getKey() + " childPair.getValue()  :  " + childPair.getValue());

				}
			}
	        ArrayList<ContentSchema> contentSchema = intensityAlgoService.calculateIntensity(conceptNetResult);
	        DocumentParserResult documentParserResult = new DocumentParserResult();
	        documentParserResult.setQuery(crawlerResult.getQuery());
	        documentParserResult.setConcept(crawlerResult.getConcept());
	        documentParserResult.setLink(crawlerResult.getLink());
	        documentParserResult.setTerms(contentSchema);
	        documentParserResult.setTitle(crawlerResult.getTitle());
	        documentParserResult.setSnippet(crawlerResult.getSnippet());
	        documentParserResult.setLastindexedof(crawlerResult.getLastindexedof());
	        //publisher.publishMessage("tointent", documentParserResult);
	        documentParserResults.add(documentParserResult);
        }
        return documentParserResults;
    }
}
