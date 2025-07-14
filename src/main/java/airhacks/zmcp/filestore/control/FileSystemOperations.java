package airhacks.zmcp.filestore.control;

import airhacks.zmcp.filestore.entity.FileOperation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * File system implementation of file operations.
 */
public interface FileSystemOperations {
    
    static Map<String, String> read(FileOperation operation) {
        try {
            var filePath = resolvePath(operation.storageRoot(), operation.path());
            
            if (!Files.exists(filePath)) {
                return Map.of("error", "File not found: " + operation.path());
            }
            
            var content = Files.readString(filePath);
            return Map.of("content", content);
        } catch (IOException e) {
            return Map.of("error", "Failed to read file: " + e.getMessage());
        }
    }
    
    static Map<String, String> write(FileOperation operation) {
        try {
            var filePath = resolvePath(operation.storageRoot(), operation.path());
            Files.createDirectories(filePath.getParent());
            Files.writeString(filePath, operation.content());
            
            return Map.of("content", "File written successfully: " + operation.path());
        } catch (IOException e) {
            return Map.of("error", "Failed to write file: " + e.getMessage());
        }
    }
    
    static Map<String, String> list(FileOperation operation) {
        try {
            var rootPath = Path.of(operation.storageRoot());
            
            if (!Files.exists(rootPath)) {
                return Map.of("content", "No files found");
            }
            
            try (var paths = Files.walk(rootPath)) {
                var files = paths
                    .filter(Files::isRegularFile)
                    .map(rootPath::relativize)
                    .map(Path::toString)
                    .collect(Collectors.joining("\n"));
                
                return Map.of("content", files.isEmpty() ? "No files found" : files);
            }
        } catch (IOException e) {
            return Map.of("error", "Failed to list files: " + e.getMessage());
        }
    }
    
    static Map<String, String> delete(FileOperation operation) {
        try {
            var filePath = resolvePath(operation.storageRoot(), operation.path());
            
            if (Files.deleteIfExists(filePath)) {
                return Map.of("content", "File deleted successfully: " + operation.path());
            } else {
                return Map.of("error", "File not found: " + operation.path());
            }
        } catch (IOException e) {
            return Map.of("error", "Failed to delete file: " + e.getMessage());
        }
    }
    
    static Path resolvePath(String storageRoot, String relativePath) {
        return Path.of(storageRoot).resolve(relativePath).normalize();
    }
}