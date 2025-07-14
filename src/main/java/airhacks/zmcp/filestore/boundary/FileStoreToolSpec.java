package airhacks.zmcp.filestore.boundary;

import java.util.Map;

/**
 * MCP tool specification for FileStoreTool.
 * https://modelcontextprotocol.io/specification/2025-06-18/server/tools#listing-tools
 */
public interface FileStoreToolSpec {
    
    Map<String, Object> SPEC = Map.of(
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
                        "description": "Name of the file to operate on"
                    },
                    "content": {
                        "type": "string",
                        "description": "File content for write operations"
                    }
                },
                "required": ["operation"],
                "additionalProperties": false,
                "allOf": [
                    {
                        "if": {
                            "properties": {
                                "operation": { "enum": ["read", "write", "delete"] }
                            }
                        },
                        "then": {
                            "required": ["fileName"]
                        }
                    },
                    {
                        "if": {
                            "properties": {
                                "operation": { "const": "write" }
                            }
                        },
                        "then": {
                            "required": ["content"]
                        }
                    }
                ]
            }
            """
    );
}