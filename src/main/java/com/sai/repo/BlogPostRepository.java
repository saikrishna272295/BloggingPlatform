package com.sai.repo;

import com.sai.model.BlogPost;
import com.sai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByAuthor(User author);
}