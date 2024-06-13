package core.domain;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class SQLQuery {
    String query;
    Map<String, Object> arguments;
}
