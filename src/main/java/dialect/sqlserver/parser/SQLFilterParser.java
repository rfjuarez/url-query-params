package dialect.sqlserver.parser;

import core.domain.Filter;
import core.domain.SQLQuery;

import java.util.Map;

@FunctionalInterface
public interface SQLFilterParser {
    SQLQuery parse(final Filter filter, final Map<String, String> aliasMap);
}
