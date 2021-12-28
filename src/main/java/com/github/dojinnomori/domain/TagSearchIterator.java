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
public class TagSearchIterator implements Iterator<List<Comic>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagSearchIterator.class);

    private final ComicTag tag;

    private final DelayController delayController;

    public TagSearchIterator(ComicTag tag, DelayController delayController) {
        this.tag = tag;
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
        String url;
        switch (tag.getType()) {
            case PRODUCT -> url = String.format(EndPoints.FIRST_PAGE_TAG_COMICS_FORMAT, 1, tag.getTagId());
            case CHARACTER -> url = String.format(EndPoints.FIRST_PAGE_TAG_COMICS_FORMAT, 2, tag.getTagId());
            case PLAIN -> url = String.format(EndPoints.FIRST_PAGE_TAG_COMICS_FORMAT, 3, tag.getTagId());
            default -> {
                return Collections.emptyList();
            }
        }

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
        String url;
        switch (tag.getType()) {
            case PRODUCT -> url = String.format(EndPoints.AFTER_PAGE_TAG_COMICS_FORMAT, 1, tag.getTagId(), currentPage);
            case CHARACTER -> url = String.format(EndPoints.AFTER_PAGE_TAG_COMICS_FORMAT, 2, tag.getTagId(), currentPage);
            case PLAIN -> url = String.format(EndPoints.AFTER_PAGE_TAG_COMICS_FORMAT, 3, tag.getTagId(), currentPage);
            default -> {
                return Collections.emptyList();
            }
        }

        try {
            Document document = Jsoup.connect(url).userAgent(Constants.USER_AGENT).get();

            return parser.parseComics(document);

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}
