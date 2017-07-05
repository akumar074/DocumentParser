package com.stackroute.swisit.documentparser.subscriber;

/*------------ Importing Libraries-----------*/
import java.util.List;

import com.stackroute.swisit.documentparser.domain.CrawlerResult;

/*--- Subscriber Interface that declares method to receive the message via a message service ---*/
public interface Subscriber {
	
	/* Method declaration to receive message via messaging service
	 * arguments- topic name
	 * returns- list of Crawler result
	 * */
	public List<CrawlerResult> receiveMessage(String topic);
}
