package core.urlqueryparams;

import core.domain.*;
import lombok.NoArgsConstructor;
import util.Preconditions;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static core.urlqueryparams.OperationsParser.parser;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class UrlQueryParamsInterpreter {
    public static final Set<String> DEFAULT_PROJECTION = Set.of("*");
    static Page DEFAULT_PAGE = Page.builder().number(0).size(10).build();

    /**
     * @param resource     name of the resource
     * @param fieldParsers map of supported field to be parser, only the fields in the map will be parsed
     *                     and considered in the query
     * @return a translator that will convert a map of url query params to a Query object
     */

    public static Translator<Map<String, String>, Query> builder(
            final String resource,
            final Map<String, Function<String, Object>> fieldParsers
    ) {
        return urlQueryParams -> {
            Preconditions.nonNullOrEmptyArgument(resource, "Resource cannot be null or empty");
            Preconditions.nonNullOrEmptyArgument(fieldParsers, "fieldParsers cannot be null or empty");
            Preconditions.nonNullOrEmptyArgument(urlQueryParams, "urlQueryParams cannot be null or empty or contain null values");

            final Set<String> columns = getProjection(fieldParsers, urlQueryParams);
            final Set<Filter> fieldQueries = getFilters(fieldParsers, urlQueryParams);
            final Page page = getPageOrElse(urlQueryParams,DEFAULT_PAGE);
            final Set<OrderBy> orderBys = getOrderBys(fieldParsers, urlQueryParams);

            return Query.builder()
                    .resource(resource)
                    .columns(columns)
                    .filters(fieldQueries)
                    .orderBy(orderBys)
                    .page(page)
                    .build();
        };
    }

    static Set<Filter> getFilters(Map<String, Function<String, Object>> fieldParsers, Map<String, String> urlQueryParams) {
        return fieldParsers.entrySet().stream()
                .filter(entry -> urlQueryParams.containsKey(entry.getKey()))
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
    }

    private static Set<String> getProjection(Map<String, Function<String, Object>> fieldParsers, Map<String, String> urlQueryParams) {
        return Optional.ofNullable(urlQueryParams.get(StringTokens.QUERY_NAME_COLUMNS))
                .map(projectionQuery -> {
                    final Operation operation = OperationsParser.PROJECTION_PARSER.parser(projectionQuery, null);
                    return Set.of(Arrays
                            .stream(operation.getValues())
                            .map(String::valueOf)
                            .filter(fieldParsers::containsKey)
                            .toArray(String[]::new));
                }).orElse(fieldParsers.keySet());
    }

    static Page getPageOrElse(final Map<String, String> urlQueryParams, final Page orElse) {
        return Optional.ofNullable(urlQueryParams.get(StringTokens.QUERY_NAME_PAGE))
                .map(pageQuery -> {
                    final Operation operation = OperationsParser.PAGE_PARSER.parser(pageQuery, null);
                    return Page.builder()
                            .number((Integer) operation.getValues()[0])
                            .size((Integer) operation.getValues()[1])
                            .build();
                }).orElse(orElse);
    }

    static Set<OrderBy> getOrderBys(final Map<String, Function<String, Object>> fieldParsers, final Map<String, String> urlQueryParams) {
        return Optional.ofNullable(urlQueryParams.get(StringTokens.QUERY_NAME_ORDER_BY))
                .map(orderByQuery -> {
                    final Operation operation = OperationsParser.ORDER_BY_PARSER.parser(orderByQuery, null);
                    return Set.of(Arrays.stream((OrderBy[]) operation.getValues())
                            .filter(ob -> fieldParsers.containsKey(ob.getName()))
                            .toArray(OrderBy[]::new)
                    );
                }).orElse(new HashSet<>());
    }
}
