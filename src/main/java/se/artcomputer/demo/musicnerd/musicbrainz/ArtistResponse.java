package se.artcomputer.demo.musicnerd.musicbrainz;

public record ArtistResponse(
        ArtistName artistName,
        ArtistGender artistGender,
        ArtistDisambiguation artistDisambiguation,
        ArtistCountry artistCountry,
        ArtistWikiUrl artistWikiUrl
) {
}
