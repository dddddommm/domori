package com.github.dom.domori.domain;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParser {

    /**
     * コミック表示用のHTMLパーサ
     * @param element コミック表示ページ
     * @return コミックの情報
     */
    public List<Comic> parseComics(Element element) {
        List<Comic> comics = new ArrayList<>();

        element.select("ul.comic__list").select("li").forEach(e -> {
            Elements a = e.select("a");

            if (!a.hasAttr("href")) {
                return;
            }

            // URLは手動で構築することにした
//            String href = a.attr("abs:href");
            String uuid = a.attr("class");
            String thumbnail = e.select("img").attr("src");
            String title = e.select("span").text().replace("登録 ", "");

            comics.add(new Comic(uuid, title, thumbnail));
        });

        return comics;
    }

    private final Pattern lastNumberRegex = Pattern.compile("\\d+$");

    /**
     * コミックに関連付くタグを取り出す
     * @param element コミック表示ページ
     * @return タグ一覧
     */
    public List<ComicTag> parseContentTags(Element element) {
        List<ComicTag> tags = new ArrayList<>();

        element.select("div.tag").select("li.tag__item").select("a").forEach(e -> {
            String href = e.attr("abs:href");
            String tagName = e.text();

            Matcher matcher = lastNumberRegex.matcher(href);
            if (!matcher.find()) {
                return;
            }

            long tagId = Long.parseLong(matcher.group());

            TagTypes type;
            if (href.contains("type=1")) {
                type = TagTypes.PRODUCT;
            } else if (href.contains("type=2")) {
                type = TagTypes.CHARACTER;
            } else if (href.contains("type=3")) {
                type = TagTypes.PLAIN;
            } else {
                return;
            }

            tags.add(new ComicTag(tagId, tagName, type));
        });

        return tags;
    }

    public List<ComicTag> parseRankingTags(Element element) {
        List<ComicTag> tags = new ArrayList<>();

        element.select("ul.comic__list").select("li").select("a").forEach(e -> {
            if (!e.hasAttr("href")) {
                return;
            }

            String href = e.attr("abs:href");
            String tagName = e.text();

            Matcher matcher = lastNumberRegex.matcher(href);
            if (!matcher.find()) {
                return;
            }

            long tagId = Long.parseLong(matcher.group());

            TagTypes type;
            if (href.contains("type=1")) {
                type = TagTypes.PRODUCT;
            } else if (href.contains("type=2")) {
                type = TagTypes.CHARACTER;
            } else if (href.contains("type=3")) {
                type = TagTypes.PLAIN;
            } else {
                return;
            }

            tags.add(new ComicTag(tagId, tagName, type));
        });

        return tags;
    }

    /**
     * コミック表示ページのPDFへのリンクを取り出す
     * @param element コミック表示ページ
     * @return PDFへのリンク
     */
    public Optional<String> parseContentLink(Element element) {
        return element.select("div.view").select("div.view__right").select("a.bgcolor__red")
                .stream()
                .filter(e -> e.hasAttr("href"))
                .map(e -> e.attr("abs:href"))
                .findFirst();
    }

    public boolean containsNextPage(Element element) {
        boolean hasNext = false;

        for (Element e : element.select("li.pagination__item")) {
            String text = e.text();
            if (">".equals(text)) {
                hasNext = true;
                break;
            }
        }

        return hasNext;
    }

    public boolean containsPreviousPage(Element element) {
        boolean hasPrevious = false;

        for (Element e : element.select("li.pagination__item")) {
            String text = e.text();
            // Jsoupのパーサの関係でこう
            if ("<< /a>".equals(text)) {
                hasPrevious = true;
                break;
            }
        }

        return hasPrevious;
    }

    public int parseLastPage(Element element) {
        Element last = element.select("li.pagination__item").last();
        if (last == null) {
            return -1;
        }

        String link = last.select("a").attr("abs:href");
        Matcher matcher = lastNumberRegex.matcher(link);
        if (!matcher.find()) {
            return -1;
        }

        return Integer.parseInt(matcher.group());
    }

}
