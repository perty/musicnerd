package se.artcomputer.demo.musicnerd.wikidata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.artcomputer.demo.musicnerd.exception.NotFoundException;

@Service
public class WikidataService {
    private final WebClient webClient;

    public WikidataService(@Value("${wikidata-service}") String baseUrl) {
        final int size = 16 * 1024 * 1024;
        this.webClient = WebClient
                .builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<WikipediaLink> getWikipediaLink(String wikidataUrl) {
        String wikidataEntityId = wikidataUrl.substring(wikidataUrl.lastIndexOf('/') + 1);
        Mono<WikidataResponse> wikidataResponse = webClient
                .get()
                .uri("/wiki/Special:EntityData/" + wikidataEntityId + ".json")
                .retrieve()
                .bodyToMono(WikidataResponse.class);
        return wikidataResponse.map(response ->
        {
            WikidataEntity wikidataEntity = response.entities().get(wikidataEntityId);
            if (wikidataEntity != null) {
                SiteLink siteLink = wikidataEntity.sitelinks().get("enwiki");
                if (siteLink != null) {
                    return new WikipediaLink(siteLink.url());
                }
            }
            throw new NotFoundException("Problem with " + wikidataEntityId);
        });
    }

}

