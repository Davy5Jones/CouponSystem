package Helper;

import java.util.Arrays;

public class QueryBuilder {

    String query;

    private QueryBuilder(String init) {
        query = init;
    }

    private void stringAdder(String str) {
        query += " " + str;
    }

    public static QueryBuilder select(String... columns) {
        String joinedColumns = String.join(",", columns);
        return new QueryBuilder("select " + joinedColumns);
    }

    public static QueryBuilder delete() {
        return new QueryBuilder("delete");
    }

    public QueryBuilder exists(String str) {
        stringAdder(String.format("(%s)", str));
        return this;
    }

    public QueryBuilder from(String table) {
        stringAdder("from");
        stringAdder(table);
        return this;
    }

    public QueryBuilder where(String column, String operator, String variable) {
        stringAdder("where");
        stringAdder(column);
        stringAdder(operator);
        stringAdder(variable);
        return this;
    }

    public QueryBuilder and() {
        stringAdder("and");
        return this;
    }

    public static QueryBuilder insertInto(String table, String[] vars) {
        QueryBuilder queryBuilder = new QueryBuilder("insert into " + table);

        String joinedVars = String.join(",", vars);

        queryBuilder.stringAdder(String.format("(%s)", joinedVars));
        return queryBuilder;
    }

    public QueryBuilder values(String[] values) {
        String joinedVars = String.join(",", values);

        stringAdder(String.format("values (%s)", joinedVars));
        return this;
    }

    /**
     * -1 for no limit
     */
    public QueryBuilder count(int limit) {
        if (limit == -1) {
            stringAdder("count");
            return this;
        }
        stringAdder(String.format("count(%d)", limit));
        return this;
    }

    public QueryBuilder as(String alias) {
        stringAdder(String.format("as %s", alias));
        return this;
    }

    public static QueryBuilder update(String table) {
        return new QueryBuilder("update " + table);
    }

    public QueryBuilder set(String[] columns, String[] values) {
        StringBuilder builder = new StringBuilder();
        Arrays.setAll(columns, i -> columns[i].concat("=" + values[i]));

        stringAdder(String.join(",", columns));
        return this;
    }

    public String getQuery() {
        return query;
    }
}
