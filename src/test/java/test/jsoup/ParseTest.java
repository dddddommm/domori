package test.jsoup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dom.domori.domain.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ParseTest {

    private static final DelayController delayController = new DelayController(TimeUnit.SECONDS, 1, 0);

    public static void main(String...args) {
//        parseContentList();
//        parseContent();
//        parseContentTags();
//        parseTagsJson();
//        parseTagList();
//        parseComicsJson();
//        containsNextPage();
//        containsPreviousPage();
//        lastPage();
//        getPDF();

//        TagSearchIterator search = new TagSearchIterator(new ComicTag(17455, "謎のヒロインなんとか", TagTypes.CHARACTER), delayController);
//        while (search.hasNext()) {
//            search.next().forEach(c -> System.out.println(c.getTitle()));
//        }

//        RankingComicsProvider provider = new RankingComicsProvider(RankingTerms.WEEKLY);
//        try {
//            provider.next().forEach(c -> System.out.println(c.getTitle()));
//            System.out.println();
//
//            provider.next().forEach(c -> System.out.println(c.getTitle()));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            HTMLParser parser = new HTMLParser();
            Document doc = Jsoup.connect("http://doujinnomori.com/ranking/products?term=weekly").get();

            parser.parseRankingTags(doc).forEach(t -> System.out.println(t));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseContentList() {
        String source;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ParseTest.class.getResourceAsStream("ContentList.html"))))) {
            source = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Document document = Jsoup.parse(source, EndPoints.BASE_URL);

        document.select("ul.comic__list").select("li").forEach(element -> {
            Elements a = element.select("a");

            if (!a.hasAttr("href")) {
                return;
            }

            String href = a.attr("abs:href");
            String uuid = a.attr("class");
            String thumbnail = element.select("img").attr("src");
            String title = element.select("span").text();

            System.out.println("href = " + href);
            System.out.println("uuid = " + uuid);
            System.out.println("thumbnail = " + thumbnail);
            System.out.println("title = " + title);
            System.out.println();
        });

        System.out.println("done parseContentList");
    }

    private static void parseContent() {
        String source;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ParseTest.class.getResourceAsStream("Content.html"))))) {
            source = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Document document = Jsoup.parse(source, EndPoints.BASE_URL);

        document.select("div.view").select("div.view__right").select("a.bgcolor__red").forEach(element -> {
            String href = element.attr("abs:href");

            System.out.println("href = " + href);
            System.out.println();
        });

        System.out.println("done parseContent");
    }

    private static void parseContentTags() {
        String source;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ParseTest.class.getResourceAsStream("Content.html"))))) {
            source = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Document document = Jsoup.parse(source, EndPoints.BASE_URL);

        document.select("div.tag").select("li.tag__item").select("a").forEach(element -> {
            String href = element.attr("abs:href");
            String tagName = element.text();

            System.out.println("href = " + href);
            System.out.println("name = " + tagName);
            System.out.println();
        });

        System.out.println("done parseContentTags");
    }

    private static void parseTagsJson() {
        String source;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ParseTest.class.getResourceAsStream("api/Tags.json"))))) {
            source = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(source);

            node.forEach(n -> System.out.println(n.get("name").asText()));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("done parseTagsJson");
    }

    private static void parseTagList() {
        String source;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ParseTest.class.getResourceAsStream("TagList.html"))))) {
            source = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Document document = Jsoup.parse(source, EndPoints.BASE_URL);

        document.select("ul.comic__list").select("li").forEach(element -> {
            Elements a = element.select("a");

            if (!a.hasAttr("href")) {
                return;
            }

            String href = a.attr("abs:href");
            String title = element.select("span").text();

            System.out.println("href = " + href);
            System.out.println("title = " + title);
            System.out.println();
        });

        System.out.println("done parseTagList");
    }

    private static void parseComicsJson() {
        String source;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ParseTest.class.getResourceAsStream("api/Comics.json"))))) {
            source = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(source);

            node.forEach(n -> {
                String name = n.get("name").asText();
                String uuid = n.get("uuid").asText();
                String thumbnail = n.get("thumbnailUrl").asText();

                System.out.println("name = " + name);
                System.out.println("uuid = " + uuid);
                System.out.println("thumbnail = " + thumbnail);
                System.out.println();
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("done parseComicsJson");
    }

    private static void containsNextPage() {
        String source;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ParseTest.class.getResourceAsStream("ContentList.html"))))) {
            source = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Document document = Jsoup.parse(source, EndPoints.BASE_URL);

        document.select("li.pagination__item").forEach(element -> {
            String text = element.text();
            if (">".equals(text)) {
                System.out.println("has next");
            }
        });

        System.out.println("done containsNextPage");
    }

    private static void containsPreviousPage() {
        String source;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ParseTest.class.getResourceAsStream("SearchLastPage.html"))))) {
            source = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Document document = Jsoup.parse(source, EndPoints.BASE_URL);

        document.select("li.pagination__item").forEach(element -> {
            String text = element.text();
            // Jsoupのパーサの関係でこう
            if ("<< /a>".equals(text)) {
                System.out.println("has previous");
            }
        });

        System.out.println("done containsPreviousPage");
    }

    private static void lastPage() {
        String source;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ParseTest.class.getResourceAsStream("ContentList.html"))))) {
            source = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Document document = Jsoup.parse(source, EndPoints.BASE_URL);

        Element last = document.select("li.pagination__item").last();
        if (last == null) {
            System.out.println("current page is last");
            return;
        }

        System.out.println(last.select("a").attr("abs:href"));

        System.out.println("done lastPage");
    }

}
