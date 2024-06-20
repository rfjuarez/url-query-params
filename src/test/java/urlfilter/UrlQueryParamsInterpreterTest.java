package urlfilter;

import core.domain.*;
import core.domain.operator.Operator;
import core.domain.StringTokens;
import core.urlqueryparams.UrlQueryParamsInterpreter;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UrlQueryParamsInterpreterTest {
    @Test
    void testTranslate() {
        final Map<String, String> input = Map.of(
                StringTokens.QUERY_NAME_COLUMNS, "proj(name;age)",
                "name", "in(Heidy;Ada;Marie)",
                StringTokens.QUERY_NAME_ORDER_BY, "ob(desc(age))",
                StringTokens.QUERY_NAME_PAGE, "pg(1;20)"
        );

        Translator<Map<String, String>, Query> interpreter = UrlQueryParamsInterpreter.builder(
                "great_women",
                Map.of("name", String::valueOf,
                        "age", Integer::valueOf));
        Query query = interpreter.translate(input);

        assertThat(query).extracting(Query::getColumns).isEqualTo(
                Set.of("name", "age")
        );

        assertThat(query).extracting(Query::getFilters).isEqualTo(
                Set.of(
                        Filter.builder()
                                .name("name")
                                .operation(Operation.builder()
                                        .operator(Operator.IN)
                                        .values(new Object[]{"Heidy", "Ada", "Marie"})
                                        .build())
                                .build()
                )
        );
        assertThat(query).extracting(Query::getOrderBy).isEqualTo(
                Set.of(OrderBy.builder()
                        .name("age")
                        .direction(Direction.DESC)
                        .build()
                )
        );
        assertThat(query).extracting(Query::getPage).isEqualTo(
                Page.builder()
                        .number(1)
                        .size(20)
                        .build()
        );
        assertThat(query).extracting(Query::getResource).isEqualTo("great_women");
    }
}