package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.response.AppResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(BookNotFoundException.class)
	public ResponseEntity<AppResponse<Object>> handleNotFound(BookNotFoundException ex){
		log.warn("Book not Found : {}" , ex.getMessage());
		AppResponse<Object> resp = new AppResponse<>();
		resp.setStatusCode(HttpStatus.NOT_FOUND.value());
		resp.setMessage(ex.getMessage());
		resp.setData(null);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
	}
	
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<AppResponse<Object>> handleBadRequest(IllegalStateException ex){
		log.warn("Bad request : {}" , ex.getMessage());
		AppResponse<Object> resp = new AppResponse<>();
		resp.setStatusCode(HttpStatus.BAD_REQUEST.value());
		resp.setMessage(ex.getMessage());
		resp.setData(null);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<AppResponse<Object>> handleGeneric(Exception ex){
		log.warn("Unexpected error : {}" , ex.getMessage());
		AppResponse<Object> resp = new AppResponse<>();
		resp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		resp.setMessage(ex.getMessage());
		resp.setData(null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
	}

}
