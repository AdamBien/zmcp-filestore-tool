package airhacks.zmcp.filestore.boundary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.*;

class InputSchemaValidationTest {
    
    @TempDir
    Path tempDir;
    
    FileStoreTool tool;
    String storageRoot;
    
    @BeforeEach
    void setup() {
        tool = new FileStoreTool();
        storageRoot = tempDir.toString();
    }
    
    @Test
    void schemaIsValidJsonString() {
        var schemaJson = (String) FileStoreTool.TOOL_SPEC.get("inputSchema");
        
        assertThat(schemaJson).isNotNull();
        assertThat(schemaJson).isNotBlank();
        
        // Validate it's proper JSON format
        assertThat(schemaJson.trim()).startsWith("{").endsWith("}");
    }
    
    @Test
    void schemaContainsFileName() {
        var schemaJson = (String) FileStoreTool.TOOL_SPEC.get("inputSchema");
        
        assertThat(schemaJson)
            .contains("\"fileName\"")
            .contains("\"type\": \"string\"");
    }
    
    
    @Test
    void schemaDisallowsAdditionalProperties() {
        var schemaJson = (String) FileStoreTool.TOOL_SPEC.get("inputSchema");
        
        assertThat(schemaJson).contains("\"additionalProperties\": false");
    }
    
    @Test
    void schemaDefinesOperationEnum() {
        var schemaJson = (String) FileStoreTool.TOOL_SPEC.get("inputSchema");
        
        assertThat(schemaJson)
            .contains("\"enum\": [\"read\", \"write\", \"list\", \"delete\"]");
    }
    
    @Test
    void schemaDefinesRequiredFields() {
        var schemaJson = (String) FileStoreTool.TOOL_SPEC.get("inputSchema");
        
        assertThat(schemaJson)
            .contains("\"required\": [\"operation\"]");
    }
}