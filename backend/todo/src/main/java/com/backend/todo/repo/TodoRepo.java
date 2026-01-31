package com.backend.todo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.todo.model.Item;

public interface TodoRepo extends JpaRepository<Item, Long> {

}
