package se.artcomputer.demo.musicnerd.wikipedia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.artcomputer.demo.musicnerd.wikidata.WikipediaLink;

@Service
public class WikipediaService {

    private static final Logger LOG = LoggerFactory.getLogger(WikipediaService.class);

    private final WebClient webClient;

    public WikipediaService(@Value("${wikipedia-service}") String baseUrl) {
        this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<PageSummary> getSummary(Mono<WikipediaLink> wikipediaLinkMono) {
        Mono<String> titleMono = wikipediaLinkMono.map(s -> s.url().substring(s.url().lastIndexOf('/') + 1));
        Mono<WikipediaResponse> wikipediaResponseMono = titleMono.flatMap(title -> webClient
                .get()
                .uri("/api/rest_v1/page/summary/" + title)
                .retrieve()
                .bodyToMono(WikipediaResponse.class));

        return wikipediaResponseMono.map(response -> new PageSummary(response.extract_html()));
    }
}
