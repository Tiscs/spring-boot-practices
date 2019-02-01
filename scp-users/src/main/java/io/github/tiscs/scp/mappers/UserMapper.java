package io.github.tiscs.scp.mappers;

import io.github.tiscs.scp.annotations.MapperComponent;
import io.github.tiscs.scp.models.User;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@MapperComponent
public interface UserMapper {
    @Select("SELECT * FROM `users` ORDER BY `id` ASC")
    List<User> find(RowBounds bounds);

    @Select("SELECT * FROM `users` where `id` = #{id}")
    User findOne(long id);
}
