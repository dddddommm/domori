package com.github.dojinnomori.domain;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class PDFSaver {

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFSaver.class);

    public void save(Comic comic, Path savePath) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(URI.create(comic.getUrl())).build();

        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() != 200) {
                return;
            }

            System.out.println(response.statusCode());
            System.out.println(response.headers());

            try (InputStream is = response.body(); OutputStream os = Files.newOutputStream(savePath)) {
                is.transferTo(os);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
