package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     *  @param  message The new message to be created, not including message_id.
     *  @return The newly created message, including its generated message_id. 
     *  @throws BadRequestException When the given message's text is blank or has a length greater than 255.
     *                              Or when the user that the message is posted by does not exist.
     */
    public Message addMessage(Message message) throws BadRequestException {
        if(message.getMessageText().isBlank()) {
            throw new BadRequestException("Your message must not be blank.");
        } else if(message.getMessageText().length() > 255) {
            throw new BadRequestException("Your message must not exceed 255 characters.");
        } else if(!accountRepository.findById(message.getPostedBy()).isPresent()) {
            throw new BadRequestException("You do not exist!");
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
    public Message getMessageByMessageId(int messageId) {
        Optional<Message> optMessage = messageRepository.findById(messageId);
        return optMessage.orElse(null);
    }
}
