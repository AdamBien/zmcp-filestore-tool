package airhacks.zmcp.filestore.boundary;

import java.util.Map;

/**
 * MCP tool specification for FileStoreTool.
 * 
 * @see <a href="https://modelcontextprotocol.io/specification/server/tools#listing-tools">MCP Tools Specification</a>
 * @see <a href="https://json-schema.org/draft/2020-12/json-schema-validation#name-if-then-else">JSON Schema Conditional Validation</a>
 */
public interface FileStoreToolSpec {
    
    /**
     * Tool specification following MCP (Model Context Protocol) format.
     * Defines the tool's name, description, and input schema with conditional requirements.
     */
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