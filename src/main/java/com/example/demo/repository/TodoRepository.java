package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
