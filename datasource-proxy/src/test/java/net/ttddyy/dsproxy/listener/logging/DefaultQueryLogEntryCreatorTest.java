package net.ttddyy.dsproxy.listener.logging;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.ExecutionInfoBuilder;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.QueryInfoBuilder;
import net.ttddyy.dsproxy.StatementType;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Tadaya Tsuyukubo
 */
public class DefaultQueryLogEntryCreatorTest {

    @Test
    public void getLogEntryForStatement() throws Exception {
        Method method = Object.class.getMethod("toString");
        Object result = new Object();

        ExecutionInfo executionInfo = ExecutionInfoBuilder
                .create()
                .dataSourceName("foo")
                .elapsedTime(100)
                .method(method)
                .result(result)
                .statementType(StatementType.STATEMENT)
                .success(true)
                .batch(false)
                .batchSize(0)
                .build();

        QueryInfo queryInfo = QueryInfoBuilder.create().query("select 1").build();

        DefaultQueryLogEntryCreator creator = new DefaultQueryLogEntryCreator();

        String entry = creator.getLogEntry(executionInfo, Lists.newArrayList(queryInfo), true);
        assertThat(entry).isEqualTo("Name:foo, Time:100, Success:True, Type:Statement, Batch:False, QuerySize:1, BatchSize:0, Query:[\"select 1\"], Params:[()]");

    }

    @Test
    public void getLogEntryForBatchStatement() throws Exception {
        Method method = Object.class.getMethod("toString");
        Object result = new Object();

        ExecutionInfo executionInfo = ExecutionInfoBuilder
                .create()
                .dataSourceName("foo")
                .elapsedTime(100)
                .method(method)
                .result(result)
                .statementType(StatementType.STATEMENT)
                .success(true)
                .batch(true)
                .batchSize(2)
                .build();

        QueryInfo queryInfo1 = QueryInfoBuilder.create().query("select 1").build();
        QueryInfo queryInfo2 = QueryInfoBuilder.create().query("select 2").build();

        DefaultQueryLogEntryCreator creator = new DefaultQueryLogEntryCreator();

        String entry = creator.getLogEntry(executionInfo, Lists.newArrayList(queryInfo1, queryInfo2), true);
        assertThat(entry).isEqualTo("Name:foo, Time:100, Success:True, Type:Statement, Batch:True, QuerySize:2, BatchSize:2, Query:[\"select 1\",\"select 2\"], Params:[(),()]");

    }

    @Test
    public void getLogEntryForPreparedStatement() throws Exception {
        Method method = Object.class.getMethod("toString");
        Object result = new Object();

        ExecutionInfo executionInfo = ExecutionInfoBuilder
                .create()
                .dataSourceName("foo")
                .elapsedTime(100)
                .method(method)
                .result(result)
                .statementType(StatementType.PREPARED)
                .success(true)
                .batch(false)
                .batchSize(0)
                .build();

        QueryInfo queryInfo = QueryInfoBuilder.create()
                .query("select 1")
                .param(1, "foo")
                .param(2, 100)
                .build();

        DefaultQueryLogEntryCreator creator = new DefaultQueryLogEntryCreator();

        String entry = creator.getLogEntry(executionInfo, Lists.newArrayList(queryInfo), true);
        assertThat(entry).isEqualTo("Name:foo, Time:100, Success:True, Type:Prepared, Batch:False, QuerySize:1, BatchSize:0, Query:[\"select 1\"], Params:[(foo,100)]");

    }

    @Test
    public void getLogEntryForBatchPreparedStatement() throws Exception {
        Method method = Object.class.getMethod("toString");
        Object result = new Object();

        ExecutionInfo executionInfo = ExecutionInfoBuilder
                .create()
                .dataSourceName("foo")
                .elapsedTime(100)
                .method(method)
                .result(result)
                .statementType(StatementType.PREPARED)
                .success(true)
                .batch(true)
                .batchSize(2)
                .build();

        QueryInfo queryInfo = QueryInfoBuilder.create()
                .query("select 1")
                .batchParam(1, 1, "foo")
                .batchParam(1, 2, 100)
                .batchParam(2, 1, "bar")
                .batchParam(2, 2, 200)
                .build();

        DefaultQueryLogEntryCreator creator = new DefaultQueryLogEntryCreator();

        String entry = creator.getLogEntry(executionInfo, Lists.newArrayList(queryInfo), true);
        assertThat(entry).isEqualTo("Name:foo, Time:100, Success:True, Type:Prepared, Batch:True, QuerySize:1, BatchSize:2, Query:[\"select 1\"], Params:[(foo,100),(bar,200)]");

    }

    @Test
    public void getLogEntryForCallableStatement() throws Exception {
        Method method = Object.class.getMethod("toString");
        Object result = new Object();

        ExecutionInfo executionInfo = ExecutionInfoBuilder
                .create()
                .dataSourceName("foo")
                .elapsedTime(100)
                .method(method)
                .result(result)
                .statementType(StatementType.CALLABLE)
                .success(true)
                .batch(false)
                .batchSize(0)
                .build();

        QueryInfo queryInfo = QueryInfoBuilder.create()
                .query("select 1")
                .param("name", "foo")
                .param("id", 100)
                .build();

        DefaultQueryLogEntryCreator creator = new DefaultQueryLogEntryCreator();

        String entry = creator.getLogEntry(executionInfo, Lists.newArrayList(queryInfo), true);
        assertThat(entry).isEqualTo("Name:foo, Time:100, Success:True, Type:Callable, Batch:False, QuerySize:1, BatchSize:0, Query:[\"select 1\"], Params:[(id=100,name=foo)]");

    }

    @Test
    public void getLogEntryForBatchCallableStatement() throws Exception {
        Method method = Object.class.getMethod("toString");
        Object result = new Object();

        ExecutionInfo executionInfo = ExecutionInfoBuilder
                .create()
                .dataSourceName("foo")
                .elapsedTime(100)
                .method(method)
                .result(result)
                .statementType(StatementType.CALLABLE)
                .success(true)
                .batch(true)
                .batchSize(2)
                .build();

        QueryInfo queryInfo = QueryInfoBuilder.create()
                .query("select 1")
                .batchParam(1, "name", "foo")
                .batchParam(1, "id", 100)
                .batchParam(2, "name", "bar")
                .batchParam(2, "id", 200)
                .build();

        DefaultQueryLogEntryCreator creator = new DefaultQueryLogEntryCreator();

        String entry = creator.getLogEntry(executionInfo, Lists.newArrayList(queryInfo), true);
        assertThat(entry).isEqualTo("Name:foo, Time:100, Success:True, Type:Callable, Batch:True, QuerySize:1, BatchSize:2, Query:[\"select 1\"], Params:[(id=100,name=foo),(id=200,name=bar)]");

    }

    @Test
    public void getLogEntryParameterOrderWithIndex() throws Exception {
        Method method = Object.class.getMethod("toString");
        Object result = new Object();

        ExecutionInfo executionInfo = ExecutionInfoBuilder
                .create()
                .dataSourceName("foo")
                .elapsedTime(100)
                .method(method)
                .result(result)
                .statementType(StatementType.PREPARED)
                .success(true)
                .batch(true)
                .batchSize(2)
                .build();

        QueryInfo queryInfo = QueryInfoBuilder.create()
                .query("select 1")
                .batchParam(1, 2, 100)  // batch 1, index 2
                .batchParam(1, 3, "FOO")  // batch 1, index 3
                .batchParam(1, 1, "foo")  // batch 1, index 1
                .batchParam(2, 1, "bar")  // batch 2, index 1
                .batchParam(2, 3, "BAR")  // batch 2, index 3
                .batchParam(2, 2, 200)  // batch 2, index 2
                .build();

        DefaultQueryLogEntryCreator creator = new DefaultQueryLogEntryCreator();

        String entry = creator.getLogEntry(executionInfo, Lists.newArrayList(queryInfo), true);
        assertThat(entry).containsOnlyOnce("Params:[(foo,100,FOO),(bar,200,BAR)]");

    }

    @Test
    public void getLogEntryParameterOrderWithNamedParam() throws Exception {
        Method method = Object.class.getMethod("toString");
        Object result = new Object();

        ExecutionInfo executionInfo = ExecutionInfoBuilder
                .create()
                .dataSourceName("foo")
                .elapsedTime(100)
                .method(method)
                .result(result)
                .statementType(StatementType.CALLABLE)
                .success(true)
                .batch(true)
                .batchSize(2)
                .build();

        QueryInfo queryInfo = QueryInfoBuilder.create()
                .query("select 1")
                .batchParam(1, "c-idx", 100)
                .batchParam(1, "b-idx", "FOO")
                .batchParam(1, "a-idx", "foo")
                .batchParam(2, "b-idx", "BAR")
                .batchParam(2, "a-idx", "bar")
                .batchParam(2, "c-idx", 200)
                .build();

        DefaultQueryLogEntryCreator creator = new DefaultQueryLogEntryCreator();

        String entry = creator.getLogEntry(executionInfo, Lists.newArrayList(queryInfo), true);
        assertThat(entry).containsOnlyOnce("Params:[(a-idx=foo,b-idx=FOO,c-idx=100),(a-idx=bar,b-idx=BAR,c-idx=200)]");

    }

    @Test
    public void statementType() throws Exception {
        ExecutionInfo executionInfo;
        String result;

        DefaultQueryLogEntryCreator creator = new DefaultQueryLogEntryCreator();

        // Statement
        executionInfo = ExecutionInfoBuilder.create().statementType(StatementType.STATEMENT).build();
        result = creator.getLogEntry(executionInfo, new ArrayList<QueryInfo>(), true);
        assertThat(result).containsOnlyOnce("Type:Statement");

        // PreparedStatement
        executionInfo = ExecutionInfoBuilder.create().statementType(StatementType.PREPARED).build();
        result = creator.getLogEntry(executionInfo, new ArrayList<QueryInfo>(), true);
        assertThat(result).containsOnlyOnce("Type:Prepared");

        // CallableStatement
        executionInfo = ExecutionInfoBuilder.create().statementType(StatementType.CALLABLE).build();
        result = creator.getLogEntry(executionInfo, new ArrayList<QueryInfo>(), true);
        assertThat(result).containsOnlyOnce("Type:Callable");
    }

    @Test
    public void query() throws Exception {
        ExecutionInfo executionInfo = ExecutionInfoBuilder.create().build();
        QueryInfo select1 = QueryInfoBuilder.create().query("select 1").build();
        QueryInfo select2 = QueryInfoBuilder.create().query("select 2").build();
        QueryInfo select3 = QueryInfoBuilder.create().query("select 3").build();

        DefaultQueryLogEntryCreator creator = new DefaultQueryLogEntryCreator();
        String result;

        // single query
        result = creator.getLogEntry(executionInfo, Arrays.asList(select1), true);
        assertThat(result).containsOnlyOnce("Query:[\"select 1\"]");

        // multiple query
        result = creator.getLogEntry(executionInfo, Arrays.asList(select1, select2, select3), true);
        assertThat(result).containsOnlyOnce("Query:[\"select 1\",\"select 2\",\"select 3\"]");
    }

    @Test
    public void querySize() throws Exception {
        ExecutionInfo executionInfo = ExecutionInfoBuilder.create().build();
        QueryInfo select1 = QueryInfoBuilder.create().query("select 1").build();
        QueryInfo select2 = QueryInfoBuilder.create().query("select 2").build();
        QueryInfo select3 = QueryInfoBuilder.create().query("select 3").build();

        DefaultQueryLogEntryCreator creator = new DefaultQueryLogEntryCreator();
        String result;

        // single query
        result = creator.getLogEntry(executionInfo, Arrays.asList(select1), true);
        assertThat(result).containsOnlyOnce("QuerySize:1");

        // multiple query
        result = creator.getLogEntry(executionInfo, Arrays.asList(select1, select2, select3), true);
        assertThat(result).containsOnlyOnce("QuerySize:3");
    }

    @Test
    public void success() throws Exception {
        ExecutionInfo executionInfo;
        String result;

        DefaultQueryLogEntryCreator creator = new DefaultQueryLogEntryCreator();

        // success
        executionInfo = ExecutionInfoBuilder.create().success(true).build();
        result = creator.getLogEntry(executionInfo, new ArrayList<QueryInfo>(), true);
        assertThat(result).containsOnlyOnce("Success:True");

        // fail
        executionInfo = ExecutionInfoBuilder.create().success(false).build();
        result = creator.getLogEntry(executionInfo, new ArrayList<QueryInfo>(), true);
        assertThat(result).containsOnlyOnce("Success:False");

    }

    @Test
    public void batch() throws Exception {
        ExecutionInfo executionInfo;
        String result;

        DefaultQueryLogEntryCreator creator = new DefaultQueryLogEntryCreator();

        // success
        executionInfo = ExecutionInfoBuilder.create().batch(true).build();
        result = creator.getLogEntry(executionInfo, new ArrayList<QueryInfo>(), true);
        assertThat(result).containsOnlyOnce("Batch:True");

        // fail
        executionInfo = ExecutionInfoBuilder.create().batch(false).build();
        result = creator.getLogEntry(executionInfo, new ArrayList<QueryInfo>(), true);
        assertThat(result).containsOnlyOnce("Batch:False");
    }

    @Test
    public void batchSize() throws Exception {
        ExecutionInfo executionInfo;
        String result;

        DefaultQueryLogEntryCreator creator = new DefaultQueryLogEntryCreator();

        // default
        executionInfo = ExecutionInfoBuilder.create().build();
        result = creator.getLogEntry(executionInfo, new ArrayList<QueryInfo>(), true);
        assertThat(result).containsOnlyOnce("BatchSize:0");

        executionInfo = ExecutionInfoBuilder.create().batchSize(100).build();
        result = creator.getLogEntry(executionInfo, new ArrayList<QueryInfo>(), true);
        assertThat(result).containsOnlyOnce("BatchSize:100");
    }

}
