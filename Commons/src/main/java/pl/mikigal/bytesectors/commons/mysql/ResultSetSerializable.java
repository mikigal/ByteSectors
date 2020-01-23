package pl.mikigal.bytesectors.commons.mysql;

import java.io.Serializable;
import java.sql.SQLException;

public class ResultSetSerializable implements Serializable {

    private Column[] columns;
    private Row[] rows;
    private SQLException exception;

    public ResultSetSerializable(Column[] columns, Row[] rows, SQLException exception) {
        this.columns = columns;
        this.rows = rows;
        this.exception = exception;
    }

    public Column[] getColumns() {
        return columns;
    }

    public Row[] getRows() {
        return rows;
    }

    public SQLException getException() {
        return exception;
    }
}
