import core.domain.Query;
import core.domain.SQLQuery;
import core.urlqueryparams.UrlQueryParamsInterpreter;
import dialect.sqlserver.Constants;
import dialect.sqlserver.SqlServerInterpreter;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class UseCaseTest {
    final static String SQL_QUERY = "SELECT age, name" +
            " FROM dbo.great_women" +
            " WHERE age > :AGE AND" +
            " name IN (:NAME)" +
            " ORDER BY age DESC" +
            " OFFSET :OFFSET ROWS FETCH NEXT :SIZE ROWS ONLY";

    @Test
    void fromUrlToQuery() {
        //
        /*
        Given a URL like:
        https://somehost/great_women?name=in(Heidy;Ada;Marie)&age=gt(20)&" +
                "__columns__=prj(name;age)&" +
                "__order_by__=ob(desc(age))
        */
        //The mapping to a Map<String, String> would be:
        final Map<String, String> urlQueryParams= Map.of(
                "name", "in(Heidy;Ada;Marie)",
                "age", "gt(20)",
                "__columns__", "proj(name;age)",
                "__order_by__", "ob(desc(age))"
        );
        //The Query object would be:
        final Map<String, Function<String, Object>> fieldParsers = Map.of(
                "name", String::valueOf,
                "age", Integer::parseInt
        );
        final Query query = UrlQueryParamsInterpreter.builder("great_women",fieldParsers)
                .translate(urlQueryParams);
        final Set<String> supportedFields = Set.of("name", "age");

        final SQLQuery sqlQuery = SqlServerInterpreter.builder("dbo", supportedFields,Map.of()).translate(query);

        assertThat(sqlQuery.getQuery()).isEqualTo(SQL_QUERY);
        assertThat(sqlQuery.getArguments()).containsEntry("AGE", 20);
        assertThat(sqlQuery.getArguments()).containsEntry("NAME", new Object[]{"Heidy", "Ada", "Marie"});
        assertThat(sqlQuery.getArguments()).containsEntry("OFFSET", Constants.DEFAULT_PAGE_NUMBER);
        assertThat(sqlQuery.getArguments()).containsEntry("SIZE", Constants.DEFAULT_PAGE_SIZE);
    }
}
