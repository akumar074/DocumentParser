package com.stackroute.swisit.documentparser.service;
import java.util.ArrayList;

/*-------- Importing Libraries -------*/
import com.stackroute.swisit.documentparser.domain.CrawlerResult;
import com.stackroute.swisit.documentparser.domain.DocumentParserResult;

/**
 * Created by user on 30/6/17.
 */
public interface MasterParserService {

    public Iterable<DocumentParserResult> parseDocument(ArrayList<CrawlerResult> crawlerResults);
}
