package core.urlqueryparams.parser;

import core.domain.Operation;
import core.domain.operator.Operator;

import java.util.function.Function;
import java.util.regex.Matcher;

public class UnaryFilterParser implements OperationParser {
    @Override
    public Operation parser(String urlQueryParams, Function<String, Object> valueParser) {
        final Matcher matcher = OperationPattern.checkPattern(OperationPattern.UNARY_FILTER_PATTERN, urlQueryParams);
        final Operator operator= Operator.valueOf(matcher.group(1).toUpperCase());
        final String operands = matcher.group(2);

        return Operation.builder()
                .operator(operator)
                .values(new Object[]{valueParser.apply(operands)})
                .build();
    }
}
