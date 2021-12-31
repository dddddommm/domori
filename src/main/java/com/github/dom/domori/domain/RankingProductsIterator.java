package com.github.dom.domori.domain;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RankingProductsIterator implements Iterator<List<ComicTag>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RankingProductsIterator.class);

    private static final int PAGE_LIMIT = 10;

    private int currentPage = 0;

    private final RankingTerms term;

    private final String startUrl;

    private final DelayController delayController;

    public RankingProductsIterator(RankingTerms term, DelayController delay) {
        this.term = term;
        this.startUrl = String.format(EndPoints.PRODUCTS_RANKING_FORMAT, term.getTerm());
        this.delayController = delay;
    }

    @Override
    public boolean hasNext() {
        return currentPage < PAGE_LIMIT;
    }

    @Override
    public List<ComicTag> next() {
        try {
            delayController.delay();

            currentPage++;

            if (currentPage < 2) {
                Document document = Jsoup.connect(startUrl).userAgent(Constants.USER_AGENT).get();

                HTMLParser parser = new HTMLParser();
                return parser.parseRankingTags(document);
            }

            String url = String.format(EndPoints.CRAWL_PRODUCTS_API_FORMAT, currentPage, term.getTerm());
            Connection.Response response = Jsoup.connect(url).userAgent(Constants.USER_AGENT).referrer(startUrl).execute();

            String json = response.body();

            JsonParser parser = new JsonParser();
            return parser.parseRankingTags(json);

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}
