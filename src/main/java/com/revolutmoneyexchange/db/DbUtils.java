package com.revolutmoneyexchange.db;

import com.revolutmoneyexchange.exceptions.ImpossibleOperationExecution;
import com.revolutmoneyexchange.model.ModelHasId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.function.BiConsumer;

/**
 * Utilities class contains a number of methods to manipulate with the data base
 *
 */
public class DbUtils {
    private static final Logger log = LoggerFactory.getLogger(DbUtils.class);

    private static final DbUtils dbUtils = new DbUtils();

    private DbUtils() {
    }

    /**
     * @return the singleton object of DbUtils class
     */
    public static DbUtils getInstance() {
        return dbUtils;
    }

    public <E> QueryResult<E> executeQuery(String query, QueryExecutor<E> queryExecutor) {
        Connection con = null;
        PreparedStatement preparedStatement = null;

        try {
            con = H2DataSource.getConnection();
            preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            QueryResult<E> qr = new QueryResult<>(queryExecutor.execute(preparedStatement));

            con.commit();

            return qr;
        } catch (Throwable th) {
            safeRollback(con);
            log.error("Unexpected exception", th);
            throw new ImpossibleOperationExecution(th);
        } finally {
            quietlyClose(preparedStatement);

            quietlyClose(con);
        }
    }


    public <E> QueryResult<E> executeQueryInConnection(Connection con, String query, QueryExecutor<E> queryExecutor) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            return new QueryResult<>(queryExecutor.execute(preparedStatement));
        } catch (Throwable th) {
            log.error("Unexpected exception", th);
            throw new ImpossibleOperationExecution(th);
        } finally {
            quietlyClose(preparedStatement);
        }
    }

    private static void quietlyClose(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                log.error("Unexpected exception", e);
            }
        }

    }

    public static void quietlyClose(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.error("Unexpected exception", e);
            }
        }
    }

    public static void safeRollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException e) {
                log.error("Unexpected exception", e);
            }
        }
    }

    public interface QueryExecutor<T> {
        T execute(PreparedStatement preparedStatement) throws SQLException;
    }

    public static class QueryResult<T> {
        private T result;

        public QueryResult(T result) {
            this.result = result;
        }

        public T getResult() {
            return result;
        }
    }


    public static class CreationQueryExecutor<T extends ModelHasId> implements QueryExecutor<T> {
        private T object;
        private BiConsumer<PreparedStatement, T> fillInPreparedStatement;

        public CreationQueryExecutor(T object, BiConsumer<PreparedStatement, T> fillInPreparedStatement) {
            this.object = object;
            this.fillInPreparedStatement = fillInPreparedStatement;
        }

        @Override
        public T execute(PreparedStatement preparedStatement) throws SQLException {
            fillInPreparedStatement.accept(preparedStatement, object);

            int res = preparedStatement.executeUpdate();

            Long obtainedId = null;

            if (res != 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        obtainedId = generatedKeys.getLong(1);
                    }
                }
            }

            if (obtainedId == null) {
                return null;
            }

            object.setAccountNumber(obtainedId);

            return object;

        }
    }
}
