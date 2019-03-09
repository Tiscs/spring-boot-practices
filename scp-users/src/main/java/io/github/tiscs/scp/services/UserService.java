package io.github.tiscs.scp.services;

import io.github.tiscs.scp.mappers.UserMapper;
import io.github.tiscs.scp.models.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> findAll(RowBounds rowBounds) {
        return userMapper.findAll(rowBounds);
    }

    public User findById(UUID id) {
        return userMapper.findById(id);
    }
}