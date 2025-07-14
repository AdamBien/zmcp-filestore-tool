package airhacks.zmcp.filestore.entity;

import java.util.Map;

/**
 * Represents a file operation request with its parameters.
 */
public record FileOperation(
    String type,
    String path,
    String content,
    String storageRoot
) {
    
    public static FileOperation parse(Map<String, Object> input) {
        return new FileOperation(
            (String) input.get("operation"),
            (String) input.get("path"),
            (String) input.get("content"),
            (String) input.get("storageRoot")
        );
    }
    
    public boolean requiresPath() {
        return !Operation.list.name().equals(type);
    }
    
    public boolean requiresContent() {
        return Operation.write.name().equals(type);
    }
}