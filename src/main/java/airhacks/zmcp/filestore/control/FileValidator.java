package airhacks.zmcp.filestore.control;

import airhacks.zmcp.filestore.entity.FileOperation;

/**
 * Interface for validating file operations and their parameters.
 */
public interface FileValidator {
    
    void validate(FileOperation operation);
    
    static void validateOperation(FileOperation operation) {
        if (operation.type() == null || operation.type().isBlank()) {
            throw new IllegalArgumentException("Operation type is required");
        }
        
        if (operation.requiresPath() && (operation.path() == null || operation.path().isBlank())) {
            throw new IllegalArgumentException("fileName is required for " + operation.type() + " operation");
        }
        
        if (operation.requiresContent() && operation.content() == null) {
            throw new IllegalArgumentException("Content is required for write operation");
        }
        
        validatePath(operation.path());
    }
    
    static void validatePath(String path) {
        if (path == null) return;
        
        if (path.contains("..")) {
            throw new IllegalArgumentException("Path traversal not allowed");
        }
        
        if (path.startsWith("/")) {
            throw new IllegalArgumentException("Absolute paths not allowed");
        }
    }
}