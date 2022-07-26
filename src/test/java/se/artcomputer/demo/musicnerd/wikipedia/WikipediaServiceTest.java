package se.artcomputer.demo.musicnerd.wikipedia;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import se.artcomputer.demo.musicnerd.wikidata.WikipediaLink;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class WikipediaServiceTest {
    private static final String SOME_HTML_STRING = "<p>The html</p>";
    private static final String SOME_WIKIPEDIA_LINK = "https://en.wikipedia.org/wiki/some_title";
    private MockWebServer mockBackEnd;
    private WikipediaService wikipediaService;

    @BeforeEach
    void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
        wikipediaService = new WikipediaService(baseUrl);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void getPageSummary() {
        mockBackEnd.enqueue(new MockResponse()
                .setBody("{\"extract_html\": \"" + SOME_HTML_STRING + "\"}")
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        PageSummary pageSummary = wikipediaService.getSummary(Mono.just(new WikipediaLink(SOME_WIKIPEDIA_LINK))).block();

        assertThat(pageSummary, notNullValue());
        assertThat(pageSummary.htmlString(), is(SOME_HTML_STRING));
    }
}