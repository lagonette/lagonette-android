package org.lagonette.android.helper;

public class Sql {

    private static final String TAG = "SqlBuilder";

    private static final String CREATE_TABLE = "CREATE TABLE ";

    private static final String CREATE_VIEW = "CREATE VIEW ";

    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";

    private static final String DROP_VIEW_IF_EXISTS = "DROP VIEW IF EXISTS ";

    private static final String BRACKET_OPEN = " (";

    private static final String BRACKET_CLOSE = ")";

    private static final String TEXT = " TEXT";

    private static final String NUMERIC = " NUMERIC";

    private static final String INTEGER = " INTEGER";

    private static final String NOT_NULL = " NOT NULL";

    private static final String PRIMARY_KEY = " PRIMARY KEY";

    private static final String ON_CONFLICT_REPLACE = " ON CONFLICT REPLACE";

    private static final String ON_CONFLICT_IGNORE = " ON CONFLICT IGNORE";

    private static final String SELECT = "SELECT ";

    private static final String COMMA = ", ";

    private static final String AS = " AS ";

    private static final String FROM = " FROM ";

    private static final String JOIN = " JOIN ";

    private static final String LEFT_JOIN = " LEFT JOIN ";

    private static final String ON = " ON ";

    private static final String ORDER_BY = " ORDER BY ";

    private static final String WHERE = " WHERE ";

    private static final String AND = " AND ";

    private static final String OR = " OR ";

    private static final String IS_NOT_NULL = " IS NOT NULL";

    private static final String LIKE = " LIKE ";

    private static final String WILDCARD = "?";

    private static final String GROUP_BY = " GROUP BY ";

    private static final String UNION = " UNION ";

    private static final String ASC = " ASC";

    private static final String DESC = " DESC";

    private static final String ALL_FIELD = "*";

    private static final String SUM = "SUM";

    private static final String COUNT = "COUNT";

    private static final String EQUALS = " = ";

    private static final String DOT = ".";

}
