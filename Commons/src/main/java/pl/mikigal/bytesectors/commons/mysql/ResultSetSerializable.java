package pl.mikigal.bytesectors.commons.mysql;

import java.io.Serializable;

public class ResultSetSerializable implements Serializable {

    private Column[] columns;
    private Row[] rows;

    public ResultSetSerializable(Column[] columns, Row[] rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public Column[] getColumns() {
        return columns;
    }

    public Row[] getRows() {
        return rows;
    }
}
