package core.domain;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class Query {
    String resource;
    Set<String> columns;
    Set<Filter> filters;
    Set<OrderBy> orderBy;
    Page page;
}
