package com.github.dom.domori.view.service;

import com.github.dom.domori.domain.ComicTag;
import com.github.dom.domori.domain.DelayController;
import com.github.dom.domori.domain.RankingTagsIterator;
import com.github.dom.domori.domain.RankingTerms;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * 並列実行させないこと
 */
public class RankingTagsService extends QueueService<List<ComicTag>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RankingTagsService.class);

    private static final int PAGE_LIMIT = 3;

    private final DelayController delayController;

    public RankingTagsService(DelayController delayController) {
        this.delayController = delayController;
    }

    private RankingTerms request;
    private RankingTerms current;
    private int page = 0;

    public void update(RankingTerms terms) {
        request = terms;
        page = 0;
        queue();
    }

    private RankingTagsIterator iterator;

    @Override
    protected Task<List<ComicTag>> createTask() {
        return new Task<>() {
            @Override
            protected List<ComicTag> call() {
                if (iterator == null || request != current) {
                    iterator = new RankingTagsIterator(request, delayController);
                }

                current = request;

                if (!(page++ < PAGE_LIMIT) || !iterator.hasNext()) {
                    return Collections.emptyList();
                }

                return iterator.next();
            }
        };
    }

    @Override
    protected void failed() {
        super.failed();
        LOGGER.error("current=" + request, getException());
    }
}
