package com.demo.shortUrl.services.impl;

import com.demo.shortUrl.dtos.UrlDecodeDto;
import com.demo.shortUrl.dtos.UrlEncodeDto;
import com.demo.shortUrl.entities.UrlShort;
import com.demo.shortUrl.repositories.UrlShortRepository;
import com.demo.shortUrl.services.UrlEncoderDecoderService;
import com.demo.shortUrl.services.UrlShortEncoderService;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlEncoderDecoderServiceImpl implements UrlEncoderDecoderService {
    private static final String HTTP_PROT = "http://";
    private static final String HTTPS_PROT = "https://";

    private final UrlShortRepository urlShortRepository;
    private final UrlShortEncoderService urlShortEncoderService;

    public UrlEncoderDecoderServiceImpl(final UrlShortRepository urlShortRepository, final UrlShortEncoderService urlShortEncoderService) {
        this.urlShortRepository = urlShortRepository;
        this.urlShortEncoderService = urlShortEncoderService;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Cacheable(value = "encode_cache", key = "#urlDecodeDto.decodedUrl")
    public UrlEncodeDto encode(final UrlDecodeDto urlDecodeDto) {
        String originalUrl = sanitizeURL(urlDecodeDto.getDecodedUrl());
        if (this.urlShortRepository.countByOriginalUrl(originalUrl) > 0) {
            return getEncodeBeanByOriginalUrl(originalUrl);
        }
        String protocol = getProtocol(urlDecodeDto.getDecodedUrl());
        String encodedUrl = this.urlShortEncoderService.encodeUrl(originalUrl);
        this.urlShortRepository.save(new UrlShort(originalUrl, encodedUrl, protocol));
        return new UrlEncodeDto(encodedUrl, protocol);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    @Cacheable(value = "decode_cache", key = "#urlEncodeDto.encodedUrl", unless = "#result==null")
    public Optional<UrlDecodeDto> decode(final UrlEncodeDto urlEncodeDto) {
        if (this.urlShortRepository.countByGeneratedUrl(urlEncodeDto.getEncodedUrl()) == 0) {
            return Optional.empty();
        }
        UrlShort urlShort = this.urlShortRepository.findByGeneratedUrl(urlEncodeDto.getEncodedUrl());
        return Optional.of(new UrlDecodeDto(urlShort.getOriginalUrl(), urlShort.getProtocol()));
    }

    private UrlEncodeDto getEncodeBeanByOriginalUrl(final String originalUrl) {
        UrlShort urlShort = this.urlShortRepository.findByOriginalUrl(originalUrl);
        return new UrlEncodeDto(urlShort.getGeneratedUrl(), urlShort.getProtocol());
    }

    private String getProtocol(final String url) {
        if (url.startsWith(HTTP_PROT)) {
            return HTTP_PROT;
        }
        if (url.startsWith(HTTPS_PROT)) {
            return HTTPS_PROT;
        }
        return HTTP_PROT;
    }
    private String sanitizeURL(String url) {
        if (url.startsWith(HTTP_PROT))
            url = url.substring(7);
        if (url.startsWith(HTTPS_PROT))
            url = url.substring(8);
        if (url.charAt(url.length() - 1) == '/')
            url = url.substring(0, url.length() - 1);
        return url;
    }


}
