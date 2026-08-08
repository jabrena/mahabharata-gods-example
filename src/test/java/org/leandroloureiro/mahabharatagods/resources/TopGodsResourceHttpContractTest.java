package org.leandroloureiro.mahabharatagods.resources;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * HTTP-level compatibility oracle for {@code GET /top-gods}: pins the exact status, content type,
 * and ordered JSON contract so the platform migration can be verified against a fixed behavior baseline.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class TopGodsResourceHttpContractTest {

    @RegisterExtension
    static final WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Autowired
    private WebTestClient webTestClient;

    @DynamicPropertySource
    static void downstreamServiceUris(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.discovery.client.simple.instances.indian-god-service[0].uri", wireMockExtension::baseUrl);
        registry.add("spring.cloud.discovery.client.simple.instances.indian-gods-service[0].uri", wireMockExtension::baseUrl);
        registry.add("spring.cloud.discovery.client.simple.instances.mahabharata-data-source-service[0].uri", wireMockExtension::baseUrl);
    }

    @Test
    void topGodsReturnsExactRankedJsonContract() {

        wireMockExtension.stubFor(get(urlEqualTo("/jabrena/latency-problems/indian"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile("indian.json")));

        wireMockExtension.stubFor(get(urlEqualTo("/wiki/Brahma"))
                .willReturn(aResponse().withHeader("Content-Type", "text/html").withStatus(200)));
        wireMockExtension.stubFor(get(urlEqualTo("/wiki/Rama"))
                .willReturn(aResponse().withHeader("Content-Type", "text/html").withStatus(200)));
        wireMockExtension.stubFor(get(urlEqualTo("/wiki/Hanuman"))
                .willReturn(aResponse().withHeader("Content-Type", "text/html").withStatus(200)));
        wireMockExtension.stubFor(get(urlEqualTo("/wiki/Lakshmi"))
                .willReturn(aResponse().withHeader("Content-Type", "text/html").withStatus(200)));
        wireMockExtension.stubFor(get(urlEqualTo("/wiki/Shiva"))
                .willReturn(aResponse().withHeader("Content-Type", "text/html").withStatus(200)));

        wireMockExtension.stubFor(get(urlEqualTo("/stream/TheMahabharataOfKrishna-dwaipayanaVyasa/MahabharataOfVyasa-EnglishTranslationByKMGanguli_djvu.txt"))
                .willReturn(aResponse().withHeader("Content-Type", "text/plain")
                        .withStatus(200)
                        .withBodyFile("MahabharataOfVyasa-EnglishTranslationByKMGanguli_djvu.txt")));

        webTestClient.get().uri("/top-gods")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(3)
                .jsonPath("$[0].name").isEqualTo("Brahma")
                .jsonPath("$[0].hitCount").isEqualTo(8100)
                .jsonPath("$[1].name").isEqualTo("Rama")
                .jsonPath("$[1].hitCount").isEqualTo(845)
                .jsonPath("$[2].name").isEqualTo("Hanuman")
                .jsonPath("$[2].hitCount").isEqualTo(54);
    }

}
