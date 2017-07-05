package com.stackroute.swisit.documentparser;
/*--------- Importing Libraries --------*/
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.stackroute.swisit.documentparser.domain.CrawlerResult;
import com.stackroute.swisit.documentparser.subscriber.KafkaSubscriberImpl;
import com.stackroute.swisit.documentparser.threadconsumer.KafkaConsumer;

@SpringBootApplication
public class DocumentparserApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext=SpringApplication.run(DocumentparserApplication.class, args);
		//KafkaSubscriberImpl subscriberImpl = applicationContext.getBean(KafkaSubscriberImpl.class);
		KafkaConsumer kakfaConsumer = applicationContext.getBean(KafkaConsumer.class);
		kakfaConsumer.consumeMessage("tonewparser");
		//List<CrawlerResult> list =subscriberImpl.receiveMessage("tonewparser");
		//CrawlerResult crawlerResult[] = new CrawlerResult[list.size()];
		//list.toArray(crawlerResult);
		//System.out.println("hi this is "+crawlerResult);
		/*
		Document doc;
		for(CrawlerResult cr : crawlerResult){
		doc = Jsoup.parse(cr.getDocument());
		System.out.println(cr.getLink());
		System.out.println(cr.getQuery());
		System.out.println(cr.getSnippet());
		System.out.println(cr.getLastindexedof());
		}
		*/

	}
}
