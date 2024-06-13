package dialect.sqlserver.parser;

import core.domain.operator.Operator;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class OperationsParser {


    static final Map<Operator, SQLFilterParser> FILTER_PARSER_STRATEGY =
            Map.of(Operator.IN, new InParser(),
                    Operator.EQ, new EqParser(),
                    Operator.LTE, new LteParser(),
                    Operator.LT, new LtParser(),
                    Operator.GTE, new GteParser(),
                    Operator.GT, new GtParser(),
                    Operator.BTW, new BtwParser()
            );

    public static final SQLPageParser PAGE_PARSER = new PageParser();
    public static final SQLOrderByParser ORDER_BY_PARSER = new OrderByParser();

    public static SQLFilterParser filterParser(Operator operator) {
        return FILTER_PARSER_STRATEGY.get(operator);
    }

    public static SQLPageParser pageParser() {
        return PAGE_PARSER;
    }

    public static SQLOrderByParser orderByParser() {
        return ORDER_BY_PARSER;
    }

    public static String getFieldNameOrAlias(final String fieldName, final Map<String, String> aliasMap) {
        return Optional.ofNullable(aliasMap)
                .map(m -> m.getOrDefault(fieldName, fieldName))
                .orElse(fieldName);
    }
}
