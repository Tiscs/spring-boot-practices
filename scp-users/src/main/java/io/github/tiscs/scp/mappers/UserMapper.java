package io.github.tiscs.scp.mappers;

import io.github.tiscs.scp.models.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserMapper {
    List<User> findAll(RowBounds bounds);

    User findById(@Param("id") UUID id);

    int delete(@Param("id") UUID id);

    int create(@Param("user") User user);

    int update(@Param("user") User user);
}
