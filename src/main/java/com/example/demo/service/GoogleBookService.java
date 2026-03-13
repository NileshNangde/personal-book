package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.example.demo.dto.GoogleBook;

@Service
public class GoogleBookService {
    private final RestClient restClient;

    public GoogleBookService(@Value("${google.books.base-url:https://www.googleapis.com/books/v1}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public GoogleBook searchBooks(String query, Integer maxResults, Integer startIndex) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/volumes")
                        .queryParam("q", query)
                        .queryParam("maxResults", maxResults != null ? maxResults : 10)
                        .queryParam("startIndex", startIndex != null ? startIndex : 0)
                        .build())
                .retrieve()
                .body(GoogleBook.class);
    }
    /**
     * Fetch a single book by Id and wraps it in a GoogleBook object for the controller 
     */
    public GoogleBook getBookById(String googleId){
    	try {
    		GoogleBook.Item item = restClient.get()
        			.uri("/volumes/{id}", googleId)
        			.retrieve()
        			.body(GoogleBook.Item.class);
    		
    		if(item == null) return null;
        	return new GoogleBook("book#volume" , 1 , List.of(item));
		} catch (RestClientResponseException ex) {
			//if upstream returns 404 , treat as missing and return null 
			try {
				if(ex.getStatusCode() == HttpStatus.NOT_FOUND) {
				return null;	
				}
			} catch (Exception ignore) {
				//if status cant be read fall through and rethrow
			}
			throw ex;
		}
    }
}

