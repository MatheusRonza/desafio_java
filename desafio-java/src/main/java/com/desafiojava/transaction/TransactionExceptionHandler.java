package com.desafiojava.transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TransactionExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<Object> handle(InvalidTransactionException e){
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
