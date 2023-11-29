package transferObject;

/**
 * Data Transfer Object (DTO) representing the main information for GUI search.
 */
public class MainGUIDTO {

    /**
     * The search term used in the GUI.
     */
    private String searchTerm;

    /**
     * The ID associated with the search term.
     */
    private String id;

    /**
     * Constructs a {@code MainGUIDTO} with the given search term and ID.
     *
     * @param searchTerm The search term entered in the GUI.
     * @param id         The ID associated with the search term.
     */
    public MainGUIDTO(String searchTerm, String id) {
        this.searchTerm = searchTerm;
        this.id = id;
    }

    /**
     * Gets the ID associated with the search term.
     *
     * @return The ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID associated with the search term.
     *
     * @param id The ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the search term.
     *
     * @param searchTerm The search term to set.
     */
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    /**
     * Gets the search term.
     *
     * @return The search term.
     */
    public String getSearchTerm() {
        return searchTerm;
    }
}
