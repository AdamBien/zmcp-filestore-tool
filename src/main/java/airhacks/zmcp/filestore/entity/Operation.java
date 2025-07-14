package airhacks.zmcp.filestore.entity;

/**
 * Supported file storage operations.
 */
public enum Operation {
    read,
    write,
    list,
    delete;
    
    public static Operation fromString(String value) {
        try {
            return Operation.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown operation: " + value);
        }
    }
}