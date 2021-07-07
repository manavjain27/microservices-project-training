package com.wp.bookapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wp.bookapi.exception.BookCantBeUpdatedException;
import com.wp.bookapi.exception.BookNotFoundException;
import com.wp.bookapi.model.Book;
import com.wp.bookapi.service.BookServiceInterface;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class BookController {
	
	private static final Logger LOG = LoggerFactory.getLogger(BookController.class);
	
	@Autowired
	BookServiceInterface bookServiceInterface;
	
	
	@GetMapping("/book")
	public List<Book> getAllBooks() {
	    LOG.info("Books are Retrieved");
	    return bookServiceInterface.findAll();
	}
	
	@PostMapping("/book")
	public Book createBook(@RequestBody Book book) {
            LOG.info("Books is Added");
	    return bookServiceInterface.addBook(book);
	}
	
	@GetMapping("/book/{id}")
	public Book getBookById(@PathVariable(value = "id") Long bookId) {
	    LOG.info("Book is retreived with a particular id");
	    return bookServiceInterface.findByBookId(bookId).orElseThrow(() -> new BookNotFoundException("Book", "id", bookId));
	}
	
	@PutMapping("/book/{id}")
	public Book updateBook(@PathVariable(value = "id") Long bookId,
	                                        @RequestBody Book bookDetails) {

		LOG.info("Book is Updated");
		bookServiceInterface.findByBookId(bookId).orElseThrow(() -> new BookNotFoundException("Book", "id", bookId));
	    
	    bookDetails.setBook_id(bookId);
	    Book updateBook = bookServiceInterface.addBook(bookDetails);
	    return updateBook;
	}
	
	@DeleteMapping("/book/{id}")
	public ResponseEntity<?> deleteBook(@PathVariable(value = "id") Long bookId) {
	    LOG.info("Book is deleted");
	    Book book = bookServiceInterface.findByBookId(bookId).orElseThrow(() -> new BookNotFoundException("Book", "id", bookId));

	    bookServiceInterface.deleteBook(book);
	    return ResponseEntity.ok().build();
	}
	
	@PutMapping("/book/updateAvailaibility/{id}/{incrementalCount}")
	public Book updateBookAvailability(@PathVariable(value = "id") Long bookId,
			@PathVariable(value = "incrementalCount") Long count) {

	    LOG.info("Books availability is updated");
	    Book bookDetails = bookServiceInterface.findByBookId(bookId).orElseThrow(() -> new BookNotFoundException("Book", "id", bookId));
	    if(bookDetails.getAvailable_copies() + count <= bookDetails.getTotal_copies()) {
	       bookDetails.setAvailable_copies(bookDetails.getAvailable_copies() + count);
	    }
	    else {
	    	throw new BookCantBeUpdatedException("Book", "id", bookId);
	    }
	    
	    Book updateBook = bookServiceInterface.addBook(bookDetails);
	    return updateBook;
	}
	

}
