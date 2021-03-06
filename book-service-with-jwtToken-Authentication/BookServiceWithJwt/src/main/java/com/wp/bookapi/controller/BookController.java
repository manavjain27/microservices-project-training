package com.wp.bookapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.wp.bookapi.model.AuthenticationRequest;
import com.wp.bookapi.model.AuthenticationResponse;
import com.wp.bookapi.model.Book;
import com.wp.bookapi.model.JwtUtil;
import com.wp.bookapi.model.MyUserDetailsService;
import com.wp.bookapi.service.BookServiceInterface;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class BookController {
	
	@Autowired
	BookServiceInterface bookServiceInterface;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtToken;
	
	
	@GetMapping("/book")
	public List<Book> getAllBooks() {
	    return bookServiceInterface.findAll();
	}
	
	@PostMapping("/book")
	public Book createBook(@RequestBody Book book) {
	    return bookServiceInterface.addBook(book);
	}
	
	@GetMapping("/book/{id}")
	public Book getBookById(@PathVariable(value = "id") Long bookId) {
	    return bookServiceInterface.findByBookId(bookId).orElseThrow(() -> new BookNotFoundException("Book", "id", bookId));
	}
	
	@PutMapping("/book/{id}")
	public Book updateBook(@PathVariable(value = "id") Long bookId,
	                                        @RequestBody Book bookDetails) {

		bookServiceInterface.findByBookId(bookId).orElseThrow(() -> new BookNotFoundException("Book", "id", bookId));
	    
	    bookDetails.setBook_id(bookId);
	    Book updateBook = bookServiceInterface.addBook(bookDetails);
	    return updateBook;
	}
	
	@DeleteMapping("/book/{id}")
	public ResponseEntity<?> deleteBook(@PathVariable(value = "id") Long bookId) {
	    Book book = bookServiceInterface.findByBookId(bookId).orElseThrow(() -> new BookNotFoundException("Book", "id", bookId));

	    bookServiceInterface.deleteBook(book);
	    return ResponseEntity.ok().build();
	}
	
	@PutMapping("/book/updateAvailaibility/{id}/{incrementalCount}")
	public Book updateBookAvailability(@PathVariable(value = "id") Long bookId,
			@PathVariable(value = "incrementalCount") Long count) {

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
	
	@PostMapping("/token")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {

		// authenticating user credentials
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		// if user has been successfully authenticated, generate token.

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtToken.generateToken(userDetails);
		
		
		// send token in response.
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	

}
