package com.demo.shortUrl.services;

import com.demo.shortUrl.dtos.UrlDecodeDto;
import com.demo.shortUrl.dtos.UrlEncodeDto;

import java.util.Optional;

public interface UrlEncoderDecoderService {

    UrlEncodeDto encode(UrlDecodeDto urlDecodeDto);

    Optional<UrlDecodeDto> decode(UrlEncodeDto urlEncodeDto);

}
