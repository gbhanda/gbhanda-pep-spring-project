package com.example.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.example.entity.Message;

public interface MessageRepository extends CrudRepository<Message, Integer>{
    public List<Message> findByPostedBy(int postedBy);
}
