package pl.mikigal.bytesectors.commons.mysql;

import java.io.Serializable;
import java.util.Map;

public class Row implements Serializable {

    private Map<String, Object> data;

    public Row(Map<String, Object> data) {
        this.data = data;
    }

    public Object get(String column) {
        return this.data.get(column);
    }

    public String getString(String column) {
        return (String) this.get(column);
    }

    public boolean getBoolean(String column) {
        return (boolean) this.get(column);
    }

    public byte getByte(String column) {
        return (byte) this.get(column);
    }

    public short getShort(String column) {
        return (short) this.get(column);
    }

    public int getInt(String column) {
        return (int) this.get(column);
    }

    public long getLong(String column) {
        return (long) this.get(column);
    }

    public float getFloat(String column) {
        return (float) this.get(column);
    }

    public double getDouble(String column) {
        return (double) this.get(column);
    }

    public Map<String, Object> getData() {
        return data;
    }
}
