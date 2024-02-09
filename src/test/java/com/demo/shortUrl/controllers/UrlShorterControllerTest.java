package com.demo.shortUrl.controllers;

import com.demo.shortUrl.dtos.UrlDecodeDto;
import com.demo.shortUrl.dtos.UrlEncodeDto;
import com.demo.shortUrl.services.UrlEncoderDecoderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UrlShorterController.class)
public class UrlShorterControllerTest {

    private static final String ENCODED_URL = "encoded";
    private static final String DECODED_URL = "decoded";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlEncoderDecoderService urlEncoderDecoderService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        if (Objects.isNull(this.objectMapper))
            this.objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldReturnEncodedUrl() throws Exception {
        UrlDecodeDto urlDecodeDto = new UrlDecodeDto(DECODED_URL);
        when(this.urlEncoderDecoderService.encode(urlDecodeDto)).thenReturn(new UrlEncodeDto(ENCODED_URL));
        mockMvc.perform(post("/urlshortner/encode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(urlDecodeDto))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.encodedUrl").value(ENCODED_URL));
    }


    @Test
    public void shouldReturnDecodedUrl() throws Exception {
        UrlEncodeDto urlEncodeDto = new UrlEncodeDto(ENCODED_URL);
        when(this.urlEncoderDecoderService.decode(urlEncodeDto)).thenReturn(Optional.of(new UrlDecodeDto(DECODED_URL)));
        mockMvc.perform(get("/urlshortner/decode").param("urlEncoded", ENCODED_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.decodedUrl").value(DECODED_URL));
    }

    @Test
    public void shouldReturnNotFoundWhenUrlIsNoTEncoded() throws Exception {
        UrlEncodeDto urlEncodeDto = new UrlEncodeDto(ENCODED_URL);
        when(this.urlEncoderDecoderService.decode(urlEncodeDto)).thenReturn(Optional.empty());
        mockMvc.perform(get("/urlshortner/decode").param("urlEncoded", ENCODED_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
