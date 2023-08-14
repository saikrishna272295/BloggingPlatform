package com.sai.controller;

import com.sai.model.BlogPost;
import com.sai.model.User;
import com.sai.model.UserRole;
import com.sai.repo.BlogPostRepository;
import com.sai.repo.UserRepository;
import com.sai.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BlogController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    @Qualifier("passwordEncoder")
    private PasswordEncoder passwordEncoder;

    private static Logger logger;

    static {
        logger  = LoggerFactory.getLogger(BlogController.class);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.getRoles().add(UserRole.USER);
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/create-post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> createPost(@RequestBody BlogPost blogPost ) {
        User author = getCurrentUser();
        User requestedAuthor = userRepository.findById(blogPost.getAuthor().getId()).orElse(null);
        if (requestedAuthor == null) {
            return ResponseEntity.badRequest().body("Invalid author id");
        }
        blogPost.setAuthor(requestedAuthor);
        blogPostRepository.save(blogPost);
        return ResponseEntity.ok("Blog post created successfully.");
    }


    @GetMapping("/posts")
    public ResponseEntity<List<BlogPost>> getPostsByUser() {
        User author = getCurrentUser();
        List<BlogPost> posts = blogPostRepository.findByAuthor(author);
        return ResponseEntity.ok(posts);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            System.out.println("Authentication is null");
            return null;
        }
        return userRepository.findByUsername(authentication.getName());
    }
}
