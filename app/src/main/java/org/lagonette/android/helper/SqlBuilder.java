package org.lagonette.android.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import org.lagonette.android.BuildConfig;

public class SqlBuilder {

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

    // TODO USE StringBuilder
    @NonNull
    private String mSql = "";

    private boolean mAppendColumnComma;

    private boolean mAppendOnComma;

    private boolean mAppendByComma;

    private boolean mAppendFieldComma;

    private boolean mAppendKeyComma;

    public SqlBuilder() {
        build();
    }

    @NonNull
    public SqlBuilder build() {
        mSql = "";
        mAppendFieldComma = false;
        mAppendKeyComma = false;
        mAppendColumnComma = false;
        mAppendOnComma = false;
        mAppendByComma = false;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder createTable(@NonNull String table) {
        mAppendFieldComma = false;
        mSql += CREATE_TABLE + table + BRACKET_OPEN;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder endTable() {
        mSql += BRACKET_CLOSE;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder field(@NonNull String field) {
        appendFieldCommaIfNeeded();
        mSql += field;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder text() {
        mSql += TEXT;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder numeric() {
        mSql += NUMERIC;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder integer() {
        mSql += INTEGER;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder primaryKey() {
        mSql += PRIMARY_KEY;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder setPrimaryKey() {
        appendFieldCommaIfNeeded();
        mAppendKeyComma = false;
        mSql += PRIMARY_KEY + BRACKET_OPEN;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder key(@NonNull String key) {
        appendKeyCommaIfNeeded();
        mSql += key;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder endPrimaryKey() {
        mSql += BRACKET_CLOSE;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder onConflictReplace() {
        mSql += ON_CONFLICT_REPLACE;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder onConflictIgnore() {
        mSql += ON_CONFLICT_IGNORE;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder notNull() {
        mSql += NOT_NULL;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder createViewAs(@NonNull String view) {
        mSql += CREATE_VIEW + view + AS;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder select() {
        mAppendColumnComma = false;
        mSql += SELECT;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder from(@NonNull String table) {
        mSql += FROM + table;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder column(@NonNull String column, @NonNull String as) {
        appendColumnCommaIfNeeded();
        mSql += column + AS + as;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder column(@NonNull String column) {
        appendColumnCommaIfNeeded();
        mSql += column;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder allColumns() {
        appendColumnCommaIfNeeded();
        mSql += ALL_FIELD;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder sum(@NonNull String table, @NonNull String column, @NonNull String as) {
        appendColumnCommaIfNeeded();
        mSql += SUM + BRACKET_OPEN + table + DOT + column + BRACKET_CLOSE + AS + as;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder sum(@NonNull String column, @NonNull String as) {
        appendColumnCommaIfNeeded();
        mSql += SUM + BRACKET_OPEN + column + BRACKET_CLOSE + AS + as;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder count(@NonNull String table, @NonNull String column, @NonNull String as) {
        appendColumnCommaIfNeeded();
        mSql += SUM + BRACKET_OPEN + table + DOT + column + BRACKET_CLOSE + AS + as;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder count(@NonNull String column, @NonNull String as) {
        appendColumnCommaIfNeeded();
        mSql += SUM + BRACKET_OPEN + column + BRACKET_CLOSE + AS + as;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder join(@NonNull String table) {
        mAppendOnComma = false;
        mSql += JOIN + table + ON;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder join(@NonNull String table, @NonNull String as) {
        mAppendOnComma = false;
        mSql += JOIN + table + AS + as + ON;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder leftJoin(@NonNull String table) {
        mAppendOnComma = false;
        mSql += LEFT_JOIN + table + ON;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder leftJoin(@NonNull String table, @NonNull String as) {
        mAppendOnComma = false;
        mSql += LEFT_JOIN + table + AS + as + ON;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder on(@NonNull String fk1, @NonNull String fk2) {
        appendOnCommaIfNeeded();
        mSql += fk1 + " = " + fk2;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder on(@NonNull String table1, @NonNull String fk1, @NonNull String table2, @NonNull String fk2) {
        appendOnCommaIfNeeded();
        mSql += table1 + DOT + fk1 + EQUALS + table2 + DOT + fk2;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder order() {
        mAppendByComma = false;
        mSql += ORDER_BY;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder by(@NonNull String by) {
        appendByCommaIfNeeded();
        mSql += by;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder by(@NonNull String by, boolean asc) {
        appendByCommaIfNeeded();
        mSql += by + (asc ? ASC : DESC);
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder where() {
        mSql += WHERE;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder statement(@NonNull String statement) {
        mSql += statement;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder equals(@NonNull String statement) {
        mSql += EQUALS + statement;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder equals(int statement) {
        return equals(String.valueOf(statement));
    }

    @NonNull
    public SqlBuilder equals(long statement) {
        return equals(String.valueOf(statement));
    }

    @NonNull
    public SqlBuilder statement(@NonNull String table, @NonNull String column) {
        mSql += table + DOT + column;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder like() {
        mSql += LIKE;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder wildcard() {
        mSql += WILDCARD;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder groupBy() {
        mAppendByComma = false;
        mSql += GROUP_BY;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder startGroup() {
        mSql += BRACKET_OPEN;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder endGroup() {
        mSql += BRACKET_CLOSE;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder and() {
        mSql += AND;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder or() {
        mSql += OR;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder isNotNull() {
        mSql += IS_NOT_NULL;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder union() {
        mSql += UNION;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder dropTableIfExists(@NonNull String table) {
        mSql += DROP_TABLE_IF_EXISTS + table;
        return SqlBuilder.this;
    }

    @NonNull
    public SqlBuilder dropViewIfExists(@NonNull String view) {
        mSql += DROP_VIEW_IF_EXISTS + view;
        return SqlBuilder.this;
    }

    @NonNull
    public String toString() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "toString: " + mSql);
        }
        return mSql;
    }

    private void appendColumnCommaIfNeeded() {
        if (mAppendColumnComma) {
            mSql += COMMA;
        } else {
            mAppendColumnComma = true;
        }
    }

    private void appendOnCommaIfNeeded() {
        if (mAppendOnComma) {
            mSql += COMMA;
        } else {
            mAppendOnComma = true;
        }
    }

    private void appendByCommaIfNeeded() {
        if (mAppendByComma) {
            mSql += COMMA;
        } else {
            mAppendByComma = true;
        }
    }

    private void appendFieldCommaIfNeeded() {
        if (mAppendFieldComma) {
            mSql += COMMA;
        } else {
            mAppendFieldComma = true;
        }
    }

    private void appendKeyCommaIfNeeded() {
        if (mAppendKeyComma) {
            mSql += COMMA;
        } else {
            mAppendKeyComma = true;
        }
    }

}
