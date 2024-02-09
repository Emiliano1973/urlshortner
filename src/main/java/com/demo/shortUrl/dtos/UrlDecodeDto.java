package com.demo.shortUrl.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UrlDecodeDto {

    private String decodedUrl;
    private String protocol;

    public UrlDecodeDto(String decodedUrl, String protocol) {
        this.decodedUrl = decodedUrl;
        this.protocol = protocol;
    }

    public UrlDecodeDto(String decodedUrl) {
        this(decodedUrl, null);
    }
}
