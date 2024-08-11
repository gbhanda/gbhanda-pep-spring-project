package com.example.service;

import javax.security.sasl.AuthenticationException;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.InvalidInputException;
import com.example.exception.UserAlreadyExistsException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public boolean accountExists(int accountId){
        if(accountRepository.findById(accountId).isPresent()){
            return true;
        }
        return false;
    }

    public Account registerUser(Account newAccount) throws UserAlreadyExistsException, InvalidInputException{
        String username = newAccount.getUsername();
        String password = newAccount.getPassword();
        if(username.length() == 0){
            throw new InvalidInputException("Username cannot be blank");
        }
        else if(accountRepository.findByUsername(username) != null){
            throw new UserAlreadyExistsException("Username: " + username + " already Exists." );
        }
        else if(password.length() < 4){
            throw new InvalidInputException("Password should be atleast 4 characters long.");
        }

        return accountRepository.save(newAccount);
    }

    public Account processLogin(Account loginInfo) throws AuthenticationException{
        String username = loginInfo.getUsername();
        String password = loginInfo.getPassword();
        Account account = accountRepository.findByUsername(username);
        if(account == null || !account.getPassword().equals(password)){
            throw new AuthenticationException("Login information does not match existing record");
        }
        return account;
    }
}
