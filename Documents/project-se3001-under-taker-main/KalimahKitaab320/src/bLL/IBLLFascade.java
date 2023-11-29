package bLL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dAL.BooksDAO.Book;
import dAL.IDALFascade;
import transferObject.PoemsTO;
import transferObject.RootDTO;

public interface IBLLFascade extends IRoot, IBooks, IToken, ITokenAssignedBO, IUpdateRootBo {

	// for root
	public void addRoot(RootDTO root);
	
	public void setBLLRoot(IRoot root);

	public void deleteRoot(RootDTO root);

	public int getID(RootDTO root);

	public void updateRoot(RootDTO root, int id);

	public Map<Integer, RootDTO> getRootsAndIDs();

	public int getVerseID(PoemsTO poem);

	public void saveWords(PoemsTO poem);

	public ArrayList<String> splitVerses(PoemsTO poem);

	public void checkWhetherInsertionOrNot(PoemsTO poem, String misraOne, String misraTwo);

	public int getVerseIDForRoots(PoemsTO poem);

	public ArrayList<String> suggestRoots(PoemsTO poem);

	public void checkInsertionOrNot(PoemsTO poem, String misraOne, String misraTwo);

	public void saveWordsForRoots(PoemsTO poem, ArrayList<String> suggestedRoot);

	// for book
	public List<Book> getAllBooks();

	public void insertBook(String title, String author, int year);

	public void updateBook(int id, String newTitle, String newAuthor, int newYear);

	public void deleteBook(int id);

	public void setIDAL(IDALFascade dal);

	public void setIBook(IBooks books);

	


	// for Poem
	public int addPoem(String title, int bookId);

	public void addVerse(String verse1, String verse2);

	public void assignPoemIdToVerses(int poemId);

	void deleteVerse(int verseId, int poemId);

}
