package org.lagonette.app.room.sql;

public interface Sql {

	String CREATE_TABLE = "CREATE TABLE ";

	String CREATE_VIEW = "CREATE VIEW ";

	String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";

	String DROP_VIEW_IF_EXISTS = "DROP VIEW IF EXISTS ";

	String BRACKET_OPEN = " (";

	String BRACKET_CLOSE = ")";

	String TEXT = " TEXT";

	String NUMERIC = " NUMERIC";

	String INTEGER = " INTEGER";

	String NOT_NULL = " NOT NULL";

	String PRIMARY_KEY = " PRIMARY KEY";

	String ON_CONFLICT_REPLACE = " ON CONFLICT REPLACE";

	String ON_CONFLICT_IGNORE = " ON CONFLICT IGNORE";

	String SELECT = "SELECT ";

	String COMMA = ", ";

	String AS = " AS ";

	String FROM = " FROM ";

	String JOIN = " JOIN ";

	String LEFT = " LEFT";

	String ON = " ON ";

	String ORDER_BY = " ORDER BY ";

	String WHERE = " WHERE ";

	String AND = " AND ";

	String OR = " OR ";

	String IS_NOT_NULL = " IS NOT NULL";

	String LIKE = " LIKE ";

	String WILDCARD = "?";

	String GROUP_BY = " GROUP BY ";

	String UNION = " UNION ";

	String ASC = " ASC";

	String DESC = " DESC";

	String ALL_FIELD = "*";

	String SUM = "SUM";

	String COUNT = "COUNT";

	String EQUALS = " = ";

	String DOT = ".";

	String SPACE = " ";

}
