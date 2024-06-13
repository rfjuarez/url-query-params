package dialect.sqlserver;

import core.domain.Page;

public class Constants {
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final Page DEFAULT_PAGE = Page.builder()
            .number(DEFAULT_PAGE_NUMBER)
            .size(DEFAULT_PAGE_SIZE)
            .build();
}
