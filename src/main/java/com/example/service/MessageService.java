package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;
import com.example.exception.BadRequestException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     *  @param  message The new message to be created, not including messageId.
     *  @return The newly created message, including its generated messageId. 
     *  @throws BadRequestException When the given message's text is blank or has a length greater than 255.
     *                              Or when the user that the message is posted by does not exist.
     */
    public Message addMessage(Message message) throws BadRequestException {
        if(message.getMessageText().isBlank()) {
            throw new BadRequestException("Message text must not be blank.");
        } else if(message.getMessageText().length() > 255) {
            throw new BadRequestException("Message text must not exceed 255 characters.");
        } else if(!accountRepository.findById(message.getPostedBy()).isPresent()) {
            throw new BadRequestException("User does not exist!");
        } else {
            return messageRepository.save(message);
        }
    }

    /**
     *  @return A list of all the messages that exist within the database.
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     *  @param  messageId The ID of the message to be retrieved from the database.
     *  @return The matching message from the database. If there is no message with the given ID, return null.
     */
    public Message getMessage(int messageId) {
        Optional<Message> optMessage = messageRepository.findById(messageId);
        return optMessage.orElse(null);
    }

    /**
     *  @param  messageId The ID of the message to be deleted from the database.
     *  @return The number of rows that were updated (deleted) in the database. 
     */
    @Transactional
    public int deleteMessage(int messageId) {
        return messageRepository.deleteByMessageId(messageId);
    }

    /**
     *  @param  messageText The new message text that will be used to update the existing message.
     *  @param  messageId   The ID of the message to be updated.
     *  @return The number of rows that were updated in the database.
     *  @throws BadRequestException When the new message text is blank or has a length greater than 255.
     *                              Or when the message to be updated does not exist.
     */
    @Transactional
    public int updateMessageText(String messageText, int messageId) throws BadRequestException {
        if(messageText.isBlank()) {
            throw new BadRequestException("Message text must not be blank.");
        } else if(messageText.length() > 255) {
            throw new BadRequestException("Message text must not exceed 255 characters.");
        } else if(!messageRepository.existsById(messageId)) {
            throw new BadRequestException("The message you are trying to update does not exist.");
        } else {
            return messageRepository.updateMessageTextByMessageId(messageText, messageId);
        }
    }

    /**
     *  @param  accountId   The ID of the account which posted the messages to be retrieved.
     *  @return A list of messages posted by the identified account.
     */
    public List<Message> getAllMessagesByAccount(int accountId) {
        return messageRepository.findMessagesByPostedBy(accountId);
    }
}
