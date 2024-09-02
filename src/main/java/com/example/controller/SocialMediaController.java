package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.BadRequestException;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.UnauthorizedException;
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
     *  @param  account The body of the request representing a new Account object to be registered, not including accountId.
     *  @return A ResponseEntity with the status of 200 (OK) 
     *          and a body representing the newly registered account, including its generated accountId. 
     *  @throws DuplicateUsernameException
     *  @throws BadRequestException
     */
    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) throws DuplicateUsernameException, BadRequestException {
        Account registered = accountService.registerAccount(account);
        return ResponseEntity.status(HttpStatus.OK).body(registered);
    }

    /**
     *  Requirement #2: Process user logins.
     *  @param  account The body of the request representing the Account that is trying to log in.
     *  @return A ResponseEntity with the status of 200 (OK) 
     *          and a body representing the verified account, including its accountId.
     *  @throws UnauthorizedException
     */
    @PostMapping("/login")
    public ResponseEntity<Account> verifyLogin(@RequestBody Account account) throws UnauthorizedException {
        Account verified = accountService.verifyLogin(account);
        return ResponseEntity.status(HttpStatus.OK).body(verified);
    }

    /**
     *  Requirement #3: Process the creation of new messages.
     *  @param  message The body of the request representing the message to be created, not including messageId.
     *  @return A ResponseEntity with the status of 200 (OK) 
     *          and a body representing the created message, including its generated messageId.
     *  @throws BadRequestException
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) throws BadRequestException {
        Message createdMessage = messageService.addMessage(message);
        return ResponseEntity.status(HttpStatus.OK).body(createdMessage);
    }

    /**
     *  Requirement #4: Retrieve all messages.
     *  @return A ResponseEntity with the status of 200 (OK) 
     *          and a body representing a list of all available messages.
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> retrieveAllMessages() {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getAllMessages());
    }

     /**
     *  Requirement #5: Retrieve a message by its ID.
     *  @param  messageId The ID of the message to be retrieved.
     *  @return A ResponseEntity with the status of 200 (OK) 
     *          and a body representing the matching message. Body is empty if there is no matching message.
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> retrieveMessageByMessageId(@PathVariable int messageId) {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getMessageByMessageId(messageId));
    }

    /**
     *  Requirement #6: Delete a message identified by a message ID.
     *  @param  messageId The ID of the message to be deleted.
     *  @return A ResponseEntity with the status of 200 (OK) 
     *          and a body containing the number of rows in the database that were updated (deleted).
     *          The body will be empty if the deletion was unsuccessful.
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageByMessageId(@PathVariable int messageId) {
        int rowsUpdated = messageService.removeMessage(messageId);
        if(rowsUpdated > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(rowsUpdated);
        } else {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    /**
     *  Requirement #7: Update a message text identified by a message ID.
     *  @param  newMessage  A message object containing the new message text. (From the request body)
     *  @param  messageId   The ID of the message to be updated.
     *  @return A ResponseEntity with the status of 200 (OK) 
     *          and a body containing the number of rows in the database that were updated.
     *  @throws BadRequestException
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> patchMessageTextByMessageId(@RequestBody Message newMessage, @PathVariable int messageId) throws BadRequestException {
        String messageText = newMessage.getMessageText();
        return ResponseEntity.status(HttpStatus.OK).body(messageService.updateMessageText(messageText, messageId));
    }

    /**
     *  Requirement #8: Retrieve all messages written by a particular user.
     *  @param  accountId   The ID of the account which posted the messages to be retrieved.
     *  @return A ResponseEntity with the status of 200 (OK) 
     *          and a body containing a list of messages by the identified user.
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByUser(@PathVariable int accountId) {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getAllMessagesByUser(accountId));
    }

    // ******************
    // Exception Handling
    // ******************
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> badRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<String> duplicateUserNameConflict(DuplicateUsernameException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> unauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
