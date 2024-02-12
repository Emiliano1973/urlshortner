package com.demo.shortUrl.services.impl;

import com.demo.shortUrl.services.UrlShortEncoderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UrlShortEncoderServiceImplTest {

    private static final String URL_DECODED="URI_DECODED";

    @Autowired
    private UrlShortEncoderService urlShortEncoderService;

    @Test
     void urlInInputShouldBeConvertedInAShortUrl() throws Exception{
        String encoded=this.urlShortEncoderService.encodeUrl(URL_DECODED);
        assertNotNull(encoded);
        assertEquals(8, encoded.length());


    }

}
