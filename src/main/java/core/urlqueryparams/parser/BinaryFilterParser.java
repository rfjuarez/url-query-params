package core.urlqueryparams.parser;

import core.domain.Operation;
import core.domain.operator.Operator;

import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Matcher;

public class BinaryFilterParser implements OperationParser {
    @Override
    public Operation parser(String urlQueryParams, Function<String, Object> valueParser) {
        final Matcher matcher = OperationPattern.checkPattern(OperationPattern.BINARY_FILTER_PATTERN, urlQueryParams);
        final String[] operands = new String[]{matcher.group(1),matcher.group(2)};
        return Operation.builder()
                .operator(Operator.BTW)
                .values(Arrays.stream(operands).map(valueParser).toArray())
                .build();
    }
}
