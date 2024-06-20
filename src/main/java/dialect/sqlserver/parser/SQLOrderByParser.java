package dialect.sqlserver.parser;

import core.domain.OrderBy;
import core.domain.SQLQuery;

import java.util.Map;
import java.util.Set;

@FunctionalInterface
public interface SQLOrderByParser {
    SQLQuery parser(final Set<OrderBy> orderBySet, final Map<String, String> aliasMap);
}
