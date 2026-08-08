package org.leandroloureiro.mahabharatagods.logic;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.BDDAssertions.then;


@SpringBootTest
@ActiveProfiles("test")
class TopMahabharataGodsImplTest {

    @RegisterExtension
    static final WireMockExtension wireMockExtension1 = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void downstreamServiceUris(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.discovery.client.simple.instances.indian-god-service[0].uri", wireMockExtension1::baseUrl);
        registry.add("spring.cloud.discovery.client.simple.instances.indian-gods-service[0].uri", wireMockExtension1::baseUrl);
        registry.add("spring.cloud.discovery.client.simple.instances.mahabharata-data-source-service[0].uri", wireMockExtension1::baseUrl);
    }

    @Autowired
    private TopMahabharataGodsImpl component;

    @Test
    void testGetTopGods() {

        wireMockExtension1.stubFor(get(urlEqualTo("/jabrena/latency-problems/indian"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBodyFile("indian.json")));


        wireMockExtension1.stubFor(get(urlEqualTo("/wiki/Brahma"))
                .willReturn(aResponse().withHeader("Content-Type", "text/html")
                        .withStatus(200)));

        wireMockExtension1.stubFor(get(urlEqualTo("/wiki/Rama"))
                .willReturn(aResponse().withHeader("Content-Type", "text/html")
                        .withStatus(200)));

        wireMockExtension1.stubFor(get(urlEqualTo("/wiki/Hanuman"))
                .willReturn(aResponse().withHeader("Content-Type", "text/html")
                        .withStatus(200)));

        wireMockExtension1.stubFor(get(urlEqualTo("/wiki/Lakshmi"))
                .willReturn(aResponse().withHeader("Content-Type", "text/html")
                        .withStatus(200)));

        wireMockExtension1.stubFor(get(urlEqualTo("/wiki/Shiva"))
                .willReturn(aResponse().withHeader("Content-Type", "text/html")
                        .withStatus(200)));

        wireMockExtension1.stubFor(get(urlEqualTo("/stream/TheMahabharataOfKrishna-dwaipayanaVyasa/MahabharataOfVyasa-EnglishTranslationByKMGanguli_djvu.txt"))
                .willReturn(aResponse().withHeader("Content-Type", "text/plain")
                        .withStatus(200)
                        .withBodyFile("MahabharataOfVyasa-EnglishTranslationByKMGanguli_djvu.txt")));

        then(component).isNotNull();

        var gods = component.getTopMahabharataGods();

        then(gods).isNotNull();
        then(gods.size()).isEqualTo(3);

        then(gods.get(0).name()).isEqualTo("Brahma");
        then(gods.get(0).hitCount()).isEqualTo(8100);

        then(gods.get(1).name()).isEqualTo("Rama");
        then(gods.get(1).hitCount()).isEqualTo(845);

        then(gods.get(2).name()).isEqualTo("Hanuman");
        then(gods.get(2).hitCount()).isEqualTo(54);

    }

}