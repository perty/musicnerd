package se.artcomputer.demo.musicnerd.wikidata;

import java.util.Map;

record WikidataResponse(Map<String, WikidataEntity> entities) {
}
