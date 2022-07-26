package se.artcomputer.demo.musicnerd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import se.artcomputer.demo.musicnerd.cover.CoverArtArchiveService;
import se.artcomputer.demo.musicnerd.cover.FrontCoverImageUrl;
import se.artcomputer.demo.musicnerd.exception.GatewayException;
import se.artcomputer.demo.musicnerd.musicbrainz.ArtistResponse;
import se.artcomputer.demo.musicnerd.musicbrainz.MusicBrainzId;
import se.artcomputer.demo.musicnerd.musicbrainz.MusicBrainzService;
import se.artcomputer.demo.musicnerd.musicbrainz.ReleaseGroup;
import se.artcomputer.demo.musicnerd.wikidata.WikidataService;
import se.artcomputer.demo.musicnerd.wikidata.WikipediaLink;
import se.artcomputer.demo.musicnerd.wikipedia.PageSummary;
import se.artcomputer.demo.musicnerd.wikipedia.WikipediaService;

import java.util.List;

@Service
public class MusicNerdService {
    private static final Logger LOG = LoggerFactory.getLogger(MusicNerdService.class);

    private final MusicBrainzService musicBrainzService;
    private final WikidataService wikidataService;
    private final WikipediaService wikipediaService;
    private final CoverArtArchiveService coverArtArchiveService;

    public MusicNerdService(MusicBrainzService musicBrainzService,
                            WikidataService wikidataService,
                            WikipediaService wikipediaService,
                            CoverArtArchiveService coverArtArchiveService) {
        this.musicBrainzService = musicBrainzService;
        this.wikidataService = wikidataService;
        this.wikipediaService = wikipediaService;
        this.coverArtArchiveService = coverArtArchiveService;
    }

    public MusicNerdResponse get(MusicBrainzId musicBrainzId) {
        ArtistResponse artistResponse = musicBrainzService.getArtist(musicBrainzId);

        Mono<PageSummary> pageSummaryMono = wikipediaService.getSummary(wikidataService.getWikipediaLink(artistResponse.artistWikiUrl().url()));
        Mono<List<Album>> albumsMono = createAlbums(artistResponse.artistReleaseGroups()).collectList();

        Tuple2<PageSummary, List<Album>> tuple2 = Mono.zip(pageSummaryMono, albumsMono).block();

        if (tuple2 != null) {
            return new MusicNerdResponse(
                    musicBrainzId,
                    artistResponse.artistName(),
                    artistResponse.artistGender(),
                    artistResponse.artistDisambiguation(),
                    artistResponse.artistCountry(),
                    new Description(tuple2.getT1().htmlString()),
                    tuple2.getT2()
            );
        }
        LOG.error("Problem calling services with id {}", musicBrainzId);
        throw new GatewayException("Problem calling services");
    }

    private Flux<Album> createAlbums(List<ReleaseGroup> releaseGroups) {
        return Flux.concat(
                releaseGroups.stream()
                        .map(this::createAlbum)
                        .toList());
    }

    private Mono<Album> createAlbum(ReleaseGroup releaseGroup) {
        Mono<FrontCoverImageUrl> coverArtUrlMono = coverArtArchiveService.getCoverArtUrl(releaseGroup.id());
        return coverArtUrlMono.map(coverArtUrl -> new Album(
                releaseGroup.id(),
                releaseGroup.title(),
                coverArtUrl.imageUrl()));
    }
}
