package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Subscription;

public interface SubscriptionServiceInterface {

	public Optional<Subscription> findByName(String name);
	public List<Subscription> findAll();
	public Subscription addSubscription(Subscription subscription);
	public void deleteSubscription(Subscription subscription);
	
}
