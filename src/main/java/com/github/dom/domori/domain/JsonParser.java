package com.github.dom.domori.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonParser.class.getName());

    public List<ComicTag> parseRankingTags(String json) {
        List<ComicTag> tags = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(json);

            node.forEach(n -> {
                String name = n.get("name").asText();
                long id = n.get("id").asLong();

                tags.add(new ComicTag(id, name, TagTypes.PLAIN));
            });

        } catch (JsonProcessingException e) {
            LOGGER.error("タグランキング(json)のパース失敗", e);
        }

        return tags;
    }

    public List<Comic> parseRankingComics(String json) {
        List<Comic> comics = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(json);

            node.forEach(n -> {
                String name = n.get("name").asText();
                String uuid = n.get("uuid").asText();
                String thumbnail = n.get("thumbnailUrl").asText();

                comics.add(new Comic(uuid, name, thumbnail));
            });

        } catch (JsonProcessingException e) {
            LOGGER.error("コミックランキング(json)のパース失敗", e);
        }

        return comics;
    }

}
