package se.artcomputer.demo.musicnerd;

import org.springframework.stereotype.Service;
import se.artcomputer.demo.musicnerd.musicbrainz.ArtistResponse;
import se.artcomputer.demo.musicnerd.musicbrainz.MusicBrainzId;
import se.artcomputer.demo.musicnerd.musicbrainz.MusicBrainzService;
import se.artcomputer.demo.musicnerd.wikidata.WikidataService;
import se.artcomputer.demo.musicnerd.wikidata.WikipediaLink;
import se.artcomputer.demo.musicnerd.wikipedia.PageSummary;
import se.artcomputer.demo.musicnerd.wikipedia.WikipediaService;

@Service
public class MusicNerdService {
    private final MusicBrainzService musicBrainzService;
    private final WikidataService wikidataService;
    private final WikipediaService wikipediaService;

    public MusicNerdService(MusicBrainzService musicBrainzService, WikidataService wikidataService, WikipediaService wikipediaService) {
        this.musicBrainzService = musicBrainzService;
        this.wikidataService = wikidataService;
        this.wikipediaService = wikipediaService;
    }

    public MusicNerdResponse get(MusicBrainzId musicBrainzId) {
        ArtistResponse artistResponse = musicBrainzService.getArtist(musicBrainzId);
        WikipediaLink wikipediaLink = wikidataService.getWikipediaLink(artistResponse.artistWikiUrl().url());
        PageSummary pageSummary = wikipediaService.getSummary(wikipediaLink.url());
        return new MusicNerdResponse(
                musicBrainzId,
                artistResponse.artistName(),
                artistResponse.artistGender(),
                artistResponse.artistDisambiguation(),
                artistResponse.artistCountry(),
                new Description(pageSummary.htmlString())
        );
    }
}
