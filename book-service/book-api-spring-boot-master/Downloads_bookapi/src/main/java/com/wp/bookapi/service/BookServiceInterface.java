package com.wp.bookapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wp.bookapi.model.Book;

public interface BookServiceInterface {

	public Optional<Book> findByBookId(Long id);
	public List<Book> findAll();
	public Book addBook(Book book);
	public void deleteBook(Book book);
	
}
