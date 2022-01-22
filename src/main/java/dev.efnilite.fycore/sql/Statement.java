package dev.efnilite.fycore.sql;

public abstract class Statement {

    protected Database database;
    protected String table;

    public Statement(Database database, String table) {
        this.database = database;
        this.table = table;
    }

    public abstract void query() throws InvalidStatementException;
}