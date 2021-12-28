package com.github.dojinnomori.domain;

import java.util.Objects;

public class Comic {

    private final String guid;

    private final String title;

    private final String thumbnailUrl;

    private final String url;

    public Comic(String guid, String title, String thumbnailUrl) {
        this.guid = guid;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.url = String.format(EndPoints.COMIC_FORMAT, guid);
    }

    public String getGuid() {
        return guid;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comic comic = (Comic) o;
        return guid.equals(comic.guid) && title.equals(comic.title) && thumbnailUrl.equals(comic.thumbnailUrl) && url.equals(comic.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, title, thumbnailUrl, url);
    }
}
