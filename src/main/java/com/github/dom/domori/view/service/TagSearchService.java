package com.github.dom.domori.view.service;

import com.github.dom.domori.domain.Comic;
import com.github.dom.domori.domain.ComicTag;
import com.github.dom.domori.domain.DelayController;
import com.github.dom.domori.domain.TagSearchIterator;
import javafx.concurrent.Task;

import java.util.Collections;
import java.util.List;

public class TagSearchService extends QueueService<List<Comic>> {

    private final DelayController delayController;

    public TagSearchService(DelayController delayController) {
        this.delayController = delayController;
    }

    private ComicTag tag;

    public void startSearch(ComicTag tag) {
        this.tag = tag;
        queue();
    }

    private TagSearchIterator iterator;

    @Override
    protected Task<List<Comic>> createTask() {
        return new Task<>() {
            @Override
            protected List<Comic> call() throws Exception {
                if (iterator == null || !iterator.getTag().equals(tag)) {
                    iterator = new TagSearchIterator(tag, delayController);
                }

                if (!iterator.hasNext()) {
                    return Collections.emptyList();
                }

                return iterator.next();
            }
        };
    }
}
