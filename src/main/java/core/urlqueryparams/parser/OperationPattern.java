package core.urlqueryparams.parser;

import core.exception.IllegalUrlQuerySyntax;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class OperationPattern {
    private static final String ILLEGAL_URL_QUERY_SYNTAX = "Illegal operation syntax";
    public static final String SEPARATOR = ";";
    public static final Pattern GENERAL_PATTERN = Pattern.compile("(?i)^(eq|lk|lte|lt|gte|gt|in|btw|proj|ob|pg)\\(([a-zA-Z0-9_*?()]+(?:;[a-zA-Z0-9_*?()]+)*)\\)$");
    public static final Pattern UNARY_FILTER_PATTERN = Pattern.compile("(?i)^(eq|lte|lt|gte|gt)\\(([a-zA-Z0-9_]+[a-zA-Z0-9_]*)\\)$");
    public static final Pattern BINARY_FILTER_PATTERN = Pattern.compile("(?i)^btw\\(([a-zA-Z0-9_]+[a-zA-Z0-9_]*);([a-zA-Z0-9_]+[a-zA-Z0-9_]*)\\)$");
    public static final Pattern NARY_FILTER_PATTERN = Pattern.compile("(?i)^in\\(([a-zA-Z0-9_]+(?:;[a-zA-Z0-9_]+)*)\\)$");
    public static final Pattern PAGE_PATTERN = Pattern.compile("(?i)^pg\\(([0-9]+[0-9]*);([0-9]+[0-9]*)\\)$");
    public static final Pattern PROJECTION_PATTERN = Pattern.compile("(?i)^proj\\(([a-zA-Z0-9_]+(?:;[a-zA-Z0-9_]+)*)\\)$");
    public static final Pattern ORDER_BY_PATTERN = Pattern.compile("(?i)^ob\\(((asc|desc)\\(([a-zA-Z0-9_]+(?:;[a-zA-Z0-9_]+)*)\\)(?:;(asc|desc)\\(([a-zA-Z0-9_]+(?:;[a-zA-Z0-9_]+)*)\\))*)\\)$");
    public static final Pattern ORDER_BY_DIRECTION_PATTERN = Pattern.compile("(?i)^(asc|desc)\\(([a-zA-Z0-9_]+(?:;[a-zA-Z0-9_]+)*)\\)$");
    public static final Pattern LIKE_PATTERN = Pattern.compile("(?i)^lk\\('[a-zA-Z0-9_*?]+'\\)$");
    public static final Pattern ORDER_BY_DIRECTION_SPLIT_PATTERN = Pattern.compile(";(?=(asc|desc)\\()");

    public static Matcher checkPattern(Pattern pattern, String input) {
        final Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalUrlQuerySyntax(ILLEGAL_URL_QUERY_SYNTAX);
        }
        return matcher;
    }
}
