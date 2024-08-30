package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.exception.BadRequestException;
import com.example.exception.DuplicateUsernameException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     *  Requirement #1: Process new user registrations.
     *  @param account The body of the request reprenting a new Account object to be registered, not including account_id.
     *  @return A ResponseEntity with the status of 200 (OK) 
     *          and a body representing the newly registered account, including its generated account_id. 
     */
    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) throws DuplicateUsernameException, BadRequestException {
        Account registered = accountService.registerAccount(account);
        return ResponseEntity.status(HttpStatus.OK).body(registered);
    }
    

    // ******************
    // Exception Handling
    // ******************
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequest(BadRequestException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String duplicateUserNameConflict(DuplicateUsernameException ex) {
        return ex.getMessage();
    }
}
