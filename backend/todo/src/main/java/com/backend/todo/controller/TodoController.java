package com.backend.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.todo.model.Item;
import com.backend.todo.repo.TodoRepo;


@RestController
@RequestMapping("/todo")
@CrossOrigin
public class TodoController {

    @Autowired
    private TodoRepo todoRepo;

    @GetMapping
    public List<Item> getAllTodos() {
        return todoRepo.findAll();
    }

    @PostMapping
    public Item createTodo(@RequestBody Item item) {
        return todoRepo.save(item);
    }

    @PutMapping
    public Item updateTodo(@RequestBody Item item) {
        return todoRepo.save(item);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        todoRepo.deleteById(id);
    }
}
