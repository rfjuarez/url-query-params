package core.domain;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Filter {
    String name;
    Operation operation;
}
