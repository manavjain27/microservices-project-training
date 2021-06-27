package com.wp.bookapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wp.bookapi.model.Book;
import com.wp.bookapi.repository.BookRepository;
import com.wp.bookapi.service.BookServiceInterface;

@Service
public class BookServiceImpl implements BookServiceInterface{

	@Autowired
	BookRepository bookRepository;
	
	@Override
	public List<Book> findAll() {
		 return bookRepository.findAll();
	}

	@Override
	public Book addBook(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public void deleteBook(Book book) {
		bookRepository.delete(book);
	}

	@Override
	public Optional<Book> findByBookId(Long id) {
		return bookRepository.findById(id);
	}

}
