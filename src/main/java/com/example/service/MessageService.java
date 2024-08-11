package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.InvalidInputException;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Message createNewMessage(Message message) throws InvalidInputException{
        String messageText = message.getMessageText();
        if(messageText.length() == 0 || messageText. length() > 255){
            throw new InvalidInputException("Message text cannot be blank or exceed 255 characters.");
        }
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        messageRepository.findAll().forEach(messages::add);
        return messages;
    }

    public Message getMessageById(int messageId){
        try{
            Message message = messageRepository.findById(messageId).get();
            return message;
        }
        catch(NoSuchElementException ex){
            return null;
        }
    }

    public Integer deleteMessageById(int messageId){
        try{
            Message message = messageRepository.findById(messageId).get();
            messageRepository.deleteById(messageId);
            return 1;
        }
        catch(NoSuchElementException ex){
            return null;
        }
    }

    public int updateMessageById(int messageId, String messageText) throws InvalidInputException{
        if(messageText.length() == 0 || messageText. length() > 255){
            throw new InvalidInputException("Message text cannot be blank or exceed 255 characters.");
        }
        Optional<Message> exisitingMessage = messageRepository.findById(messageId);
        if(exisitingMessage.isEmpty()){
            throw new InvalidInputException("The message corresponding to messageId: "+ messageId + " does not exists.");
        }
        Message updatedMessage = exisitingMessage.get();
        updatedMessage.setMessageText(messageText);
        messageRepository.save(updatedMessage);
        return 1;
    }

    public List<Message> getMessageByUserId(int accountId){
        return messageRepository.findByPostedBy(accountId);
    }
}
