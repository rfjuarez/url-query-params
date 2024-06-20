package core.domain;

import core.domain.operator.Operator;
import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class Operation {
    Operator operator;
    Object[] values;
}
