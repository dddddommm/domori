package com.github.dom.domori.domain;

import java.util.Objects;

public class ComicTag {

    private final String title;

    private final long tagId;

    private final TagTypes type;

    public ComicTag(long tagId, String title, TagTypes type) {
        this.title = title;
        this.tagId = tagId;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public long getTagId() {
        return tagId;
    }

    public TagTypes getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComicTag comicTag = (ComicTag) o;
        return tagId == comicTag.tagId && title.equals(comicTag.title) && type == comicTag.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, tagId, type);
    }

    @Override
    public String toString() {
        return "ComicTag{" +
                "title='" + title + '\'' +
                ", tagId=" + tagId +
                ", type=" + type +
                '}';
    }
}
