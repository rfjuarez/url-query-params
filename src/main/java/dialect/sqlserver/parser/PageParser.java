package dialect.sqlserver.parser;

import core.domain.Page;
import core.domain.SQLQuery;
import util.Preconditions;

import java.util.Map;

public class PageParser implements SQLPageParser {


    @Override
    public SQLQuery parser(final Page page) {
        Preconditions.nonNullArgument(page, "Page cannot be null");

        return SQLQuery.builder()
                .query(" OFFSET :OFFSET ROWS FETCH NEXT :SIZE ROWS ONLY")
                .arguments(Map.of("OFFSET", page.getNumber() * page.getSize(),
                        "SIZE", page.getSize()))
                .build();
    }
}
