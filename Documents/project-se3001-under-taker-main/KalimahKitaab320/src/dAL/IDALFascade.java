package dAL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;

import dAL.BooksDAO.Book;
import transferObject.PoemsDTO;
import transferObject.PoemsTO;
import transferObject.RootDTO;

public interface IDALFascade extends IDALRoot, IDALBooks, IDALImportPoems, IDALToken, IDALSinglePoemVIew, ITokenAssignDAO, IUpdateRootDAO{

	// for the root
	public void addRootsInDB(RootDTO root);

	public void deleteRootInDB(RootDTO root);

	public int getRootIDFromDB(RootDTO root);

	public void updateRootInDB(RootDTO root, int id);

	public void setDALRoot(IDALRoot root);
	
	public void setIDALRoot(IDALRoot root);

	public Map<Integer, RootDTO> getRootsAndIDsFromDB();

	public void setDTO(RootDTO root);

	public int getVerseID(PoemsTO poem);

	public void saveWords(PoemsTO poem, ArrayList<String> tokens);

	public void checkWhetherInsertionOrNot(PoemsTO poem, String misraOne, String misraTwo);

	public int getVerseIDForRoots(PoemsTO poem);

	public void saveRootsInDB(PoemsTO poem, ArrayList<String> tokens);

	public void checkInsertionOrNot(PoemsTO poem, String misraOne, String misraTwo);
	
	public void setIUpdateRootDAO(IUpdateRootDAO iroots);
	
	public List<String[]> getPoemContentByID(int num);

	// for Books
	public List<Book> getAllBooks();

	public void insertBook(String title, String author, int year);

	public void updateBook(int id, String newTitle, String newAuthor, int newYear);

	public void deleteBook(int id);

	public void setIDALbook(IDALBooks books);

	// for import Poem
	public boolean parsePoem(String filePath);

	// for Poem
	public int addPoem(String title, int bookId);

	public void addVerse(String verse1, String verse2);

	public void assignPoemIdToVerses(int poemId);

	public JTable getAllPoemTitles(int bookId);

	public JTable getPoemVerses(int poemId);

	public void deletePoem(int poemId);

	public void updatePoemTitle(int poemId, String newTitle);

	public void deleteVerse(int verseId, int poemId);

	public void updateVerse(String verse1, String verse2, String newVerse1, String newVerse2);

	public void addVerseToPoem(String verse1, String verse2, int currentPoemId);

	public ArrayList<String> searchVersesList(String searchTerm);

	public int getLastInsertedVerseId();
}
