package com.desafiojava.transaction;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.authorization.AuthorizationService;
import com.desafiojava.notification.NotificationService;
import com.desafiojava.wallet.Wallet;
import com.desafiojava.wallet.WalletRepository;
import com.desafiojava.wallet.WalletType;

@Service
public class TransactionService {
	private final TransactionRepository transactionRepository;
	private final WalletRepository walletRepository;
	private final AuthorizationService authorizeService;
	private final NotificationService notificationService;
	
	public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository
			, AuthorizationService authorizeService, NotificationService notificationService) {
		super();
		this.transactionRepository = transactionRepository;
		this.walletRepository = walletRepository;
		this.authorizeService = authorizeService;
		this.notificationService = notificationService;
	}
	
	@Transactional
	public Transaction create(Transaction transaction) {
		validate(transaction);
		
		var newTransaction = transactionRepository.save(transaction);
		
		var walletPayer = walletRepository.findById(transaction.payer()).get();
		walletRepository.save(walletPayer.debit(transaction.value()));

		var walletPayee = walletRepository.findById(transaction.payer()).get();
		walletRepository.save(walletPayee.credit(transaction.value()));
		
		
		authorizeService.Authorize(transaction);
		
		notificationService.notify(transaction);
		
		return newTransaction;
	}
	
	private void validate(Transaction transaction) {
		walletRepository.findById(transaction.payee())
		.map(payee -> walletRepository.findById(transaction.payer())
		.map(payer -> isTransactionValid(transaction, payer) ? transaction : null)
		.orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s")))
		.orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s"));
	}

	private boolean isTransactionValid(Transaction transaction, Wallet payer) {
		return payer.type() == WalletType.COMUM.getValue()&& 
		payer.balance().compareTo(transaction.value())>= 0 &&
		!payer.id().equals(transaction.payee());
	}
	
	public List<Transaction> list(){
		return transactionRepository.findAll();
	}
}


