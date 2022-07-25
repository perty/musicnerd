package se.artcomputer.demo.musicnerd;

import se.artcomputer.demo.musicnerd.musicbrainz.ArtistName;
import se.artcomputer.demo.musicnerd.musicbrainz.MusicBrainzId;

public final class MusicNerdResponse {
    private final String mbid;
    private final String name;

    public MusicNerdResponse(MusicBrainzId mbid, ArtistName name) {
        this.mbid = mbid.mbid();
        this.name = name.name();
    }

    public String getMbid() {
        return mbid;
    }

    public String getName() {
        return name;
    }
}
