package dAL;

import java.util.ArrayList;


/**
 * This interface defines the Data Access Layer (DAL) for retrieving verses and related information for a single poem view.
 */
public interface IDALSinglePoemVIew {

    /**
     * Searches for verses based on a given search term and returns the result as a list.
     *
     * @param searchTerm The term to search for in verses.
     * @return An ArrayList of verses matching the search term.
     */
    ArrayList<String> searchVersesList(String searchTerm);

}
