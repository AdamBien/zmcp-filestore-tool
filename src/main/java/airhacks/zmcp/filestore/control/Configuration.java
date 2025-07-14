package airhacks.zmcp.filestore.control;

import java.nio.file.Path;

/**
 * Provides configuration values from environment variables.
 */
public interface Configuration {
    
    static Path getStorageRoot() {
        return Path.of(System.getenv().getOrDefault("ZMCP_STORAGE_ROOT", "./zmcp-filestore"));
    }
}