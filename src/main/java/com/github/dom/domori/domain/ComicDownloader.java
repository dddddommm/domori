package com.github.dom.domori.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ComicDownloader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComicDownloader.class);

    public static record Result(boolean isSucceeded, Path savePath, LocalDateTime saveDateTime, String failedMessage) {
    }

    public static interface ProgressListener {
        void readTotalByteLength(long totalByteLength);
    }

    private final List<ProgressListener> listeners = new ArrayList<>(2);

    private final DelayController delayController;

    private Path saveDirectory;

    public ComicDownloader(DelayController delayController) {
        this(delayController, Paths.get(System.getProperty("user.dir")));
    }

    public ComicDownloader(DelayController delayController, Path saveDirectory) {
        this.delayController = delayController;
        this.saveDirectory = saveDirectory;
    }

    public void addListener(ProgressListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ProgressListener listener) {
        listeners.remove(listener);
    }

    public Path getSaveDirectory() {
        return saveDirectory;
    }

    public void setSaveDirectory(Path saveDirectory) {
        this.saveDirectory = saveDirectory;
    }

    private static final String[] PROHIBITED_SYMBOLS = {
            "\\", "/", ":", "*", "?", "&#039;", "&quot;", "<", ">"
    };

    public Result save(Comic comic) {
        delayController.delay();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(URI.create(comic.getUrl())).build();

        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() != 200) {
                return new Result(false, null, null, "ステータスコード: " + response.statusCode());
            }

            String fileName = comic.getTitle() + ".pdf";
            for (String prohibit : PROHIBITED_SYMBOLS) {
                fileName = fileName.replace(prohibit, "_");
            }

            Path savePath = saveDirectory.resolve(fileName);

            int length = 0;
            long totalByteLength = 0;
            byte[] buffer = new byte[1024 * 8];
            try (InputStream is = response.body(); OutputStream os = Files.newOutputStream(savePath)) {

                while (0 < (length = is.read(buffer))) {
                    os.write(buffer, 0, length);
                    totalByteLength += length;

                    for (ProgressListener listener : listeners) {
                        listener.readTotalByteLength(totalByteLength);
                    }
                }
            }

            LocalDateTime saveDate = LocalDateTime.ofInstant(Files.getLastModifiedTime(savePath).toInstant(), ZoneId.systemDefault());

            return new Result(true, savePath, saveDate, "");

        } catch (IOException | InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
            return new Result(false, null, null, e.getMessage());
        }
    }

}
