package io.github.tiscs.scp.mappers;

import io.github.tiscs.scp.models.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserMapper {
    List<User> find(RowBounds bounds);

    User findOne(@Param("id") UUID id);
}
