package core.urlqueryparams;

import core.domain.Operation;
import core.domain.operator.Operator;
import core.urlqueryparams.parser.*;

import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;

import static java.util.Map.entry;

public class OperationsParser {

    public static final OperationParser UNARY_FILTER_PARSER = new UnaryFilterParser();
    public static final OperationParser BINARY_FILTER_PARSER = new BinaryFilterParser();
    public static final OperationParser NARY_FILTER_PARSER = new NaryFilterParser();
    public static final OperationParser LIKE_FILTER_PARSER = new LikeFilterParser();
    public static final OperationParser PROJECTION_PARSER = new ProjectionParser();
    public static final OperationParser PAGE_PARSER = new PageParser();
    public static final OperationParser ORDER_BY_PARSER = new OrderByParser();

    static final Map<Operator, OperationParser> PARSER_STRATEGY = Map.ofEntries(
            entry(Operator.EQ, UNARY_FILTER_PARSER),
            entry(Operator.GT, UNARY_FILTER_PARSER),
            entry(Operator.GTE, UNARY_FILTER_PARSER),
            entry(Operator.LT, UNARY_FILTER_PARSER),
            entry(Operator.LTE, UNARY_FILTER_PARSER),
            entry(Operator.LK, LIKE_FILTER_PARSER),
            entry(Operator.IN, NARY_FILTER_PARSER),
            entry(Operator.BTW, BINARY_FILTER_PARSER),
            entry(Operator.PG, PAGE_PARSER),
            entry(Operator.OB, ORDER_BY_PARSER),
            entry(Operator.PROJ, PROJECTION_PARSER)
    );

    public static Operation parser(String urlQueryParams, Function<String, Object> fieldParser) {
        final Matcher matcher = OperationPattern.checkPattern(OperationPattern.GENERAL_PATTERN, urlQueryParams);
        final Operator operator = Operator.valueOf(matcher.group(1).toUpperCase());

        return PARSER_STRATEGY.get(operator).parser(urlQueryParams, fieldParser);
    }
}
