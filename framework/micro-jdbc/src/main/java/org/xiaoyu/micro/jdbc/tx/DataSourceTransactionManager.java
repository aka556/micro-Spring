package org.xiaoyu.micro.jdbc.tx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xiaoyu.micro.exception.TransactionException;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceTransactionManager implements PlatformTransactionManager, InvocationHandler {

    static final ThreadLocal<TransactionStatus> transactionStatus = new ThreadLocal<>();

    Logger logger = LoggerFactory.getLogger(getClass());

    final DataSource dataSource;

    public DataSourceTransactionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TransactionStatus status = transactionStatus.get();
        if (status == null) {
            // start new transaction
            try (Connection connection = dataSource.getConnection()){
                final boolean autoCommit = connection.getAutoCommit();
                if (autoCommit) {
                    connection.setAutoCommit(false);
                }
                try {
                    transactionStatus.set(new TransactionStatus(connection));
                    Object object = method.invoke(proxy, args);
                    connection.commit();
                    return object;
                } catch (InvocationTargetException e) {
                    logger.warn("will rollback transaction for caused exception: {}", e.getCause() == null ? "null" : e.getCause().getClass().getName());
                    TransactionException te = new TransactionException(e.getCause());
                    try {
                        connection.rollback();
                    } catch (SQLException sqle) {
                        te.addSuppressed(sqle);
                    }
                    throw te;
                } finally {
                    transactionStatus.remove();
                    if (autoCommit) {
                        connection.setAutoCommit(true);
                    }
                }
            }
        } else {
            // join current transaction
            return method.invoke(proxy, args);
        }
    }
}
