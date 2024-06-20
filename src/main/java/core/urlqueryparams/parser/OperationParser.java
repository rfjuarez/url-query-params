package core.urlqueryparams.parser;

import core.domain.Operation;

import java.util.function.Function;

@FunctionalInterface
public interface OperationParser {
    Operation parser(String urlQueryParams, Function<String, Object> typeParser);
}
