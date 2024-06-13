package core.urlqueryparams.parser;

import core.domain.Operation;
import core.domain.operator.Operator;

import java.util.function.Function;
import java.util.regex.Matcher;

public class LikeFilterParser implements OperationParser {
    @Override
    public Operation parser(String urlQueryParams, Function<String, Object> valueParser) {
        final Matcher matcher = OperationPattern.checkPattern(OperationPattern.LIKE_PATTERN, urlQueryParams);
        final String[] operands = new String[]{matcher.group(1)};

        return Operation.builder()
                .operator(Operator.LK)
                .values(operands)
                .build();
    }
}
