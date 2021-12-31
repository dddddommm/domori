package com.github.dom.domori.view;

import com.github.dom.domori.domain.Comic;
import com.github.dom.domori.domain.ComicDownloader;
import javafx.beans.property.*;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;

public class ComicListItem {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComicListItem.class);

    private final Comic comic;

    private final ComicDownloader downloader;

    private final ExecutorService downloadExecutor;

    public ComicListItem(int number, Comic comic, ComicDownloader downloader, ExecutorService downloadExecutor) {
        this(number, comic, downloader, downloadExecutor, null);
    }

    public ComicListItem(int number, Comic comic, ComicDownloader downloader, ExecutorService downloadExecutor, LocalDateTime downloadedDateTime) {
        this.comic = comic;
        this.downloader = downloader;
        this.downloadExecutor = downloadExecutor;

        numberProperty = new ReadOnlyIntegerWrapper(number);
        titleProperty = new ReadOnlyStringWrapper(comic.getTitle());
        thumbnailProperty = new ReadOnlyStringWrapper(comic.getThumbnailUrl());
        downloadedDateTimeProperty = new ReadOnlyObjectWrapper<>(downloadedDateTime);
    }

    public Comic getComic() {
        return comic;
    }

    public void download() {
        canDownloadProperty.set(false);

        Task<ComicDownloader.Result> task = new Task<ComicDownloader.Result>() {
            @Override
            protected ComicDownloader.Result call() throws Exception {
                return downloader.save(comic);
            }
        };

        task.setOnSucceeded(e -> {
            var result = task.getValue();

            if (result.isSucceeded()) {
                downloadedDateTimeProperty.set(result.saveDateTime());
            }

            canDownloadProperty.set(true);
        });

        task.setOnFailed(e -> {
            LOGGER.warn("ダウンロードに失敗 guid={}, title={}", comic.getGuid(), comic.getTitle());
            canDownloadProperty.set(true);
        });

        downloadExecutor.submit(task);
    }

    private final ReadOnlyIntegerWrapper numberProperty;

    public ReadOnlyIntegerProperty numberProperty() {
        return numberProperty.getReadOnlyProperty();
    }

    public int getNumber() {
        return numberProperty.get();
    }

    private final ReadOnlyStringWrapper titleProperty;

    public ReadOnlyStringProperty titleProperty() {
        return titleProperty;
    }

    public String getTitle() {
        return titleProperty.get();
    }

    private final ReadOnlyStringWrapper thumbnailProperty;

    public ReadOnlyStringProperty thumbnailProperty() {
        return thumbnailProperty.getReadOnlyProperty();
    }

    private final ReadOnlyObjectWrapper<LocalDateTime> downloadedDateTimeProperty;

    public ReadOnlyObjectProperty<LocalDateTime> downloadedDateTimeProperty() {
        return downloadedDateTimeProperty;
    }

    public LocalDateTime getDownloadedDate() {
        return downloadedDateTimeProperty.get();
    }

    public boolean isDownloaded() {
        return downloadedDateTimeProperty.get() != null;
    }

    private final ReadOnlyBooleanWrapper canDownloadProperty = new ReadOnlyBooleanWrapper(false);

    public ReadOnlyBooleanProperty canDownloadProperty() {
        return canDownloadProperty.getReadOnlyProperty();
    }

    public boolean canDownload() {
        return canDownloadProperty.get();
    }

    private Image image;

    public Image getImage() {
        if (image == null) {
            image = new Image(comic.getThumbnailUrl(), 150, 150, true, true, true);
        }
        return image;
    }
}
