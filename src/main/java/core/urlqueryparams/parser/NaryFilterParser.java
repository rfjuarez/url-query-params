package core.urlqueryparams.parser;

import core.domain.Operation;
import core.domain.operator.Operator;

import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Matcher;

public class NaryFilterParser implements OperationParser {
    @Override
    public Operation parser(String urlQueryParams, Function<String, Object> valueParser) {
        final Matcher matcher = OperationPattern.checkPattern(OperationPattern.NARY_FILTER_PATTERN, urlQueryParams);
        final String[] operands = matcher.group(1).split(OperationPattern.SEPARATOR);
        return Operation.builder()
                .operator(Operator.IN)
                .values(Arrays.stream(operands).map(valueParser).toArray())
                .build();
    }
}
