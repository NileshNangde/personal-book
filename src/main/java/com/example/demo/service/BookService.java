package com.example.demo.service;

import java.lang.System.Logger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.constant.AppConstant;
import com.example.demo.dto.GoogleBook;
import com.example.demo.entity.Book;
import com.example.demo.properties.AppProperties;
import com.example.demo.repository.BookRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(BookService.class);
	private final BookRepository bookRepository;
	private final GoogleBookService googleBookService;
	private final AppProperties appProp;
	
	public BookService(BookRepository bookRepository , GoogleBookService googleBookService , AppProperties appProp) {
		this.bookRepository = bookRepository;
		this.googleBookService = googleBookService;
		this.appProp = appProp;
	}
	
	@Transactional
	public Book addBookByGoogleId(String googleId) {
		Map<String ,String> messages = appProp.getMessages();
		
		Optional<Book> existing = bookRepository.findById(googleId);
		if(existing.isPresent()) {
			log.warn("Attempt to add dublicate book with googleId={}" , googleId);
			throw new IllegalStateException(messages.get(AppConstant.BOOK_ALLREADY_EXISTS));
		}
		
		GoogleBook googleData = googleBookService.getBookById(googleId);
		if(googleData == null || googleData.items() == null || googleData.items().isEmpty()) {
			throw new IllegalStateException(messages.get(AppConstant.BOOK_NOT_FOUND));
		}
		
		GoogleBook.Item item = googleData.items().get(0);
		GoogleBook.VolumeInfo info = item.volumeInfo();
		
		String author = (info.authors() != null && !info.authors().isEmpty()) ? info.authors().get(0) :"Unkown Author";
		int pages = (info.pageCount() == null) ? 0 : info.pageCount();
		
		Book bookToSave = new Book();
		bookToSave.setId(item.id());
		bookToSave.setTitle(info.title());
		bookToSave.setAuthor(author);
		bookToSave.setPageCount(pages);
		
		Book saved = bookRepository.save(bookToSave);
		log.info("saved book {} ({}) to repository" , saved.getTitle(), saved.getAuthor());
		return saved;
		
	}
	
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public List<Book> getAllBooks(){
		log.debug("fetching all books");
		return bookRepository.findAll();
	}
}
