package dAL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;

import dAL.BooksDAO.Book;
import transferObject.PoemsDTO;
import transferObject.PoemsTO;
import transferObject.RootDTO;

public class DALFascade implements IDALFascade {
	private IDALRoot root;
	private IDALImportPoems importPoems;
	private IDALBooks books;
	private IDALPoems poemsDAO;
	private IDALToken tokenDAO;
	private SinglePoemVIewDAO singlePoemDAO;
	private RootDTO rootDTO;
	private ITokenAssignDAO itokens;
	private IUpdateRootDAO iroots;

	public DALFascade() {
        // Initialize the DAO instances.
        this.poemsDAO = new PoemsDAO();
        this.tokenDAO = new TokenDAO();
    }

	// Root Interface Functions
	@Override
    public void setIDALRoot(IDALRoot root) {
        this.root = root;
    }

	@Override
	public void addRootsInDB(RootDTO root) {
		this.root.addRootsInDB(root);
	}

	@Override
	public void deleteRootInDB(RootDTO root) {
		this.root.deleteRootInDB(root);
	}

	@Override
	public int getRootIDFromDB(RootDTO root) {
		return this.root.getRootIDFromDB(root);
	}

	@Override
	public void updateRootInDB(RootDTO root, int id) {
		this.root.updateRootInDB(root, id);
	}

	@Override
	public Map<Integer, RootDTO> getRootsAndIDsFromDB() {
		return this.root.getRootsAndIDsFromDB();
	}

	@Override
	public void setDTO(RootDTO root) {
	}

	// Poems Interface Functions
	@Override
	public int addPoem(String title, int bookId) {
		return poemsDAO.addPoem(title, bookId);
	}

	@Override
	public void addVerse(String verse1, String verse2) {
		poemsDAO.addVerse(verse1, verse2);
	}

	@Override
	public void assignPoemIdToVerses(int poemId) {
		poemsDAO.assignPoemIdToVerses(poemId);
	}

	@Override
	public JTable getAllPoemTitles(int bookId) {
		return poemsDAO.getAllPoemTitles(bookId);
	}

	@Override
	public JTable getPoemVerses(int poemId) {
		return poemsDAO.getPoemVerses(poemId);
	}

	@Override
	public void deletePoem(int poemId) {
		poemsDAO.deletePoem(poemId);
	}

	@Override
	public void updatePoemTitle(int poemId, String newTitle) {
		poemsDAO.updatePoemTitle(poemId, newTitle);
	}

	@Override
	public void deleteVerse(int verseId, int poemId) {
		poemsDAO.deleteVerse(verseId, poemId);
	}

	@Override
	public void updateVerse(String verse1, String verse2, String newVerse1, String newVerse2) {
		poemsDAO.updateVerse(verse1, verse2, newVerse1, newVerse2);
	}

	@Override
	public void setIDALbook(IDALBooks books) {
		this.books = books;
	}

	@Override
	public List<Book> getAllBooks() {
		return this.books.getAllBooks();
	}

	@Override
	public void insertBook(String title, String author, int year) {
		this.books.insertBook(title, author, year);
	}

	@Override
	public void updateBook(int id, String newTitle, String newAuthor, int newYear) {
		this.books.updateBook(id, newTitle, newAuthor, newYear);
	}

	@Override
	public void deleteBook(int id) {
		this.books.deleteBook(id);
	}

	@Override
	public void setDALRoot(IDALRoot root) {
		this.root = root;
	}

	// ImportPoems Interface Functions
	@Override
	public boolean parsePoem(String filePath) {
		return importPoems.parsePoem(filePath);
	}

	@Override
	public void addVerseToPoem(String verse1, String verse2, int currentPoemId) {
		poemsDAO.addVerseToPoem(verse1, verse2, currentPoemId);
	}

	
	

	// Token Interface Functions
	@Override
	public List<String> getVersesData(int verseId) {
		return tokenDAO.getVersesData(verseId);
	}

	@Override
	public List<String> getTokens(int verseId) {
		return tokenDAO.getTokens(verseId);
	}

	@Override
	public void tokenizeVerses(int verseId) {
		tokenDAO.tokenizeVerses(verseId);
	}

	@Override
	public void saveToken(int verseId, String token, String pos) {
		tokenDAO.saveToken(verseId, token, pos);
	}

	@Override
	public void updateToken(int verseId, int oldTokenId, String[] newTokens) {
		tokenDAO.updateToken(verseId, oldTokenId, newTokens);
	}

	@Override
	public int getTokenId(int verseId, String token) {
		return tokenDAO.getTokenId(verseId, token);
	}

	@Override
	public void deleteToken(int tokenId) {
		tokenDAO.deleteToken(tokenId);
	}

	@Override
	public void closeConnection() {
		tokenDAO.closeConnection();
	}

	@Override
	public ArrayList<String> searchVersesList(String searchTerm) {
		return singlePoemDAO.searchVersesList(searchTerm);
	}
	
	public void setITokenAssignDAO(ITokenAssignDAO itokens) {
		this.itokens = itokens;
	}
	
	public void setIUpdateRootDAO(IUpdateRootDAO iroots) {
		this.iroots = iroots;
	}

	@Override
	public int getVerseID(PoemsTO poem) {
		return itokens.getVerseID(poem);
	}

	@Override
	public void saveWords(PoemsTO poem, ArrayList<String> tokens) {
		itokens.saveWords(poem, tokens);
	}

	@Override
	public void checkWhetherInsertionOrNot(PoemsTO poem, String misraOne, String misraTwo) {
		itokens.checkWhetherInsertionOrNot(poem, misraOne, misraTwo);
	}

	@Override
	public int getVerseIDForRoots(PoemsTO poem) {
		return iroots.getVerseIDForRoots(poem);
	}

	@Override
	public void saveRootsInDB(PoemsTO poem, ArrayList<String> tokens) {
		iroots.saveRootsInDB(poem, tokens);
	}

	@Override
	public void checkInsertionOrNot(PoemsTO poem, String misraOne, String misraTwo) {
		iroots.checkInsertionOrNot(poem, misraOne, misraTwo);
	}

	@Override
	public List<String[]> getPoemContentByID(int num) {
		return poemsDAO.getPoemContentByID(num);
	}

	@Override
	public int getLastInsertedVerseId() {
		return poemsDAO.getLastInsertedVerseId();
	}
}
