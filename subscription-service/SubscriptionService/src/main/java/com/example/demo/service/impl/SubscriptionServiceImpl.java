package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Subscription;
import com.example.demo.repository.SubscriptionRepository;
import com.example.demo.service.SubscriptionServiceInterface;

@Service
public class SubscriptionServiceImpl implements SubscriptionServiceInterface{
	
	@Autowired
	SubscriptionRepository subscriptionRepository;

	@Override
	public Optional<Subscription> findByName(String name) {
		return subscriptionRepository.findById(name);
	}

	@Override
	public List<Subscription> findAll() {
		return subscriptionRepository.findAll();
	}

	@Override
	public Subscription addSubscription(Subscription subscription) {
		return subscriptionRepository.save(subscription);
	}

	@Override
	public void deleteSubscription(Subscription subscription) {
		subscriptionRepository.delete(subscription);
	}

}
