package com.stackroute.swisit.documentparser.service;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.swisit.documentparser.domain.ContentSchema;


/**
 * Created by user on 30/6/17.
 */
@Service
public class IntensityAlgoServiceImpl implements IntensityAlgoService {
	
	float count;

	@Autowired
	ObjectMapperService objectMapperService;
	
	ArrayList<ContentSchema> contentSchemas = new ArrayList<>();
	
	public ArrayList<ContentSchema> calculateIntensity(HashMap<String,HashMap<String,Integer>> parsedDocumentMap){
		
		List<LinkedHashMap<String, String>> list = objectMapperService.objectMapping("./src/main/resources/common/termintensity.json");
		HashMap<String,String> map = new HashMap<>();

		for(LinkedHashMap<String, String> linked : list){
		   // Entry<String,String> keyset = linked.entrySet<String,String>();
            Iterator<Map.Entry<String, String>> entries = linked.entrySet().iterator();
            Map.Entry<String,String> entry =entries.next();
            map.put(entry.getKey(),entry.getValue());
        }
		for(Entry<String, HashMap<String, Integer>> parsedDocumentMapRef : parsedDocumentMap.entrySet()){
			String wordKey = parsedDocumentMapRef.getKey();
			for(Entry<String, Integer> wordKeyValueRef : parsedDocumentMapRef.getValue().entrySet()){
				String tagKey = wordKeyValueRef.getKey();
				Integer tagFrequency = wordKeyValueRef.getValue();
				float intensity = Float.parseFloat(map.get(tagKey));
				count+=tagFrequency*intensity;
			}
			ContentSchema contentSchema= new ContentSchema(wordKey,count);
			contentSchemas.add(contentSchema);
		}
		return contentSchemas;
	}
}

