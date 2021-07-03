package com.example.demo.controller;

import java.util.List;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.exception.SubscriptionCantBeCreatedException;
import com.example.demo.exception.SubscriptionNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.model.Subscription;
import com.example.demo.service.SubscriptionServiceInterface;



@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class SubscriptionController {

	private static final Logger LOG = LoggerFactory.getLogger(SubscriptionController.class);
	
	@Autowired
	SubscriptionServiceInterface subscriptionServiceInterface;
	
	@GetMapping("/subscription")
	public List<Subscription> getAllSubscriptions() {
	    LOG.info("Subscriptions are Retrieved");
	    return subscriptionServiceInterface.findAll();
	}
	
	@PostMapping("/subscription")
	public Subscription createSubscription(@Valid @RequestBody Subscription subscription) {
		Subscription subscription2 = null;
		try {
		Book book = httpClient(subscription.getBook_id());
		if(book !=null) {
			httpClient1(book,-1);
	                subscription2 = subscriptionServiceInterface.addSubscription(subscription);
			LOG.info("Subscription is Created");
		}
		}
		catch(Exception e) {
			throw new SubscriptionCantBeCreatedException("Subscription", "bookid", subscription.getBook_id());
		}
		return subscription2;
	}
	
	@GetMapping("/subscription/{id}")
	public Subscription getSubscriptionByName(@PathVariable(value = "id") String subscription_name) {
	    LOG.info("Subscription is Retrieved with a particular name");
	    return subscriptionServiceInterface.findByName(subscription_name).orElseThrow(() -> new SubscriptionNotFoundException("Subscription", "id", subscription_name));
	}
	
	@PutMapping("/subscription/{id}")
	public Subscription updateSubscription(@PathVariable(value = "id") String subscription_name,
	                                        @Valid @RequestBody Subscription subscription) {

		Subscription subscription2 = subscriptionServiceInterface.findByName(subscription_name).orElseThrow(() -> new SubscriptionNotFoundException("Subscription", "id", subscription_name));
	    if(subscription2.getBook_id() != subscription.getBook_id()) {
	    	 try {
	 			Book book = httpClient(subscription.getBook_id());
	 			Book book1 = httpClient(subscription2.getBook_id());
	 			if(book !=null && (book.getAvailable_copies() <=book.getTotal_copies())) {
	 				httpClient1(book,1);
	 			}
	 			if(book1 !=null && (book1.getAvailable_copies() <=book1.getTotal_copies())) {
	 				httpClient1(book1,-1);
	 			}
	 			}
	 			catch(Exception e) {
	 				throw new SubscriptionCantBeCreatedException("Subscription", "bookid", subscription.getBook_id());
	 			}
	    }
	    subscription.setSubscription_name(subscription_name);
	    subscription.setBook_id(subscription.getBook_id());
	    Subscription updatedSubscription = subscriptionServiceInterface.addSubscription(subscription);
	    LOG.info("Subscription is Updated with a particular name");
	    return updatedSubscription;
	}
	
	@DeleteMapping("/subscription/{id}")
	public ResponseEntity<?> deleteSubscription(@PathVariable(value = "id") String subscription_name) {
	    Subscription subscription = subscriptionServiceInterface.findByName(subscription_name).orElseThrow(() -> new SubscriptionNotFoundException("Subscription", "id", subscription_name));
	    try {
			Book book = httpClient(subscription.getBook_id());
			if(book !=null && (book.getAvailable_copies() <=book.getTotal_copies())) {
				httpClient1(book,1);
			        subscriptionServiceInterface.deleteSubscription(subscription);
			        LOG.info("Subscription is Deleted with a particular name");
			}
			}
			catch(Exception e) {
				throw new SubscriptionCantBeCreatedException("Subscription", "bookid", subscription.getBook_id());
			}
	    return ResponseEntity.ok().build();
	}
	
	private Book httpClient(Long id) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Book> entity = new HttpEntity<>(headers);
		String uri = "http://localhost:9082/api/book/"+id;
		return restTemplate().exchange(uri,HttpMethod.GET,entity,Book.class).getBody();
	}
	
	private Book httpClient1(Book book,int count) {
		HttpHeaders headers = new HttpHeaders();
	      HttpEntity<Book> entity = new HttpEntity<Book>(book,headers);
	      
	      return restTemplate().exchange(
	    		  "http://localhost:9082/api/book/updateAvailaibility/"+book.getBook_id() +"/" +count, HttpMethod.PUT, entity, Book.class).getBody();
	}
	
	public RestTemplate restTemplate() {
		HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		return new RestTemplate(httpComponentsClientHttpRequestFactory);
	}
	

}
