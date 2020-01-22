package pl.mikigal.bytesectors.commons.mysql;

import java.io.Serializable;

public class Column implements Serializable {

    private int index;
    private String name;
    private String type;
    private boolean autoIncrement;
    private boolean nullable;

    public Column(int index, String name, String type, boolean autoIncrement, boolean nullable) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.autoIncrement = autoIncrement;
        this.nullable = nullable;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public boolean isNullable() {
        return nullable;
    }
}
