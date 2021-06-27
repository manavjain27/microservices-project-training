package com.wp.bookapi.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "Book")
public class Book implements Serializable{

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long book_id;
   private String book_name;
   private String author;
   private Long available_copies;
   private Long total_copies;
public Long getBook_id() {
	return book_id;
}
public void setBook_id(Long book_id) {
	this.book_id = book_id;
}
public String getBook_name() {
	return book_name;
}
public void setBook_name(String book_name) {
	this.book_name = book_name;
}
public String getAuthor() {
	return author;
}
public void setAuthor(String author) {
	this.author = author;
}
public Long getAvailable_copies() {
	return available_copies;
}
public void setAvailable_copies(Long available_copies) {
	this.available_copies = available_copies;
}
public Long getTotal_copies() {
	return total_copies;
}
public void setTotal_copies(Long total_copies) {
	this.total_copies = total_copies;
}

   
}
