package com.demo.shortUrl.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UrlEncodeDto {
    private String encodedUrl;
    private String protocol;

    public UrlEncodeDto(String encodedUrl, String protocol) {
        this.encodedUrl = encodedUrl;
        this.protocol = protocol;
    }

    public UrlEncodeDto(String encodedUrl) {
        this(encodedUrl, null);
    }
}
