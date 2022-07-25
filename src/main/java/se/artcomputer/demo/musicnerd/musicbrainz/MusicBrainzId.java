package se.artcomputer.demo.musicnerd.musicbrainz;

public record MusicBrainzId(String mbid) {
    public static MusicBrainzId fromString(String mbid) {
        return new MusicBrainzId(mbid);
    }

}
