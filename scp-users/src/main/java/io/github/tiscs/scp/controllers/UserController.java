package io.github.tiscs.scp.controllers;

import io.github.tiscs.scp.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity fetch() {
        return ResponseEntity.ok(userMapper.findAll());
    }
}
