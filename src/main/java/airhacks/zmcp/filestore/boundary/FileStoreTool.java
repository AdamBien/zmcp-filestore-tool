package airhacks.zmcp.filestore.boundary;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import airhacks.zmcp.filestore.control.Configuration;
import airhacks.zmcp.filestore.control.FileSystemOperations;
import airhacks.zmcp.filestore.control.FileValidator;
import airhacks.zmcp.filestore.entity.FileOperation;

/**
 * ZMCP (https://github.com/AdamBien/zmcp) tool for local file storage operations. Provides read, write,
 * list, and delete capabilities for files in a configured directory.
 */
public class FileStoreTool implements Function<Map<String, Object>, Map<String, String>> {
    static Path STORAGE_ROOT = Path.of(Configuration.getStorageRoot());
    
    public static final Map<String, Object> TOOL_SPEC = Map.of(
        "name", "FileStoreTool",
        "description", "Local file storage operations - read, write, list, and delete files",
        "inputSchema", """
            {
                "type": "object",
                "properties": {
                    "operation": {
                        "type": "string",
                        "description": "Operation to perform: read, write, list, or delete",
                        "enum": ["read", "write", "list", "delete"]
                    },
                    "fileName": {
                        "type": "string",
                        "description": "Name of the file to operate on (required for read, write, and delete operations)"
                    },
                    "content": {
                        "type": "string",
                        "description": "File content for write operations (required for write operation)"
                    }
                },
                "required": ["operation"],
                "additionalProperties": false
            }
            """
    );
    
    @Override
    public Map<String, String> apply(Map<String, Object> input) {
        try {
            var enrichedInput = new HashMap<>(input);
            enrichedInput.put("storageRoot", STORAGE_ROOT.toString());
            
            if (enrichedInput.containsKey("fileName")) {
                enrichedInput.put("path", enrichedInput.get("fileName"));
            }
            
            var operation = FileOperation.parse(enrichedInput);
            FileValidator.validateOperation(operation);
            
            return switch (operation.type()) {
                case "read" -> FileSystemOperations.read(operation);
                case "write" -> FileSystemOperations.write(operation);
                case "list" -> FileSystemOperations.list(operation);
                case "delete" -> FileSystemOperations.delete(operation);
                default -> Map.of("error", "Unknown operation: " + operation.type());
            };
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }
}