package se.artcomputer.demo.musicnerd.musicbrainz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.artcomputer.demo.musicnerd.exception.GatewayException;

import java.util.Optional;

@Service
public class MusicBrainzService {
    private static final Logger LOG = LoggerFactory.getLogger(MusicBrainzService.class);

    private final WebClient webClient;

    public MusicBrainzService(@Value("${music-brainz-service}") String baseURL) {
        this.webClient = WebClient
                .builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public ArtistResponse getArtist(MusicBrainzId musicBrainzId) {
        MusicBrainzResponse musicBrainzResponse =
                webClient
                        .get()
                        .uri("/ws/2/artist/{mbid}?&inc=url-rels+release-groups", musicBrainzId.mbid())
                        .retrieve()
                        .bodyToMono(MusicBrainzResponse.class)
                        .block();
        if (musicBrainzResponse != null) {
            return new ArtistResponse(
                    new ArtistName(musicBrainzResponse.name()),
                    new ArtistGender(musicBrainzResponse.gender()),
                    new ArtistDisambiguation(musicBrainzResponse.disambiguation()),
                    new ArtistCountry(musicBrainzResponse.country()),
                    new ArtistWikiUrl(getWikidataUrl(musicBrainzResponse))
            );
        }
        LOG.error("Null response from MusicBrainz for id: {}", musicBrainzId);
        throw new GatewayException("Null response from MusicBrainz");
    }

    private String getWikidataUrl(MusicBrainzResponse musicBrainzResponse) {
        Optional<Relation> wikidata = musicBrainzResponse.relations().stream().filter(r -> r.type().equals("wikidata")).findFirst();
        if (wikidata.isPresent()) {
            return wikidata.get().url().resource();
        }
        return "";
    }
}
