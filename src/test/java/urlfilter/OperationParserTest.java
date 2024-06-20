package urlfilter;

import core.domain.Direction;
import core.domain.Operation;
import core.domain.OrderBy;
import core.domain.operator.Operator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static core.urlqueryparams.OperationsParser.parser;

class OperationParserTest {
    @Test
    void naryParserTest() {
        final Operation in = Operation.builder()
                .operator(Operator.IN)
                .values(new Object[]{1, 2, 3, 4, 5})
                .build();
        Assertions.assertEquals(in, parser("in(1;2;3;4;5)", Integer::valueOf));

        final Operation in2 = Operation.builder()
                .operator(Operator.IN)
                .values(new Object[]{1, 2, 3})
                .build();
        Assertions.assertEquals(in2, parser("in(1;2;3)", Integer::valueOf));

        final Operation in3 = Operation.builder()
                .operator(Operator.IN)
                .values(new Object[]{"Uno", "Dos", "3"})
                .build();
        Assertions.assertEquals(in3, parser("in(Uno;Dos;3)", String::valueOf));

        final Operation in4 = Operation.builder()
                .operator(Operator.IN)
                .values(new Object[]{1})
                .build();
        Assertions.assertEquals(in4, parser("in(1)", Integer::valueOf));
        final Operation in5 = Operation.builder()
                .operator(Operator.IN)
                .values(new Object[]{1})
                .build();
        Assertions.assertEquals(in5, parser("IN(1)", Integer::valueOf));

        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("in(1,2,3,4,5)", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("in(1;2)(3;4)", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("in(1;2;3;4;5", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("in()", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("in(1;2;3;4;5))", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("in1;2;3;4;5)", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("in((1;2);3;4;5)", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("in((1;2);(3;4;5))", Integer::valueOf));


    }

    @Test
    void unaryParserTest() {
        final Operation eq = Operation.builder()
                .operator(Operator.EQ)
                .values(new Object[]{1})
                .build();
        Assertions.assertEquals(eq, parser("eq(1)", Integer::valueOf));
        final Operation lt = Operation.builder()
                .operator(Operator.LT)
                .values(new Object[]{1})
                .build();
        Assertions.assertEquals(lt, parser("lt(1)", Integer::valueOf));
        final Operation lte = Operation.builder()
                .operator(Operator.LTE)
                .values(new Object[]{1})
                .build();
        Assertions.assertEquals(lte, parser("lte(1)", Integer::valueOf));
        final Operation gt = Operation.builder()
                .operator(Operator.GT)
                .values(new Object[]{1})
                .build();
        Assertions.assertEquals(gt, parser("gt(1)", Integer::valueOf));
        final Operation gte = Operation.builder()
                .operator(Operator.GTE)
                .values(new Object[]{1})
                .build();
        Assertions.assertEquals(gte, parser("gte(1)", Integer::valueOf));

        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("eq(1", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("eq(1))", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("eq1)", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("eq(1,2)", Integer::valueOf));
    }

    @Test
    void binaryParserTest() {
        final Operation btw = Operation.builder()
                .operator(Operator.BTW)
                .values(new Object[]{1, 2})
                .build();
        Assertions.assertEquals(btw, parser("btw(1;2)", Integer::valueOf));

        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("btw(1;2", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("btw(1;2))", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("btw1;2)", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("btw(1,2)", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("btw(1)", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("btw(1;2;3)", Integer::valueOf));

    }

    @Test
    void orderByParserTest() {
        final Operation ob = Operation.builder()
                .operator(Operator.OB)
                .values(new Object[]{OrderBy.builder().name("columnName").direction(Direction.ASC).build()})
                .build();
        Assertions.assertEquals(ob, parser("ob(asc(columnName))", s -> s));

        final Operation ob2 = Operation.builder()
                .operator(Operator.OB)
                .values(new Object[]{OrderBy.builder().name("columnName").direction(Direction.DESC).build()})
                .build();
        Assertions.assertEquals(ob2, parser("ob(desc(columnName))", s -> s));

        final Operation ob3 = Operation.builder()
                .operator(Operator.OB)
                .values(new Object[]{
                        OrderBy.builder()
                                .name("columnName1")
                                .direction(Direction.DESC)
                                .build(),
                        OrderBy.builder()
                                .name("columnName2")
                                .direction(Direction.ASC)
                                .build()
                })
                .build();
        Assertions.assertEquals(ob3, parser("ob(desc(columnName1);asc(columnName2))", s -> s));

        final Operation ob4 = Operation.builder()
                .operator(Operator.OB)
                .values(new Object[]{OrderBy.builder()
                        .name("columnName1")
                        .direction(Direction.DESC)
                        .build(),
                        OrderBy.builder()
                                .name("columnName2")
                                .direction(Direction.DESC)
                                .build(),
                        OrderBy.builder()
                                .name("columnName3")
                                .direction(Direction.ASC)
                                .build()})
                .build();
        Assertions.assertEquals(ob4, parser("ob(desc(columnName1;columnName2);asc(columnName3))", s -> s));

        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("ob(asc(columnName)", s -> s));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("ob(asc(columnName)))", s -> s));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("ob(asc(columnName),desc(columnName2))", s -> s));
    }

    @Test
    void pgParserTest() {
        final Operation pg = Operation.builder()
                .operator(Operator.PG)
                .values(new Object[]{1, 2})
                .build();
        Assertions.assertEquals(pg, parser("pg(1;2)", Integer::valueOf));

        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("pg(1;2", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("pg(1;2))", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("pg1;2)", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("pg(1,2)", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("pg(1)", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("pg(1;2;3)", Integer::valueOf));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("pg((1);(2))", Integer::valueOf));
    }

    @Test
    void projParserTest() {
        final Operation proj1 = Operation.builder()
                .operator(Operator.PROJ)
                .values(new Object[]{"col1"})
                .build();
        Assertions.assertEquals(proj1, parser("proj(col1)", s -> s));

        final Operation proj2 = Operation.builder()
                .operator(Operator.PROJ)
                .values(new Object[]{"col1", "col2"})
                .build();
        Assertions.assertEquals(proj2, parser("proj(col1;col2)", s -> s));

        final Operation proj3 = Operation.builder()
                .operator(Operator.PROJ)
                .values(new Object[]{"col1", "col2", "col3"})
                .build();
        Assertions.assertEquals(proj3, parser("proj(col1;col2;col3)", s -> s));

        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("proj(col1;col2", s -> s));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("proj(col1;col2))", s -> s));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("projcol1;col2)", s -> s));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("proj(col1,col2)", s -> s));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("proj(col1;(col2);col3)", s -> s));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("proj(col1;(col2;col3))", s -> s));
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser("proj()", s -> s));
    }
}