package core.urlqueryparams;

import core.domain.*;
import lombok.NoArgsConstructor;
import util.Preconditions;
import core.domain.Translator;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static core.urlqueryparams.OperationsParser.parser;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class UrlQueryParamsInterpreter {
    public static final Set<String> DEFAULT_PROJECTION = Set.of("*");
    static Page DEFAULT_PAGE = Page.builder().number(0).size(10).build();


    public static Translator<Map<String, String>, Query> builder(
            String resource,
            Map<String, Function<String, Object>> fieldParsers) {
        return urlQueryParams -> {
            Preconditions.nonNullOrEmptyArgument(urlQueryParams, "urlQueryParams cannot be null or empty or contain null values");
            Preconditions.nonNullArgument(fieldParsers, "fieldParsers cannot be null");

            final Set<Filter> argumentQueries = fieldParsers.entrySet().stream()
                    .filter(entry -> !StringTokens.QUERY_NAME_COLUMNS.equals(entry.getKey()) &&
                            !StringTokens.QUERY_NAME_PAGE.equals(entry.getKey()) &&
                            !StringTokens.QUERY_NAME_ORDER_BY.equals(entry.getKey()))
                    .map(entry -> {
                                final String name = entry.getKey();
                                final Function<String, Object> fieldParser = entry.getValue();
                                final String value = urlQueryParams.get(name);
                                final Operation operation = parser(value, fieldParser);
                                return Filter.builder()
                                        .name(name)
                                        .operation(operation)
                                        .build();
                            }
                    ).collect(Collectors.toSet());


            final Set<String> columns = Optional.ofNullable(urlQueryParams.get(StringTokens.QUERY_NAME_COLUMNS))
                    .map(columnsString -> {
                        final Operation operation = OperationsParser.PROJECTION_PARSER.parser(columnsString, null);
                        return Set.of((String[]) operation.getValues());
                    }).orElse(DEFAULT_PROJECTION);


            final Page page = Optional.ofNullable(urlQueryParams.get(StringTokens.QUERY_NAME_PAGE))
                    .map(pageString -> {
                        final Operation operation = OperationsParser.PAGE_PARSER.parser(pageString, null);
                        return Page.builder()
                                .number((Integer) operation.getValues()[0])
                                .size((Integer) operation.getValues()[1])
                                .build();
                    }).orElse(DEFAULT_PAGE);

            final Set<OrderBy> orderBys = Optional.ofNullable(urlQueryParams.get(StringTokens.QUERY_NAME_ORDER_BY))
                    .map(orderByString -> {
                        final Operation operation = OperationsParser.ORDER_BY_PARSER.parser(orderByString, null);
                        return Set.of((OrderBy[]) operation.getValues());
                    }).orElse(new HashSet<>());

            return Query.builder()
                    .resource(resource)
                    .columns(columns)
                    .filters(argumentQueries)
                    .orderBy(orderBys)
                    .page(page)
                    .build();
        };
    }
}
