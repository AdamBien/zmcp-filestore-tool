package airhacks.zmcp.filestore.boundary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class FileStoreToolTest {
    
    @TempDir
    Path tempDir;
    
    FileStoreTool tool;
    String storageRoot;
    
    @BeforeEach
    void setup() {
        tool = new FileStoreTool();
        storageRoot = tempDir.toString();
        // Set the storage root to use temp directory for all tests
        FileStoreTool.STORAGE_ROOT = tempDir;
    }
    
    @Test
    void writeAndReadFile() throws Exception {
        var writeInput = Map.<String, Object>of(
            "operation", "write",
            "fileName", "test.txt",
            "content", "Hello, ZMCP!"
        );
        
        var writeResult = tool.apply(writeInput);
        assertThat(writeResult).containsEntry("content", "File written successfully: test.txt");
        
        var readInput = Map.<String, Object>of(
            "operation", "read",
            "fileName", "test.txt"
        );
        
        var readResult = tool.apply(readInput);
        assertThat(readResult).containsEntry("content", "Hello, ZMCP!");
    }
    
    @Test
    void listFiles() throws Exception {
        Files.writeString(tempDir.resolve("file1.txt"), "content1");
        Files.writeString(tempDir.resolve("file2.txt"), "content2");
        
        var input = Map.<String, Object>of(
            "operation", "list"
        );
        
        var result = tool.apply(input);
        assertThat(result.get("content"))
            .contains("file1.txt")
            .contains("file2.txt");
    }
    
    @Test
    void deleteFile() throws Exception {
        Files.writeString(tempDir.resolve("to-delete.txt"), "content");
        
        var input = Map.<String, Object>of(
            "operation", "delete",
            "fileName", "to-delete.txt"
        );
        
        var result = tool.apply(input);
        assertThat(result).containsEntry("content", "File deleted successfully: to-delete.txt");
        assertThat(tempDir.resolve("to-delete.txt")).doesNotExist();
    }
    
    @Test
    void readNonExistentFileReturnsError() {
        var input = Map.<String, Object>of(
            "operation", "read",
            "fileName", "non-existent.txt"
        );
        
        var result = tool.apply(input);
        assertThat(result).containsKey("error");
    }
    
    @Test
    void invalidOperationReturnsError() {
        var input = Map.<String, Object>of(
            "operation", "invalid"
        );
        
        var result = tool.apply(input);
        assertThat(result).containsKey("error");
    }
    
    @Test
    void toolSpecContainsRequiredFields() {
        assertThat(FileStoreTool.TOOL_SPEC)
            .containsKeys("name", "description", "inputSchema");
        
        assertThat(FileStoreTool.TOOL_SPEC.get("name")).isEqualTo("FileStoreTool");
        
        var schemaJson = (String) FileStoreTool.TOOL_SPEC.get("inputSchema");
        assertThat(schemaJson)
            .contains("\"type\": \"object\"")
            .contains("\"properties\"")
            .contains("\"required\": [\"operation\"]")
            .contains("\"additionalProperties\": false");
    }
}