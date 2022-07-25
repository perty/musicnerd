package se.artcomputer.demo.musicnerd.musicbrainz;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record MusicBrainzResponse(String name,
                                  String disambiguation,
                                  String gender,
                                  String country,
                                  @JsonAlias({"release-groups"}) List<ReleaseGroup> releaseGroups,
                                  List<Relation> relations
) {
}
