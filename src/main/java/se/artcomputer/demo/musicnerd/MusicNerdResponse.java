package se.artcomputer.demo.musicnerd;

import se.artcomputer.demo.musicnerd.musicbrainz.*;

import java.util.List;

public record MusicNerdResponse(
        MusicBrainzId mbid,
        ArtistName name,
        ArtistGender artistGender,
        ArtistDisambiguation artistDisambiguation,
        ArtistCountry artistCountry,
        Description description,
        List<Album> albums) {
}
