package com.github.dojinnomori.domain;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * タグ、作品名、キャラクター名による検索
 */
public class KeywordSearchIterator implements Iterator<List<Comic>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeywordSearchIterator.class);

    private final String keyword;

    private final DelayController delayController;

    public KeywordSearchIterator(String keyword, DelayController delayController) {
        this.keyword = keyword;
        this.delayController = delayController;
    }

    @Override
    public boolean hasNext() {
        return lastPage < 0 || currentPage < lastPage-1;
    }

    private final HTMLParser parser = new HTMLParser();
    private int currentPage = 0;
    private int lastPage = -1;

    @Override
    public List<Comic> next() {

        delayController.delay();

        currentPage++;
        if (currentPage == 1) {
            return firstPage();
        }

        return afterPage();
    }

    private List<Comic> firstPage() {
        String url = String.format(EndPoints.FIRST_PAGE_FREE_SEARCH_FORMAT, keyword);

        try {
            Document document = Jsoup.connect(url).userAgent(Constants.USER_AGENT).get();

            lastPage = parser.parseLastPage(document);

            return parser.parseComics(document);

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private List<Comic> afterPage() {
        String url = String.format(EndPoints.AFTER_PAGE_FREE_SEARCH_FORMAT, keyword, currentPage);

        try {
            Document document = Jsoup.connect(url).userAgent(Constants.USER_AGENT).get();

            return parser.parseComics(document);

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}
