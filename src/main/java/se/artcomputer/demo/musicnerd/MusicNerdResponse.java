package se.artcomputer.demo.musicnerd;

import se.artcomputer.demo.musicnerd.musicbrainz.*;

public final class MusicNerdResponse {
    private final String mbid;
    private final String name;
    private final String gender;
    private final String disambiguation;
    private final String country;
    private final Description description;

    public MusicNerdResponse(
            MusicBrainzId mbid,
            ArtistName name,
            ArtistGender artistGender,
            ArtistDisambiguation artistDisambiguation,
            ArtistCountry artistCountry,
            Description description) {
        this.mbid = mbid.mbid();
        this.name = name.name();
        this.gender = artistGender.gender();
        this.disambiguation = artistDisambiguation.disambiguation();
        this.country = artistCountry.country();
        this.description = description;
    }

    public String getMbid() {
        return mbid;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getDisambiguation() {
        return disambiguation;
    }

    public String getCountry() {
        return country;
    }

    public Description getDescription() {
        return description;
    }
}
