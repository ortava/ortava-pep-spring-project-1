package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.BadRequestException;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.UnauthorizedException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     *  @param  account The new account to be registered, not including accountId.
     *  @return The newly registered account, including its generated accountId. 
     *  @throws DuplicateUsernameException When the given account's username already belongs to an account within the db.
     *  @throws BadRequestException When the given account's username is blank or the password is less than 4 characters.
     */
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

    /**
     *  @param  account The account attempting to log in, not including accountId.
     *  @return The verified account, including accountId.
     *  @throws UnauthorizedException When the login is unsuccessful (invalid username/password combination).
     */
    public Account verifyLogin(Account account) throws UnauthorizedException {
        Account existingAccount = accountRepository.findAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
        if(existingAccount != null) {
            return existingAccount;
        } else {
            throw new UnauthorizedException("Invalid username/password combination.");
        }
    }
}
