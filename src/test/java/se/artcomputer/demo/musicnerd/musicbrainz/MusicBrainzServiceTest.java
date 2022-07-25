package se.artcomputer.demo.musicnerd.musicbrainz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MusicBrainzServiceTest {

    private static final String SOME_NAME = "some name";
    private static final String SOME_DISAMBIGUATION = "some disambiguation";
    private static final String SOME_GENDER = "Female";
    private static final String SOME_COUNTRY = "SE";
    private static final String SOME_MBID = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee";
    private MockWebServer mockBackEnd;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MusicBrainzService musicBrainzService;

    @BeforeEach
    void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        String baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());
        musicBrainzService = new MusicBrainzService(baseUrl);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void getArtist() throws JsonProcessingException {
        MusicBrainzResponse mockResponse = new MusicBrainzResponse(
                SOME_NAME,
                SOME_DISAMBIGUATION,
                SOME_GENDER,
                SOME_COUNTRY,
                Collections.emptyList()
        );
        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockResponse))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        ArtistResponse artist = musicBrainzService.getArtist(new MusicBrainzId(SOME_MBID));

        assertThat(artist.artistName(), is(new ArtistName(SOME_NAME)));
        assertThat(artist.artistDisambiguation(), is(new ArtistDisambiguation(SOME_DISAMBIGUATION)));
        assertThat(artist.artistGender(), is(new ArtistGender(SOME_GENDER)));
        assertThat(artist.artistCountry(), is(new ArtistCountry(SOME_COUNTRY)));
    }
}