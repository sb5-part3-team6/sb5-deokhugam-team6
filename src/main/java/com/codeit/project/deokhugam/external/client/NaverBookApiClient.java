package com.codeit.project.deokhugam.external.client;

import com.codeit.project.deokhugam.domain.book.dto.response.BookResponse;
import com.codeit.project.deokhugam.external.client.dto.NaverBookRss;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverBookApiClient {

    @Value("${external.naver.api.client-id}")
    private String clientId;

    @Value("${external.naver.api.client-secret}")
    private String clientSecret;

    private final @Qualifier("naverWebClient") WebClient naverWebClient;

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
                        .title(item.getTitle())
                        .author(item.getAuthor())
                        .description(item.getDescription())
                        .publisher(item.getPublisher())
                        .publishedDate(LocalDate.parse(item.getPubdate(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .isbn(item.getIsbn())
                        .thumbnailImage(item.getImage())
                        .build()
                ).findFirst().get();
    }

    private NaverBookRss parseXml(String xml) {
        try {
            JAXBContext context = JAXBContext.newInstance(NaverBookRss.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (NaverBookRss) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            throw new RuntimeException("XML 파싱 실패", e);
        }
    }
}
