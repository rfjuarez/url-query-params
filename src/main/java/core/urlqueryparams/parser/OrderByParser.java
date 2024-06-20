package core.urlqueryparams.parser;

import core.domain.Direction;
import core.domain.Operation;
import core.domain.OrderBy;
import core.domain.operator.Operator;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Stream;

public class OrderByParser implements OperationParser {
    @Override
    public Operation parser(String urlQueryParams, Function<String, Object> valueParser) {
        final Matcher matcher = OperationPattern.checkPattern(OperationPattern.ORDER_BY_PATTERN, urlQueryParams);
        final String[] directions = matcher.group(1).split(OperationPattern.ORDER_BY_DIRECTION_SPLIT_PATTERN.pattern());
        final OrderBy[] orderBys = Stream.of(directions).map(d -> {
            final Matcher directionMatcher = OperationPattern.checkPattern(OperationPattern.ORDER_BY_DIRECTION_PATTERN, d);
            final Direction direction = Direction.valueOf(directionMatcher.group(1).toUpperCase());
            final String[] fields = directionMatcher.group(2).split(OperationPattern.SEPARATOR);
            return Stream.of(fields)
                    .map(f->OrderBy.builder().direction(direction).name(f).build())
                    .toArray(OrderBy[]::new);
        }).flatMap(Stream::of).toArray(OrderBy[]::new);

        return Operation.builder()
                .operator(Operator.OB)
                .values(orderBys)
                .build();
    }
}
