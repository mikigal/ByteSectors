package pl.mikigal.bytesectors.commons.mysql;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StatementSerializable implements Serializable {

    private String sql;
    private Map<Integer, Object> replacements;

    public StatementSerializable(String sql, Object... replacements) {
        this.sql = sql;
        this.replacements = new HashMap<>();

        for (int i = 0; i < replacements.length; i++) {
            this.replacements.put(i + 1, replacements[i]);
        }
    }

    // First index = 1, same as normal PreparedStatement
    public void setReplacement(int index, Object replacement) {
        this.replacements.put(index, replacement);
    }

    public String getSql() {
        return sql;
    }

    public Map<Integer, Object> getReplacements() {
        return replacements;
    }
}
