package com.stackroute.swisit.documentparser.service;

/*------Importing Libraries-----*/
import com.stackroute.swisit.documentparser.domain.CrawlerResult;
import com.stackroute.swisit.documentparser.domain.DocumentParserResult;
import com.stackroute.swisit.documentparser.domain.ContentSchema;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 30/6/17.
 */
@Service
public class MasterParserServiceImpl implements MasterParserService {

	@Autowired
	private KeywordScannerService keywordScannerService;

    /*@Autowired
    public void setKeywordScannerService(KeywordScannerService keywordScannerService){
        this.keywordScannerService = keywordScannerService;
    }*/

	@Autowired
    private IntensityAlgoService intensityAlgoService;
    
    /*@Autowired
    public void setIntensityAlgoService(IntensityAlgoService intensityAlgoService){
    	this.intensityAlgoService = intensityAlgoService;
    }*/
    
	@Autowired
    private ConceptNetService conceptNetService;
    
    /*@Autowired
    public void setConceptNetService(ConceptNetService conceptNetService){
    	this.conceptNetService = conceptNetService;
    }*/

    @Autowired
    private WordCheckerService wordCheckerService;
    
    /*@Autowired
    public void setWordCheckerService(WordCheckerService wordCheckerService){
    	this.wordCheckerService=wordCheckerService;
    }*/
    
    public Iterable<DocumentParserResult> parseDocument(ArrayList<CrawlerResult> crawlerResults){
    	
        Document document=null;
        ArrayList<DocumentParserResult> documentParserResults = new ArrayList<DocumentParserResult>();
        for(CrawlerResult crawlerResult : crawlerResults) {
            document = Jsoup.parse(crawlerResult.getDocument());
	        HashMap<String, String> keywordScannerResult = keywordScannerService.scanDocument(document);
	        HashMap<String, List<String>> wordCheckerResult = wordCheckerService.getWordCheckerByWord(keywordScannerResult);
	        HashMap<String,HashMap<String,Integer>> conceptNetResult = conceptNetService.createDocumentModel(wordCheckerResult);
	        ArrayList<ContentSchema> contentSchema = intensityAlgoService.calculateIntensity(conceptNetResult);
	        DocumentParserResult documentParserResult = new DocumentParserResult();
	        documentParserResult.setQuery(crawlerResult.getQuery());
	        documentParserResult.setConcept(crawlerResult.getConcept());
	        documentParserResult.setLink(crawlerResult.getLink());
	        documentParserResult.setTerms(contentSchema);
	        documentParserResult.setTitle(crawlerResult.getTitle());
	        documentParserResult.setSnippet(crawlerResult.getSnippet());
	        documentParserResult.setLastindexedof(crawlerResult.getLastindexedof());
	        documentParserResults.add(documentParserResult);
        }
        return documentParserResults;
    }
}
