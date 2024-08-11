package com.example.controller;

import java.util.List;

import javax.security.sasl.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.ResponseBody;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.InvalidInputException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public @ResponseBody ResponseEntity<Account> registerUser(@RequestBody Account newAccount){
        Account account = accountService.registerUser(newAccount);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @PostMapping("login")
    public @ResponseBody ResponseEntity<Account> processLogin(@RequestBody Account newAccount) throws AuthenticationException{
        Account account = accountService.processLogin(newAccount);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @PostMapping("messages")
    public @ResponseBody ResponseEntity<Message> createMessage(@RequestBody Message newMessage){
        boolean userExists = accountService.accountExists(newMessage.getPostedBy());
        if(!userExists){
            throw new InvalidInputException("The user that created the message does not exists.");
        }
        Message message = messageService.createNewMessage(newMessage);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("messages")
    public @ResponseBody ResponseEntity<List<Message>> retrieveAllMessages(){
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @GetMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<Message> retrieveMessage(@PathVariable int messageId){
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @DeleteMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<Integer> deleteMessage(@PathVariable int messageId){
        Integer rowsUpdated = messageService.deleteMessageById(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(rowsUpdated);
    }

    @PatchMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<Integer> updateMessage(@PathVariable int messageId, @RequestBody Message message){
        int rowsUpdated = messageService.updateMessageById(messageId, message.getMessageText());
        return ResponseEntity.status(HttpStatus.OK).body(rowsUpdated);
    }

    @GetMapping("accounts/{accountId}/messages")
    public @ResponseBody ResponseEntity<List<Message>> retrieveMessagesByUser(@PathVariable int accountId){
        List<Message> messages = messageService.getMessageByUserId(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }
}
