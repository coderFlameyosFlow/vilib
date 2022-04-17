package dev.efnilite.vilib.sql;

import java.sql.SQLException;

/**
 * Error thrown if a statement is invalid
 */
public class InvalidStatementException extends SQLException {

    public InvalidStatementException(String reason) {
        super(reason);
    }
}