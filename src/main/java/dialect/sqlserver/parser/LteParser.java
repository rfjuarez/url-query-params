package dialect.sqlserver.parser;

import core.domain.Filter;
import core.domain.SQLQuery;
import util.Preconditions;

import java.util.Map;

import static dialect.sqlserver.parser.OperationsParser.getFieldNameOrAlias;

public class LteParser implements SQLFilterParser {
    @Override
    public SQLQuery parse(final Filter filter, final Map<String, String> aliasMap) {
        Preconditions.nonNullArgument(filter, "Filter cannot be null");
        final String fieldName = getFieldNameOrAlias(filter.getName(), aliasMap);

        return SQLQuery.builder()
                .query(fieldName + " <= :" + filter.getName().toUpperCase())
                .arguments(Map.of(filter.getName().toUpperCase(), filter.getOperation().getValues()[0]))
                .build();
    }
}
