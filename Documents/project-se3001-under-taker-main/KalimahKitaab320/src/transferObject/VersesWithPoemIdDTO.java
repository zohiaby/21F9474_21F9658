package transferObject;

/**
 * A simple class to hold both verse and poem ID.
 */
public class VersesWithPoemIdDTO {

    /**
     * The ID of the poem associated with the verses.
     */
    private String poemId;

    /**
     * Constructs a {@code VersesWithPoemIdDTO} with the given poem ID.
     *
     * @param poemId The ID of the poem.
     */
    public VersesWithPoemIdDTO(String poemId) {
        this.poemId = poemId;
    }

    /**
     * Gets the ID of the poem.
     *
     * @return The poem ID.
     */
    public String getPoemId() {
        return poemId;
    }
}
