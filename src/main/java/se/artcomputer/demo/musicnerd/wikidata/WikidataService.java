package se.artcomputer.demo.musicnerd.wikidata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import se.artcomputer.demo.musicnerd.exception.NotFoundException;

@Service
public class WikidataService {
    private final WebClient webClient;

    public WikidataService(@Value("${wikidata-service}") String baseUrl) {
        final int size = 16 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
        this.webClient = WebClient
                .builder()
                .exchangeStrategies(strategies)
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public WikipediaLink getWikipediaLink(String wikidataUrl) {
        String wikidataEntityId = wikidataUrl.substring(wikidataUrl.lastIndexOf('/') + 1);
        WikidataResponse wikidataResponse = webClient
                .get()
                .uri("/wiki/Special:EntityData/" + wikidataEntityId + ".json")
                .retrieve()
                .bodyToMono(WikidataResponse.class)
                .block();
        if (wikidataResponse != null) {
            WikidataEntity wikidataEntity = wikidataResponse.entities().get(wikidataEntityId);
            if (wikidataEntity != null) {
                SiteLink enwiki = wikidataEntity.sitelinks().get("enwiki");
                if (enwiki != null) {
                    return new WikipediaLink(enwiki.url());
                }
            }
        }
        throw new NotFoundException("Problem with " + wikidataEntityId);
    }
}
