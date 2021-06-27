package com.example.demo.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "Subscription")
public class Subscription {

	@Id
	private String subscription_name;
	private Date date_subscribed;
	private Date date_returned;
	private Long book_id;
	
	public String getSubscription_name() {
		return subscription_name;
	}
	public void setSubscription_name(String subscription_name) {
		this.subscription_name = subscription_name;
	}
	public Date getDate_subscribed() {
		return date_subscribed;
	}
	public void setDate_subscribed(Date date_subscribed) {
		this.date_subscribed = date_subscribed;
	}
	public Date getDate_returned() {
		return date_returned;
	}
	public void setDate_returned(Date date_returned) {
		this.date_returned = date_returned;
	}
	public Long getBook_id() {
		return book_id;
	}
	public void setBook_id(Long book_id) {
		this.book_id = book_id;
	}
	
	
	
	
}
