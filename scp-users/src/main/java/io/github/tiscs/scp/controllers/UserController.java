package io.github.tiscs.scp.controllers;

import io.github.tiscs.scp.mappers.UserMapper;
import io.github.tiscs.scp.models.APIError;
import io.github.tiscs.scp.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Users")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = APIError.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = APIError.class),
            @ApiResponse(code = 501, message = "Not Implemented", response = APIError.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity fetch() {
        return ResponseEntity.ok(userMapper.find(new RowBounds(0, 1)));
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request", response = APIError.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = APIError.class),
            @ApiResponse(code = 501, message = "Not Implemented", response = APIError.class)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> fetch(@ApiParam(required = true) @PathVariable Long id) {
        return ResponseEntity.ok(userMapper.findOne(id));
    }
}
