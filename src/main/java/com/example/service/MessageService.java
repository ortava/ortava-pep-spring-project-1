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

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageByMessageId(int messageId) {
        Optional<Message> optMessage = messageRepository.findById(messageId);
        return optMessage.orElse(null);
    }
}
