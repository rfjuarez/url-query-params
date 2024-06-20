# url-query-params
Utility for translate URL search parameters to SQL a query.
Given a URL search parameters as Map<String,String>, it returns a SQL query string.
Where the key is the column name and the value is a kind of filter function.
The filter function can be a simple value, a range or a list of values.

List of supported filter operations:
- eq: equal
- lk: like a value
- gt: greater than
- gte: greater or equal
- lt: less than
- lte: less or equal
- in: in a list of values
- btw: between two values

Other query operations supported:
- pg: page by current page and size,
- ob: order by columns and directions.
- proj: projection of set columns name.


Reserved words and characters:
- Reserved words: eq, lk, gt, gte, lt, lte, in, btw, pg, ob, proj
- __columns__: set of column name, used by proj operation
- __page__: used by pg operation
- __order_by__: used by ob operation

Example:
````java
        /*
        Given a URL like this:
        https://somehost/great_women?name=in(Heidy;Ada;Marie)&age=gt(20)&" +
                "__columns__=proj(name;age)&" +
                "__order_by__=ob(desc(age))
        */
        //The mapping expected would be:
        final Map<String, String> urlQueryParams = Map.of(
                "name", "in(Heidy;Ada;Marie)",
                "age", "gt(20)",
                "__columns__", "proj(name;age)",
                "__order_by__", "ob(desc(age))"
        );
        //Map for specify a set of field supported and its function to parser String to Object:
        final Map<String, Function<String, Object>> fieldParsers = Map.of(
                "name", String::valueOf,
                "age", Integer::parseInt
        );
        //Translate the URL search parameters to agnostic query structure.
        final Query query = UrlQueryParamsInterpreter.builder("great_women", fieldParsers)
                .translate(urlQueryParams);
        
        //Translate the agnostic query structure to SQL query string.
        final SQLQuery sqlQuery = SqlServerInterpreter.builder("dbo", Map.of()).translate(query);
        
        //The SQL query string expected is:
        final static String SQL_QUERY = "SELECT age, name" +
                " FROM dbo.great_women" +
                " WHERE age > :AGE AND" +
                " name IN (:NAME)" +
                " ORDER BY age DESC" +
                " OFFSET :OFFSET ROWS FETCH NEXT :SIZE ROWS ONLY";
        
        //Where the values of parameters are:
        final Map<String, Object> params = Map.of(
                "AGE", 20,
                "NAME", new Object[]{"Heidy", "Ada", "Marie"},
                "OFFSET", 0,
                "SIZE", 10
        );

````
Note: The SQLQuery object contains the SQL query string and the parameters values to avoid SQL Injection.

## Disclaimer
This project is under development and it is not ready for production.