package core.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Page {
    Integer number;
    Integer size;
}
