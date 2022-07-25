package se.artcomputer.demo.musicnerd;

import org.springframework.stereotype.Service;
import se.artcomputer.demo.musicnerd.musicbrainz.ArtistResponse;
import se.artcomputer.demo.musicnerd.musicbrainz.MusicBrainzId;
import se.artcomputer.demo.musicnerd.musicbrainz.MusicBrainzService;
import se.artcomputer.demo.musicnerd.wikidata.WikidataResponse;
import se.artcomputer.demo.musicnerd.wikidata.WikidataService;
import se.artcomputer.demo.musicnerd.wikidata.WikipediaLink;

@Service
public class MusicNerdService {
    private final MusicBrainzService musicBrainzService;
    private final WikidataService wikidataService;

    public MusicNerdService(MusicBrainzService musicBrainzService, WikidataService wikidataService) {
        this.musicBrainzService = musicBrainzService;
        this.wikidataService = wikidataService;
    }

    public MusicNerdResponse get(MusicBrainzId musicBrainzId) {
        ArtistResponse artistResponse = musicBrainzService.getArtist(musicBrainzId);
        WikipediaLink wikipediaLink = wikidataService.getWikipediaLink(artistResponse.artistWikiUrl().url());
        return new MusicNerdResponse(
                musicBrainzId,
                artistResponse.artistName(),
                artistResponse.artistGender(),
                artistResponse.artistDisambiguation(),
                artistResponse.artistCountry()
        );
    }
}
