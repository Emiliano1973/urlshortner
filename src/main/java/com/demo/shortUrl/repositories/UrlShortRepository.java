package com.demo.shortUrl.repositories;

import com.demo.shortUrl.entities.UrlShort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlShortRepository extends JpaRepository<UrlShort, Long> {

    int countByOriginalUrl(String originalUrl);

    int countByGeneratedUrl(String generatedUrl);

    UrlShort findByOriginalUrl(String originalUrl);

    UrlShort findByGeneratedUrl(String generatedUrl);

}
