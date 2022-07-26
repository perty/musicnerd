package se.artcomputer.demo.musicnerd.wikipedia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.artcomputer.demo.musicnerd.exception.GatewayException;

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

    public PageSummary getSummary(String wikipediaUrl) {
        String title = wikipediaUrl.substring(wikipediaUrl.lastIndexOf('/') + 1);
        WikipediaResponse wikipediaResponse = webClient
                .get()
                .uri("/api/rest_v1/page/summary/" + title)
                .retrieve()
                .bodyToMono(WikipediaResponse.class)
                .block();
        if (wikipediaResponse != null) {
            return new PageSummary(wikipediaResponse.extract_html());
        }
        LOG.error("Problem calling Wikipedia with title {}", title);
        throw new GatewayException("Problem calling Wikipedia");
    }
}
