package net.ttddyy.dsproxy;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Contains query execution information.
 *
 * @author Tadaya Tsuyukubo
 */
public class ExecutionInfo {
    private String dataSourceName;
    private long connectionId;
    private Method method;
    private Object[] methodArgs;
    private Object result;
    private long elapsedTime;
    private Throwable throwable;
    private StatementType statementType;
    private boolean isSuccess;
    private boolean isBatch;
    private int batchSize;
    private Statement statement;
    private ResultSet generatedKeys;

    public ExecutionInfo() {
    }

    public ExecutionInfo(ConnectionInfo connectionInfo, Statement statement, boolean isBatch, int batchSize, Method method, Object[] methodArgs) {
        this.dataSourceName = connectionInfo.getDataSourceName();
        this.connectionId = connectionInfo.getConnectionId();
        this.statement = statement;
        this.isBatch = isBatch;
        this.batchSize = batchSize;
        this.method = method;
        this.methodArgs = methodArgs;

        this.statementType = StatementType.valueOf(statement);
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getMethodArgs() {
        return methodArgs;
    }

    public void setMethodArgs(Object[] methodArgs) {
        this.methodArgs = methodArgs;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * @since 1.4.2
     */
    public long getConnectionId() {
        return connectionId;
    }

    /**
     * @since 1.4.2
     */
    public void setConnectionId(long connectionId) {
        this.connectionId = connectionId;
    }

    /**
     * Contains query execution result.
     * Only available after successful query execution.
     *
     * @return result of query
     */
    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * Duration of query execution.
     * Only available after successful query execution.
     *
     * @return query execution time
     */
    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    /**
     * Contains an exception thrown while query was executed.
     * Contains value only when an exception has thrown, otherwise {@code null}.
     *
     * @param throwable an error thrown while executing a query
     */
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public StatementType getStatementType() {
        return statementType;
    }

    public void setStatementType(StatementType statementType) {
        this.statementType = statementType;
    }

    /**
     * Indicate whether the query execution was successful or not.
     * Contains valid value only after the query execution.
     *
     * @return true when query has successfully executed
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isBatch() {
        return isBatch;
    }

    public void setBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    /**
     * Returns {@link java.sql.Statement}, {@link java.sql.PreparedStatement}, or {@link java.sql.CallableStatement}
     * used by the execution.
     *
     * @return statement/prepared/callable object
     * @since 1.3.1
     */
    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    /**
     * @return Generated keys of the current statement
     * @since 1.4.5
     */
    public ResultSet getGeneratedKeys() {
        return generatedKeys;
    }

    public void setGeneratedKeys(ResultSet generatedKeys) {
        this.generatedKeys = generatedKeys;
    }
}
