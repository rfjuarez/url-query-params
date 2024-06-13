package dialect.sqlserver;

import core.domain.*;
import dialect.sqlserver.parser.OperationsParser;
import util.Preconditions;

import java.util.*;
import java.util.stream.Collectors;

public class SqlServerInterpreter {

    public static Translator<Query, SQLQuery> builder(final String schema, final Set<String> supportedFields, final Map<String, String> aliasMap) {
        Preconditions.nonNullOrEmptyArgument(supportedFields, "Field supported cannot be null or empty");

        return query -> {
            Preconditions.nonNullArgument(query, "Query cannot be null");
            Preconditions.nonNullOrEmptyArgument(query.getResource(), "Resource cannot be null or empty");
            Preconditions.nonNullOrEmptyArgument(query.getFilters(), "Filters cannot be null or empty");

            final List<SQLQuery> filters = query.getFilters().stream()
                    .filter(filter -> supportedFields.contains(filter.getName()))
                    .map(filter -> OperationsParser.filterParser(filter.getOperation().getOperator()).parse(filter, aliasMap)
                    ).toList();

            final SQLQuery queryFilters = SQLQuery.builder()
                    .query(buildProjection(query.getColumns(), supportedFields, aliasMap) +
                            buildFrom(schema, query.getResource(), aliasMap) +
                            buildWhere(filters))
                    .arguments(filters.stream().map(SQLQuery::getArguments).reduce(new HashMap<>(), (a, b) -> {
                        a.putAll(b);
                        return a;
                    }))
                    .build();
            final SQLQuery queryPage = buildPage(query.getPage());

            queryFilters.getArguments().putAll(queryPage.getArguments());

            final SQLQuery queryOrderBy = buildOrderBy(query.getOrderBy(), supportedFields, aliasMap);

            return SQLQuery.builder()
                    .query(queryFilters.getQuery() + queryOrderBy.getQuery() + queryPage.getQuery())
                    .arguments(queryFilters.getArguments())
                    .build();
        };
    }

    static String buildProjection(final Set<String> columns, final Set<String> supportedFields, final Map<String, String> aliasMap) {
        final Set<String> supportedColumns = Optional.ofNullable(columns).map(
                c -> c.stream()
                        .filter(supportedFields::contains)
                        .collect(Collectors.toSet())
        ).orElse(supportedFields);

        return "SELECT " + String.join(", ", supportedColumns
                .stream()
                .map(c -> OperationsParser.getFieldNameOrAlias(c, aliasMap))
                .sorted()
                .toList());
    }

    static String buildFrom(final String schema, final String resource, final Map<String, String> aliasMap) {
        final String resourceAlias = OperationsParser.getFieldNameOrAlias(resource, aliasMap);

        return " FROM " + Optional.ofNullable(schema).map(
                s -> s + "." + resourceAlias
        ).orElse(resourceAlias);
    }

    static String buildWhere(final List<SQLQuery> filters) {
        return " WHERE " + String.join(" AND ", filters.stream().map(SQLQuery::getQuery).toList());
    }

    static SQLQuery buildOrderBy(final Set<OrderBy> orderBySet, final Set<String> supportedFields, final Map<String, String> aliasMap) {
        if (orderBySet == null || orderBySet.isEmpty()) {
            return SQLQuery.builder().query("").arguments(new HashMap<>()).build();
        }

        final Set<OrderBy> supportedOrderBy = orderBySet.stream()
                .filter(orderBy -> supportedFields.contains(orderBy.getName()))
                .collect(Collectors.toSet());

        return OperationsParser.orderByParser().parser(supportedOrderBy, aliasMap);
    }

    static SQLQuery buildPage(final Page page) {
        final Page pageOrDefault = Optional.ofNullable(page).orElse(Constants.DEFAULT_PAGE);
        Preconditions.check(() ->
                        pageOrDefault.getNumber() >= Constants.DEFAULT_PAGE_NUMBER &&
                                pageOrDefault.getSize() >= Constants.DEFAULT_PAGE_SIZE
                , "Page number cannot be negative");
        return OperationsParser.pageParser().parser(pageOrDefault);
    }
}
