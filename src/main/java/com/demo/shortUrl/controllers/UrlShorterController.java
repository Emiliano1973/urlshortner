package com.demo.shortUrl.controllers;


import com.demo.shortUrl.dtos.UrlDecodeDto;
import com.demo.shortUrl.dtos.UrlEncodeDto;
import com.demo.shortUrl.services.UrlEncoderDecoderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/urlshortner")
public class UrlShorterController {

    private final UrlEncoderDecoderService urlEncoderDecoderService;

    public UrlShorterController(final UrlEncoderDecoderService urlEncoderDecoderService) {
        this.urlEncoderDecoderService = urlEncoderDecoderService;
    }

    @PostMapping(value = "/encode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UrlEncodeDto encode(@RequestBody UrlDecodeDto urlDecodeDto) {
        return this.urlEncoderDecoderService.encode(urlDecodeDto);
    }

    @GetMapping(value = "/decode", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> decode(@RequestParam("urlEncoded") String urlEncoded) {
        Optional<UrlDecodeDto> urlDecodeDtoOpt = this.urlEncoderDecoderService.decode(new UrlEncodeDto(urlEncoded));
        return ResponseEntity.of(urlDecodeDtoOpt);
    }
}
