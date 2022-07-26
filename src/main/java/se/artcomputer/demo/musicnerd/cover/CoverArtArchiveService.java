package se.artcomputer.demo.musicnerd.cover;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import se.artcomputer.demo.musicnerd.exception.GatewayException;

@Service
public class CoverArtArchiveService {

    private static final Logger LOG = LoggerFactory.getLogger(CoverArtArchiveService.class);

    private final WebClient webClient;

    public CoverArtArchiveService(@Value("${cover-art-archive-service}") String baseUrl) {
        this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().followRedirect(true)
                ))
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

    }

    public FrontCoverImageUrl getCoverArtUrl(String releaseGroupId) {
        CoverArtArchiveResponse coverArtArchiveResponse = webClient
                .get()
                .uri("/release-group/" + releaseGroupId)
                .retrieve()
                .bodyToMono(CoverArtArchiveResponse.class)
                .block();
        if (coverArtArchiveResponse != null) {
            return coverArtArchiveResponse.images().stream()
                    .filter(CoverArtImage::front)
                    .findFirst()
                    .map(coverArtImage -> new FrontCoverImageUrl(coverArtImage.image()))
                    .orElseGet(() -> new FrontCoverImageUrl(""));
        }
        LOG.error("Problem calling Cover Art Archive with id {}", releaseGroupId);
        throw new GatewayException("Problem calling Cover Art Archive");
    }
}
