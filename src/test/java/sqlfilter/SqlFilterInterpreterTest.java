package sqlfilter;

import core.domain.*;
import core.domain.operator.Operator;
import dialect.sqlserver.SqlServerInterpreter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SqlFilterInterpreterTest {
    final static String SQL_QUERY_1 = "SELECT age, cc.name" +
            " FROM dbo.great_women" +
            " WHERE cc.name IN (:NAME)" +
            " ORDER BY age DESC" +
            " OFFSET :OFFSET ROWS FETCH NEXT :SIZE ROWS ONLY";
    final static String SQL_QUERY_2 = "SELECT age, cc.name" +
            " FROM dbo.great_women" +
            " WHERE cc.name IN (:NAME)" +
            " ORDER BY age DESC, cc.name ASC" +
            " OFFSET :OFFSET ROWS FETCH NEXT :SIZE ROWS ONLY";
    final static String SQL_QUERY_WITH_DEFAULT_PAGE = "SELECT age, cc.name" +
            " FROM dbo.great_women" +
            " WHERE cc.name IN (:NAME)" +
            " OFFSET :OFFSET ROWS FETCH NEXT :SIZE ROWS ONLY";

    final static Set<String> SUPPORTED_FIELDS = Set.of("name", "age");

    @Test
    void queryWithOB() {
        final Query query = Query.builder()
                .resource("great_women")
                .columns(Set.of("name", "age"))
                .filters(Set.of(
                        Filter.builder()
                                .name("name")
                                .operation(Operation.builder()
                                        .operator(Operator.IN)
                                        .values(new Object[]{"Heidy", "Ada", "Marie"})
                                        .build())
                                .build()
                ))
                .orderBy(Set.of(OrderBy.builder()
                        .name("age")
                        .direction(Direction.DESC)
                        .build()
                ))
                .page(
                        Page.builder()
                                .number(1)
                                .size(20)
                                .build()
                )
                .build();
        final Map<String, String> aliasMap = Map.of("name", "cc.name");
        final SQLQuery sqlQuery = SqlServerInterpreter.builder("dbo", SUPPORTED_FIELDS,aliasMap).translate(query);

        assertThat(sqlQuery.getQuery()).isEqualTo(SQL_QUERY_1);
    }

    @Test
    void queryWithTwoOB() {
        final Query query = Query.builder()
                .resource("great_women")
                .columns(Set.of("name", "age"))
                .filters(Set.of(
                        Filter.builder()
                                .name("name")
                                .operation(Operation.builder()
                                        .operator(Operator.IN)
                                        .values(new Object[]{"Heidy", "Ada", "Marie"})
                                        .build())
                                .build()
                ))
                .orderBy(Set.of(OrderBy.builder()
                                .name("age")
                                .direction(Direction.DESC)
                                .build(),
                        OrderBy.builder()
                                .name("name")
                                .direction(Direction.ASC)
                                .build()
                ))
                .page(
                        Page.builder()
                                .number(1)
                                .size(20)
                                .build()
                )
                .build();
        final Map<String, String> aliasMap = Map.of("name", "cc.name");
        final SQLQuery sqlQuery = SqlServerInterpreter.builder("dbo", SUPPORTED_FIELDS,aliasMap).translate(query);

        assertThat(sqlQuery.getQuery()).isEqualTo(SQL_QUERY_2);
    }
    @Test
    void queryWithoutResource() {
        final Query query = Query.builder()
                .columns(Set.of("name", "age"))
                .filters(Set.of(
                        Filter.builder()
                                .name("name")
                                .operation(Operation.builder()
                                        .operator(Operator.IN)
                                        .values(new Object[]{"Heidy", "Ada", "Marie"})
                                        .build())
                                .build()
                ))
                .page(
                        Page.builder()
                                .number(1)
                                .size(20)
                                .build()
                )
                .build();
        final Map<String, String> aliasMap = Map.of("name", "cc.name");
        Assertions.assertThrows(IllegalArgumentException.class, () ->SqlServerInterpreter.builder("dbo", SUPPORTED_FIELDS,aliasMap).translate(query));
    }
    @Test
    void queryWithoutFilters() {
        final Query query = Query.builder()
                .resource("great_women")
                .columns(Set.of("name", "age"))
                .page(
                        Page.builder()
                                .number(1)
                                .size(20)
                                .build()
                )
                .build();
        final Map<String, String> aliasMap = Map.of("name", "cc.name");
        Assertions.assertThrows(IllegalArgumentException.class, () ->SqlServerInterpreter.builder("dbo", SUPPORTED_FIELDS,aliasMap).translate(query));
    }
    @Test
    void queryWithoutPage() {
        final Query query = Query.builder()
                .resource("great_women")
                .columns(Set.of("name", "age"))
                .filters(Set.of(
                        Filter.builder()
                                .name("name")
                                .operation(Operation.builder()
                                        .operator(Operator.IN)
                                        .values(new Object[]{"Heidy", "Ada", "Marie"})
                                        .build())
                                .build()
                ))
                .build();
        final Map<String, String> aliasMap = Map.of("name", "cc.name");
        final SQLQuery sqlQuery = SqlServerInterpreter.builder("dbo", SUPPORTED_FIELDS,aliasMap).translate(query);

        assertThat(sqlQuery.getQuery()).isEqualTo(SQL_QUERY_WITH_DEFAULT_PAGE);
        assertThat(sqlQuery.getArguments().get("OFFSET")).isEqualTo(0);
        assertThat(sqlQuery.getArguments().get("SIZE")).isEqualTo(10);
    }

}