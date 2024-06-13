package core.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderBy {
    String name;
    Direction direction;
}
