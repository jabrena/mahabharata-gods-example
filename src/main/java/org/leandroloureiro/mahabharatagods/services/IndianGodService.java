package org.leandroloureiro.mahabharatagods.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.concurrent.CompletableFuture;

/**
 * Logic to check the validity of an Indian God
 */
public interface IndianGodService {

    /**
     * Check if Indian god is valid
     *
     * @param indianGod the name of the god
     * @return True if god is valid and false is god is not existent
     */
    @GetExchange("/wiki/{indianGod}")
    CompletableFuture<ResponseEntity<String>> isValidIndianGod(@PathVariable("indianGod") String indianGod);

}
