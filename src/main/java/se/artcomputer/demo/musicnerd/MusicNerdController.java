package se.artcomputer.demo.musicnerd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import se.artcomputer.demo.musicnerd.musicbrainz.MusicBrainzId;

@RestController
public class MusicNerdController {

    private final MusicNerdService musicNerdService;

    public MusicNerdController(MusicNerdService musicNerdService) {
        this.musicNerdService = musicNerdService;
    }

    @GetMapping("/musify/music-artist/details/{mbid}")
    public MusicNerdResponse get(@PathVariable String mbid) {
        return musicNerdService.get(MusicBrainzId.fromString(mbid));
    }
}
