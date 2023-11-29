package transferObject;

/**
 * Data Transfer Object (DTO) representing information related to importing poems.
 */
public class ImportPoemDTO {

    /**
     * The file path of the poem to be imported.
     */
    public String filePath;

    /**
     * Constructs an {@code ImportPoemDTO} with the given file path.
     *
     * @param filePath The file path of the poem to be imported.
     */
    public ImportPoemDTO(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the file path of the poem to be imported.
     *
     * @return The file path.
     */
    public String getFilePath() {
        return filePath;
    }
}
