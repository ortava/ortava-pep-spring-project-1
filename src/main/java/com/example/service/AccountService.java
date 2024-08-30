package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.BadRequestException;
import com.example.exception.DuplicateUsernameException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account registerAccount(Account account) throws DuplicateUsernameException, BadRequestException {
        if(account.getUsername().isBlank()) {
            throw new BadRequestException("Username must not be blank.");
        } else if(account.getPassword().length() < 4) {
            throw new BadRequestException("Password must be at least 4 characters.");
        } else if(accountRepository.findAccountByUsername(account.getUsername()) != null) {
            throw new DuplicateUsernameException("An account with that username already exists. Please try a different username.");
        } else {
            return accountRepository.save(account);
        }
    }
}
