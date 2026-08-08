package org.leandroloureiro.mahabharatagods.services;

import org.springframework.web.service.annotation.GetExchange;

import java.util.concurrent.CompletableFuture;

/**
 * Interface to get Mahabharata book
 */
public interface MahabharataDataSourceService {

    /**
     * Get Mahabharata book
     *
     * @return the content of Mahabharata book
     */
    @GetExchange("/stream/TheMahabharataOfKrishna-dwaipayanaVyasa/MahabharataOfVyasa-EnglishTranslationByKMGanguli_djvu.txt")
    CompletableFuture<String> getMahabharataBook();

}
