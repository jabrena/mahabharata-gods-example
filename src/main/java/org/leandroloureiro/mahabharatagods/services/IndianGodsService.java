package org.leandroloureiro.mahabharatagods.services;

import org.springframework.web.service.annotation.GetExchange;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * API to get Available Indian Gods
 */
public interface IndianGodsService {

    /**
     * Get a list of Indian gods.
     *
     * @return a list with Indian gods.
     */
    @GetExchange("/jabrena/latency-problems/indian")
    CompletableFuture<List<String>> getGodList();

}
