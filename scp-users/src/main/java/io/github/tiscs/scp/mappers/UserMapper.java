package io.github.tiscs.scp.mappers;

import io.github.tiscs.scp.annotations.MapperComponent;
import io.github.tiscs.scp.entities.UserEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@MapperComponent
public interface UserMapper {
    @Select("SELECT * FROM `users`")
    List<UserEntity> findAll();
}
