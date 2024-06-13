package core.domain.operator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Operator {
    EQ(Arity.UNARY),
    LK(Arity.UNARY),
    LTE(Arity.UNARY),
    LT(Arity.UNARY),
    GTE(Arity.UNARY),
    GT(Arity.UNARY),
    IN(Arity.NARY),
    BTW(Arity.BINARY),
    OB(Arity.UNARY),
    PROJ(Arity.NARY),
    PG(Arity.BINARY);
    private final Arity arity;
}
