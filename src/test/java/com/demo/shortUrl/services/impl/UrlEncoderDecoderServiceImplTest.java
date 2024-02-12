package com.demo.shortUrl.services.impl;

import com.demo.shortUrl.dtos.UrlDecodeDto;
import com.demo.shortUrl.dtos.UrlEncodeDto;
import com.demo.shortUrl.entities.UrlShort;
import com.demo.shortUrl.repositories.UrlShortRepository;
import com.demo.shortUrl.services.UrlShortEncoderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UrlEncoderDecoderServiceImplTest {

    private static final String ORIGINAL_URL_1="originalUrl1";
    private static final String ENCODED_URL_1="encodedUrl1";

    private static final String ORIGINAL_URL_2="originalUrl2";
    private static final String ENCODED_URL_2="encodedUrl2";


    private static final String PROT_STR="http://";

    @Autowired
    private UrlEncoderDecoderServiceImpl urlEncoderDecoderService;

    @MockBean
    private UrlShortRepository urlShortRepository;

    @MockBean
    private  UrlShortEncoderService urlShortEncoderService;

    @MockBean
    private UrlDecodeDto urlDecodeDto;

    @MockBean
    private UrlEncodeDto urlEncodeDto;


    @MockBean
    private UrlShort urlShort;

    @Test
     void whenTheUrlIsNeededTobeEncodedIfItIsAlreadyStoredThenReturnTtFromDB() throws Exception{
        when(urlDecodeDto.getDecodedUrl()).thenReturn(ORIGINAL_URL_1);
        when(this.urlShortRepository.countByOriginalUrl(ORIGINAL_URL_1)).thenReturn(Integer.valueOf(1));
       when(this.urlShortRepository.findByOriginalUrl(ORIGINAL_URL_1)).thenReturn(this.urlShort);
        when(this.urlShort.getGeneratedUrl()).thenReturn(ENCODED_URL_1);
        when(this.urlShort.getProtocol()).thenReturn(PROT_STR);
        this.urlEncoderDecoderService.encode(urlDecodeDto);
        verify(this.urlShortEncoderService, never()).encodeUrl(ORIGINAL_URL_1);
    }
    @Test
    void whenTheUrlIsNeededTobeEncodedIfItIsNotAlreadyStoredThenReturnTtFromDB() throws Exception{
        when(this.urlDecodeDto.getDecodedUrl()).thenReturn(ORIGINAL_URL_2);
        when(this.urlShortRepository.countByOriginalUrl(ORIGINAL_URL_2)).thenReturn(Integer.valueOf(0));
        when(this.urlShortEncoderService.encodeUrl(ORIGINAL_URL_2)).thenReturn(ENCODED_URL_2);
        when(this.urlShort.getGeneratedUrl()).thenReturn(ENCODED_URL_2);
        when(this.urlShort.getProtocol()).thenReturn(PROT_STR);
        UrlEncodeDto urlEncodeDto= this.urlEncoderDecoderService.encode(urlDecodeDto);
        assertNotNull(urlEncodeDto);
        assertEquals( ENCODED_URL_2, urlEncodeDto.getEncodedUrl());
        verify(this.urlShortEncoderService, times(1)).encodeUrl(ORIGINAL_URL_2);
    }


    @Test
     void whenTheUrlIsNeededTobeDecodedButItsNotStoredThenItShouldReturnOptionalEmpty(){
        when(this.urlEncodeDto.getEncodedUrl()).thenReturn(ENCODED_URL_1);
      //when(this.urlEncodeDto.getProtocol()).thenReturn(PROT_STR);
      when(this.urlShortRepository.countByGeneratedUrl(ENCODED_URL_1)).thenReturn(Integer.valueOf(0));
        Optional<UrlDecodeDto> decodeDtoOptional=this.urlEncoderDecoderService.decode(urlEncodeDto);
        assertTrue(decodeDtoOptional.isEmpty());
      verify(this.urlShortRepository, never()).findByGeneratedUrl(ENCODED_URL_1);

    }

    @Test
    void whenTheUrlIsNeededTobeDecodedAndtStoredThenItShouldReturnOptionalFull(){
        when(this.urlShortRepository.findByGeneratedUrl(ENCODED_URL_2)).thenReturn(this.urlShort);
        when(this.urlEncodeDto.getEncodedUrl()).thenReturn(ENCODED_URL_2);
        when(this.urlEncodeDto.getProtocol()).thenReturn(PROT_STR);
        when(this.urlShort.getOriginalUrl()).thenReturn(ORIGINAL_URL_2);
        when(this.urlShortRepository.countByGeneratedUrl(ENCODED_URL_2)).thenReturn(Integer.valueOf(1));
        Optional<UrlDecodeDto> decodeDtoOptional=this.urlEncoderDecoderService.decode(urlEncodeDto);
        assertFalse(decodeDtoOptional.isEmpty());
        assertEquals(ORIGINAL_URL_2, decodeDtoOptional.get().getDecodedUrl() );
    }
}
