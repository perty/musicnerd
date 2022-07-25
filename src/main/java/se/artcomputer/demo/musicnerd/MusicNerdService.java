package se.artcomputer.demo.musicnerd;

import org.springframework.stereotype.Service;
import se.artcomputer.demo.musicnerd.musicbrainz.ArtistResponse;
import se.artcomputer.demo.musicnerd.musicbrainz.MusicBrainzId;
import se.artcomputer.demo.musicnerd.musicbrainz.MusicBrainzService;

@Service
public class MusicNerdService {
    private final MusicBrainzService musicBrainzService;

    public MusicNerdService(MusicBrainzService musicBrainzService) {
        this.musicBrainzService = musicBrainzService;
    }

    public MusicNerdResponse get(MusicBrainzId musicBrainzId) {
        ArtistResponse artistResponse = musicBrainzService.getArtist(musicBrainzId);
        return new MusicNerdResponse(musicBrainzId, artistResponse.artistName());
    }
}
