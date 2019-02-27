package io.github.tiscs.scp.mybatis;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Properties;

@Component
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class PageInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof StatementHandler) {
            return prepare(invocation);
        } else if (invocation.getTarget() instanceof Executor) {
            return query(invocation);
        } else {
            return invocation.proceed();
        }
    }

    private Object prepare(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        RowBounds rowBounds = (RowBounds) metaObject.getValue("delegate.rowBounds");
        if (rowBounds != null && rowBounds.getLimit() < RowBounds.NO_ROW_LIMIT) {
            metaObject.setValue("delegate.boundSql.sql", statementHandler.getBoundSql().getSql() + " LIMIT " + rowBounds.getLimit() + " OFFSET " + rowBounds.getOffset());
            metaObject.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
            metaObject.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
        }
        return invocation.proceed();
    }

    private Object query(Invocation invocation) throws Throwable {
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
