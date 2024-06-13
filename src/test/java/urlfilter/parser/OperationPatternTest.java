package urlfilter.parser;

import core.urlqueryparams.parser.OperationPattern;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperationPatternTest {

    @Test
    void testOrderByPattern() {
        assertTrue(OperationPattern.ORDER_BY_PATTERN.matcher("ob(asc(col1))").matches());
        assertTrue(OperationPattern.ORDER_BY_PATTERN.matcher("ob(asc(col1;col2))").matches());
        assertTrue(OperationPattern.ORDER_BY_PATTERN.matcher("ob(asc(col1;col2);desc(col3))").matches());
        assertTrue(OperationPattern.ORDER_BY_PATTERN.matcher("ob(asc(col1;col2;col3);desc(col4;col5))").matches());
        assertTrue(OperationPattern.ORDER_BY_PATTERN.matcher("ob(asc(col4;col5);desc(col1;col2;col3))").matches());
        assertTrue(OperationPattern.ORDER_BY_PATTERN.matcher("ob(desc(col4;col5))").matches());
        assertTrue(OperationPattern.ORDER_BY_PATTERN.matcher("ob(desc(col4;col5);asc(col1;col2;col3))").matches());
        assertTrue(OperationPattern.ORDER_BY_PATTERN.matcher("ob(desc(col4;col5);asc(col1;col2;col3);asc(col6))").matches());
    }

    @Test
    void testOrderByDirectionPattern() {
        assertTrue(OperationPattern.ORDER_BY_DIRECTION_PATTERN.matcher("asc(col1)").matches());
        assertTrue(OperationPattern.ORDER_BY_DIRECTION_PATTERN.matcher("asc(col1;col2)").matches());
        assertTrue(OperationPattern.ORDER_BY_DIRECTION_PATTERN.matcher("asc(col1;col2;col3)").matches());
        assertTrue(OperationPattern.ORDER_BY_DIRECTION_PATTERN.matcher("desc(col1)").matches());
        assertTrue(OperationPattern.ORDER_BY_DIRECTION_PATTERN.matcher("desc(col1;col2)").matches());
        assertTrue(OperationPattern.ORDER_BY_DIRECTION_PATTERN.matcher("desc(col1;col2;col3)").matches());
    }

    @Test
    void testLikePattern() {
        assertTrue(OperationPattern.LIKE_PATTERN.matcher("lk('*str')").matches());
        assertTrue(OperationPattern.LIKE_PATTERN.matcher("lk('str*')").matches());
        assertTrue(OperationPattern.LIKE_PATTERN.matcher("lk('str?')").matches());
        assertTrue(OperationPattern.LIKE_PATTERN.matcher("lk('str??')").matches());
        assertTrue(OperationPattern.LIKE_PATTERN.matcher("lk('?str?')").matches());
        assertTrue(OperationPattern.LIKE_PATTERN.matcher("lk('??str')").matches());
        assertTrue(OperationPattern.LIKE_PATTERN.matcher("lk('str??str')").matches());
        assertTrue(OperationPattern.LIKE_PATTERN.matcher("lk('str?str')").matches());
        assertTrue(OperationPattern.LIKE_PATTERN.matcher("lk('str*str')").matches());

        assertFalse(OperationPattern.LIKE_PATTERN.matcher("lk('str*str)").matches());
        assertFalse(OperationPattern.LIKE_PATTERN.matcher("lk(str*str')").matches());
        assertFalse(OperationPattern.LIKE_PATTERN.matcher("lk(str*str)").matches());
        assertFalse(OperationPattern.LIKE_PATTERN.matcher("lk(str)").matches());
        assertFalse(OperationPattern.LIKE_PATTERN.matcher("lk(str'str)").matches());

    }

}