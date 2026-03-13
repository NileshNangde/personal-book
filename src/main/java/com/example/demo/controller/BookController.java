package com.example.demo.controller;

import java.util.List;

import com.example.demo.constant.AppConstant;
import com.example.demo.dto.GoogleBook;
import com.example.demo.entity.Book;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.properties.AppProperties;
import com.example.demo.repository.BookRepository;
import com.example.demo.response.AppResponse;
import com.example.demo.service.BookService;
import com.example.demo.service.GoogleBookService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;


import java.util.Map;
import java.util.Optional;

@RestController
public class BookController {
    private final BookService bookService;
    private final GoogleBookService googleBookService;
    private final AppProperties appProp;

    @Autowired
    public BookController(GoogleBookService googleBookService ,BookService bookService , AppProperties appProp) {
        this.googleBookService = googleBookService;
        this.bookService = bookService;
        this.appProp = appProp;
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/google")
    public GoogleBook searchGoogleBooks(@RequestParam("q") String query,
                                        @RequestParam(value = "maxResults", required = false) Integer maxResults,
                                        @RequestParam(value = "startIndex", required = false) Integer startIndex) {
        return googleBookService.searchBooks(query, maxResults, startIndex);
    }
   
   @PostMapping("/books/{googleId}")
    public ResponseEntity<AppResponse<Book>> addBookByGoogleId(@PathVariable String googleId){
    	AppResponse<Book> response = new AppResponse<>();
    	
    	Book saveBook = bookService.addBookByGoogleId(googleId);
    	
    	response.setStatusCode(HttpStatus.CREATED.value());
    	response.setMessage(appProp.getMessages().get(AppConstant.BOOK_SAVED_SUCCESS));
    	response.setData(saveBook);
    	
    	return ResponseEntity.status(HttpStatus.CREATED).body(response);
    	
    	
    }
}
