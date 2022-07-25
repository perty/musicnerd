package se.artcomputer.demo.musicnerd.wikidata;

import java.util.Map;

public record WikidataResponse(Map<String, WikidataEntity> entities) {
}
