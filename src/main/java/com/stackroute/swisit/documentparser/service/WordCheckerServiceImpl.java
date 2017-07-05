package com.stackroute.swisit.documentparser.service;

import com.uttesh.exude.ExudeData;

import com.uttesh.exude.exception.InvalidDataException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.*;
import java.util.Map;


/**
 * Created by user on 30/6/17.
 */
@Service
public class WordCheckerServiceImpl implements WordCheckerService{
	public HashMap<String,List<String>> getWordCheckerByWord(HashMap<String,String> input){
		
		/*String inputData = "This handy tool helps you create dummy text for all your layout needs. 
		We are gradually adding new functionality and we welcome your suggestions and feedback. 
		Please feel free to send us any additional dummy texts.";*/
		
		HashMap<String,List<String>> tockenizedWords = new HashMap<>();
		
		HashMap<String,String> map = new HashMap<>();
		
		Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
		while(entries.hasNext()) {
			Map.Entry<String, String> entry =  entries.next();
			String key = entry.getKey();
			String inputData = entry.getValue();
			String filteredWords = "";
			String swearWords = "";
			try {
				filteredWords = ExudeData.getInstance().filterStoppings(inputData);
				swearWords = ExudeData.getInstance().getSwearWords(inputData);
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("output : " + filteredWords);
			System.out.println("output : " + swearWords);
			String filteredSpecialChar = filteredWords.replaceAll("[$_&+,:;=?@#|'<>.-^*()%!]", " ");
			List<String> result = new ArrayList<>();
			for (String s1 : filteredSpecialChar.split(" ")) {
				result.add(s1);
				System.out.println(result);
			}
			tockenizedWords.put(key,result);
		}
		return tockenizedWords;
	}
}
