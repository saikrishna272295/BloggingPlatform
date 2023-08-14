package com.sai.service;

import com.sai.model.User;
import com.sai.repo.BlogPostRepository;
import com.sai.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}