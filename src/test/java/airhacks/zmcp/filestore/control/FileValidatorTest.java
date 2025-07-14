package airhacks.zmcp.filestore.control;

import org.junit.jupiter.api.Test;
import airhacks.zmcp.filestore.entity.FileOperation;
import static org.assertj.core.api.Assertions.*;

class FileValidatorTest {
    
    @Test
    void validateSucceedsForValidReadOperation() {
        var operation = new FileOperation("read", "file.txt", null, "/storage");
        assertThatCode(() -> FileValidator.validateOperation(operation)).doesNotThrowAnyException();
    }
    
    @Test
    void validateSucceedsForValidWriteOperation() {
        var operation = new FileOperation("write", "file.txt", "content", "/storage");
        assertThatCode(() -> FileValidator.validateOperation(operation)).doesNotThrowAnyException();
    }
    
    @Test
    void validateSucceedsForValidListOperation() {
        var operation = new FileOperation("list", null, null, "/storage");
        assertThatCode(() -> FileValidator.validateOperation(operation)).doesNotThrowAnyException();
    }
    
    @Test
    void validateFailsForMissingOperationType() {
        var operation = new FileOperation(null, "file.txt", null, "/storage");
        assertThatThrownBy(() -> FileValidator.validateOperation(operation))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Operation type is required");
    }
    
    
    @Test
    void validateFailsForPathTraversal() {
        var operation = new FileOperation("read", "../secret.txt", null, "/storage");
        assertThatThrownBy(() -> FileValidator.validateOperation(operation))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Path traversal not allowed");
    }
    
    @Test
    void validateFailsForAbsolutePath() {
        var operation = new FileOperation("read", "/etc/passwd", null, "/storage");
        assertThatThrownBy(() -> FileValidator.validateOperation(operation))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Absolute paths not allowed");
    }
    
    @Test
    void validateFailsForMissingContentInWriteOperation() {
        var operation = new FileOperation("write", "file.txt", null, "/storage");
        assertThatThrownBy(() -> FileValidator.validateOperation(operation))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Content is required for write operation");
    }
}