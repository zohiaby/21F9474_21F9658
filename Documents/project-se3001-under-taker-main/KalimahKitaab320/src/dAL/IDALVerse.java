package dAL;

import java.util.List;

/**
 * This interface defines the Data Access Layer (DAL) for retrieving verses from a database.
 */
public interface IDALVerse {

    /**
     * Retrieves verses from the database based on the given poem ID.
     *
     * @param poemId The ID of the poem to retrieve verses for.
     * @return A list of verses as arrays of strings.
     */
    List<String[]> getVersesByPoemId(String poemId);
}

