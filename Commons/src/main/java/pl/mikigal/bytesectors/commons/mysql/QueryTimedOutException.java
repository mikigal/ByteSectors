package pl.mikigal.bytesectors.commons.mysql;

public class QueryTimedOutException extends RuntimeException {

    public QueryTimedOutException(String message) {
        super(message);
    }
}
