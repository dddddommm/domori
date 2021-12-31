package com.github.dom.domori.domain;

public class EndPoints {

    public static final String BASE_URL = "http://doujinnomori.com/";

    /**
     * 任意のコミックのUUIDが%s
     */
    public static final String COMIC_FORMAT = "http://doujinnomori.com/comics/detail?uuid=%s&type=prev";

    /**
     * 末尾に%s - エンコードしたキーワード
     */
    public static final String FIRST_PAGE_FREE_SEARCH_FORMAT = "http://doujinnomori.com/comics/free-search?type=&id=&keyword=%s";

    /**
     * %s - キーワード
     * %d - ページ番号
     */
    public static final String AFTER_PAGE_FREE_SEARCH_FORMAT = "http://doujinnomori.com/comics/free-search?type=&id=&keyword=%s&page=2";

    /**
     * %d - カテゴリ
     * %d - id
     */
    public static final String FIRST_PAGE_TAG_COMICS_FORMAT = "http://doujinnomori.com/comics/search?type=%d&id=%d";

    /**
     * %d - カテゴリ
     * %d - id
     * %d - ページ番号(2から)
     */
    public static final String AFTER_PAGE_TAG_COMICS_FORMAT = "http://doujinnomori.com/comics/search?type=%d&id=%d&page=%d";

    /**
     * ページ番号・期間名
     * 続きのタグ検索用API
     * {@see TAGS_RANKING_URL}がリファラに必要
     */
    public static final String CRAWL_TAGS_API_FORMAT = "http://doujinnomori.com/api/ranking-tags?page=%d&term=%s";

    /**
     * ページ番号
     * 続きのコミック探索用API
     */
    public static final String CRAWL_COMICS_API_FORMAT = "http://doujinnomori.com/api/ranking-comics?page=%d&term=%s";

    /**
     * %d - ページ番号
     * %s - 期間
     */
    public static final String CRAWL_PRODUCTS_API_FORMAT = "http://doujinnomori.com/api/ranking-products?page=%d&term=%s";

    /**
     * %s - 期間
     * タグランキング
     */
    public static final String TAGS_RANKING_FORMAT = "http://doujinnomori.com/ranking/tags?term=%s";

    /**
     * %s - 期間
     * コミックランキング
     */
    public static final String COMICS_RANKING_FORMAT = "http://doujinnomori.com/ranking/comics?term=%s";

    /**
     * %s - 期間
     * 原作ランキング
     */
    public static final String PRODUCTS_RANKING_FORMAT = "http://doujinnomori.com/ranking/products?term=%s";

}
