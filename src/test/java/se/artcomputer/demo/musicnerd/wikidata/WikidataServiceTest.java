package se.artcomputer.demo.musicnerd.wikidata;

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
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class WikidataServiceTest {
    private static final String SOME_WIKI_DATA_URL_ENTITY_ID = "Q1337";
    private static final String SOME_WIKIPEDIA_URL = "https://en.wikipedia.org/";
    private MockWebServer mockBackEnd;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private WikidataService wikidataService;

    @BeforeEach
    void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
        wikidataService = new WikidataService(baseUrl);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void getWikipediaLink() throws JsonProcessingException {
        Map<String, WikidataEntity> theEntity = new HashMap<>();
        Map<String, SiteLink> someSite = new HashMap<>();
        someSite.put("enwiki", new SiteLink(SOME_WIKIPEDIA_URL));
        theEntity.put(SOME_WIKI_DATA_URL_ENTITY_ID, new WikidataEntity(someSite));
        WikidataResponse mockResponse = new WikidataResponse(theEntity);

        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockResponse))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        WikipediaLink wikipediaLink = wikidataService.getWikipediaLink(SOME_WIKI_DATA_URL_ENTITY_ID);

        assertThat(wikipediaLink, is(new WikipediaLink(SOME_WIKIPEDIA_URL)));
    }
}