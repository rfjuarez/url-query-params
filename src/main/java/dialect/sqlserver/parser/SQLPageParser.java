package dialect.sqlserver.parser;

import core.domain.Page;
import core.domain.SQLQuery;

@FunctionalInterface
public interface SQLPageParser {
    SQLQuery parser(final Page page);
}
