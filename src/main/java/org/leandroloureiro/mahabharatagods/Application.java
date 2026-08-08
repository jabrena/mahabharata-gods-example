package org.leandroloureiro.mahabharatagods;

import org.leandroloureiro.mahabharatagods.services.IndianGodService;
import org.leandroloureiro.mahabharatagods.services.IndianGodsService;
import org.leandroloureiro.mahabharatagods.services.MahabharataDataSourceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${spring.cloud.discovery.client.simple.instances.indian-god-service[0].uri}")
    private String indianGodServiceUrl;

    @Value("${spring.cloud.discovery.client.simple.instances.indian-gods-service[0].uri}")
    private String indianGodsServiceUrl;

    @Value("${spring.cloud.discovery.client.simple.instances.mahabharata-data-source-service[0].uri}")
    private String mahabharataDataSourceServiceUrl;

    @Bean
    public ExchangeStrategies defaultExchangeStrategies() {

        var size = 16 * 1024 * 1024;

        return ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();
    }

    @Bean
    public IndianGodService indianGodService(ExchangeStrategies defaultExchangeStrategies) {

        WebClient client = WebClient.builder()
                .baseUrl(indianGodServiceUrl)
                .exchangeStrategies(defaultExchangeStrategies)
                .build();

        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();

        return proxyFactory.createClient(IndianGodService.class);
    }

    @Bean
    public IndianGodsService indianGodsService() {

        WebClient client = WebClient.builder()
                .baseUrl(indianGodsServiceUrl)
                .build();

        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();

        return proxyFactory.createClient(IndianGodsService.class);
    }

    @Bean
    public MahabharataDataSourceService mahabharataDataSourceService(ExchangeStrategies defaultExchangeStrategies) {

        WebClient client = WebClient.builder()
                .baseUrl(mahabharataDataSourceServiceUrl)
                .exchangeStrategies(defaultExchangeStrategies)
                .build();

        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();

        return proxyFactory.createClient(MahabharataDataSourceService.class);
    }

}
