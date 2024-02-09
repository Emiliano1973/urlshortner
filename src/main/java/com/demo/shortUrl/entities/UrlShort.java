package com.demo.shortUrl.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "URL_SHORT")
@NoArgsConstructor
@Getter
@Setter
public class UrlShort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "ORIGINAL_URL", unique = true)
    private String originalUrl;

    @Column(name = "GENERATED_URL", unique = true)
    private String generatedUrl;

    @Column(name = "PROTOCOL")
    private String protocol;

    public UrlShort(final String originalUrl, final String generatedUrl, final String protocol) {
        this.originalUrl = originalUrl;
        this.generatedUrl = generatedUrl;
        this.protocol = protocol;
    }
}
