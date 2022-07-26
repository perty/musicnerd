package se.artcomputer.demo.musicnerd.cover;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
public class CoverArtArchiveService {

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

    public Mono<FrontCoverImageUrl> getCoverArtUrl(String releaseGroupId) {
        Mono<CoverArtArchiveResponse> coverArtArchiveResponse = webClient
                .get()
                .uri("/release-group/" + releaseGroupId)
                .retrieve()
                .bodyToMono(CoverArtArchiveResponse.class);

        return coverArtArchiveResponse.map(coverArt ->
                coverArt.images().stream()
                        .filter(CoverArtImage::front)
                        .findFirst()
                        .map(coverArtImage -> new FrontCoverImageUrl(coverArtImage.image()))
                        .orElseGet(() -> new FrontCoverImageUrl("")));
    }

}

