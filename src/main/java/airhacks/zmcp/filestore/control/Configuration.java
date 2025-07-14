package airhacks.zmcp.filestore.control;

/**
 * Provides configuration values from environment variables.
 */
public interface Configuration {
    
    static String getStorageRoot() {
        return System.getenv().getOrDefault("ZMCP_STORAGE_ROOT", "./zmcp-filestore");
    }
}