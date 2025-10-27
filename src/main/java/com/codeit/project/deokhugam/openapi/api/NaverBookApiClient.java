package com.codeit.project.deokhugam.openapi.api;

import com.codeit.project.deokhugam.domain.book.dto.BookDto;
import com.codeit.project.deokhugam.domain.book.dto.BookResponse;
import com.codeit.project.deokhugam.openapi.dto.NaverBookRss;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverBookApiClient {

    @Value("${external.naver.api.client-id}")
    private String clientId;

    @Value("${external.naver.api.client-secret}")
    private String clientSecret;

    private final @Qualifier("naverWebClient") WebClient naverWebClient;
    private final XmlMapper xmlMapper;

    public BookResponse fetchBooks(String isbn) {
        String rss = naverWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search/book_adv.xml")
                        .queryParam("d_isbn", isbn)
                        .build())
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        NaverBookRss response = parseXml(rss);

        return response.getChannel().getItems().stream()
                .map(item -> BookResponse.builder()
                        .title(item.title())
                        .author(item.author())
                        .description(item.description())
                        .publisher(item.publisher())
                        .publishedDate(LocalDate.parse(item.pubdate(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .isbn(item.isbn())
                        .thumbnailImage(item.image())
                        .build()
                ).findFirst().get();
    }

    private NaverBookRss parseXml(String xml) {
        try {
            xmlMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return xmlMapper.readValue(xml, NaverBookRss.class);
        } catch (Exception e) {
            log.error("XML 파싱 실패: {}", xml, e);
            throw new RuntimeException("XML 파싱 실패", e);
        }
    }
}
