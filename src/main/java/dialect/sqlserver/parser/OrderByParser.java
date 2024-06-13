package dialect.sqlserver.parser;

import core.domain.OrderBy;
import core.domain.SQLQuery;
import util.Preconditions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static dialect.sqlserver.parser.OperationsParser.getFieldNameOrAlias;

public class OrderByParser implements SQLOrderByParser {

    @Override
    public SQLQuery parser(final Set<OrderBy> orderBySet, final Map<String, String> aliasMap) {
        Preconditions.nonNullOrEmptyArgument(orderBySet, "OrderBy cannot be null");
        final String orderBy = orderBySet.stream().map(
                        order -> getFieldNameOrAlias(order.getName(), aliasMap) + " " + order.getDirection().name()
                ).sorted().reduce((a, b) -> a + ", " + b)
                .orElse("");

        return SQLQuery.builder()
                .query(" ORDER BY " + orderBy)
                .arguments(new HashMap<>())
                .build();
    }
}
