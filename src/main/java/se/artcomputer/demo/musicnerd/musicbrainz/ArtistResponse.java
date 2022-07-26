package se.artcomputer.demo.musicnerd.musicbrainz;

import java.util.List;

public record ArtistResponse(
        ArtistName artistName,
        ArtistGender artistGender,
        ArtistDisambiguation artistDisambiguation,
        ArtistCountry artistCountry,
        ArtistWikiUrl artistWikiUrl,
        List<ReleaseGroup> artistReleaseGroups
) {
}
