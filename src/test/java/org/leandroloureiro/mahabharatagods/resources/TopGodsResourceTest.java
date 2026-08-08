package org.leandroloureiro.mahabharatagods.resources;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leandroloureiro.mahabharatagods.logic.TopMahabharataGods;
import org.leandroloureiro.mahabharatagods.model.God;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopGodsResourceTest {

    @Mock
    private TopMahabharataGods topMahabharataGods;

    @InjectMocks
    private TopGodsResource resource;

    @Test
    void testTopMahabharataGods() {

        var gods = Collections.singletonList(new God("SomeGod", 10));

        when(topMahabharataGods.getTopMahabharataGods()).thenReturn(gods);

        var response = resource.topGods().join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gods, response.getBody());

        verify(topMahabharataGods).getTopMahabharataGods();

        verifyNoMoreInteractions(topMahabharataGods);

    }

}