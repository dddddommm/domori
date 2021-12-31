package com.github.dom.domori.view;

import com.github.dom.domori.domain.ComicDownloader;
import com.github.dom.domori.domain.ComicTag;
import com.github.dom.domori.domain.DelayController;
import com.github.dom.domori.domain.RankingTerms;
import com.github.dom.domori.view.service.RankingProductsService;
import com.github.dom.domori.view.service.RankingTagsService;
import com.github.dom.domori.view.service.TagSearchService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class MainViewModel {

    public final ObservableList<ComicTag> rankingTags = FXCollections.observableArrayList();

    public final ObservableList<ComicTag> rankingProducts = FXCollections.observableArrayList();

    public final ObservableList<ComicListItem> comics = FXCollections.observableArrayList();

    private final DelayController downloadDelay = new DelayController(TimeUnit.SECONDS, 1, 0);

    private final DelayController delayController = new DelayController(TimeUnit.MILLISECONDS, 100, 0);

    private final ExecutorService downloadExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("download-thread");
        return t;
    });

    private final ExecutorService fixedExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("single-thread");
        return t;
    });

    private final RankingProductsService rankingProductsService = new RankingProductsService(delayController);
    private final RankingTagsService rankingTagsService = new RankingTagsService(delayController);
    private final TagSearchService tagSearchService = new TagSearchService(delayController);

    private final ComicDownloader downloader = new ComicDownloader(downloadDelay);

    public MainViewModel() {
        initServices();
        init();
    }

    private void initServices() {
        rankingProductsService.setExecutor(fixedExecutor);
        rankingProductsService.setOnSucceeded(e -> {
            rankingProducts.addAll(rankingProductsService.getValue());
        });
        rankingTagsService.setExecutor(fixedExecutor);
        rankingTagsService.setOnSucceeded(e -> {
            rankingTags.addAll(rankingTagsService.getValue());
        });
        tagSearchService.setExecutor(fixedExecutor);
        tagSearchService.setOnSucceeded(e -> {
            comics.addAll(tagSearchService.getValue().stream().map(c -> new ComicListItem(1, c, downloader, downloadExecutor)).collect(Collectors.toList()));
        });
    }

    private void init() {
        rankingTags.clear();
        rankingProducts.clear();
        comics.clear();
    }

    public void updateTagRankingTerm(RankingTerms terms) {
        rankingTags.clear();
        rankingTagsService.update(terms);
    }

    public void crawlTagRanking() {
    }

    public void updateProductsRankingTerm(RankingTerms terms) {
        rankingProducts.clear();
        rankingProductsService.update(terms);
    }

    public void crawlProductRanking() {
        rankingProductsService.queue();
    }

    public void openRanking(ComicTag tag) {
        comics.clear();
        tagSearchService.startSearch(tag);
    }

}
