package com.desafio.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.desafiojava.transaction.Transaction;

@Service
public class AuthorizationService {
	
	private static final Logger Logger = LoggerFactory.getLogger(AuthorizationService.class);

	private RestClient restClient;
	
	public AuthorizationService(RestClient.Builder builder) {
		this.restClient = builder
				.baseUrl("https//run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc")
				.build();
	}
	
	public void Authorize(Transaction transaction) {
	Logger.info("Authorizing Transaction: {}", transaction);		
	var response = restClient.get()
	.retrieve()
	.toEntity(Authorization.class);
	
	if (response.getStatusCode().isError() || !response.getBody().isAuthorized()) 
		throw new UnauthorizedTransactionException("Unauthorized Exception!");

	Logger.info("Transaction authorized: {}", transaction);		
	
	}
	
}
