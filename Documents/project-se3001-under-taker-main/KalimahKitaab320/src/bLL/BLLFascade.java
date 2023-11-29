package bLL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dAL.BooksDAO.Book;
import dAL.IDALFascade;
import transferObject.PoemsTO;
import transferObject.RootDTO;

public class BLLFascade implements IBLLFascade {

    private IRoot root;
    private IBooks books;
    private IPoems poems;
    private IToken token;
	private ITokenAssignedBO itoken;
	private IUpdateRootBo iroots;

    // Root Function starts here.
    public void setBLLRoot(IRoot root) {
        this.root = root;
    }

    @Override
    public void addRoot(RootDTO root) {
        if (this.root != null) {
            this.root.addRoot(root);
        } else {
            System.err.println("Root object is null. Make sure it is properly initialized add in DB BLL.");
        }
    }

    @Override
    public void deleteRoot(RootDTO root) {
        if (this.root != null) {
            this.root.deleteRoot(root);
        } else {
            System.err.println("Root object is null. Make sure it is properly initialized Delete root BLL.");
        }
    }

    @Override
    public int getID(RootDTO root) {
        if (this.root != null) {
            return this.root.getID(root);
        } else {
            System.err.println("Root object is null. Make sure it is properly initialized Get id BLL.");
        }
        return 0;
    }

    @Override
    public void updateRoot(RootDTO root, int id) {
        if (this.root != null) {
            this.root.updateRoot(root, id);
        } else {
            System.err.println("Root object is null. Make sure it is properly initialized Update BLL.");
        }
    }

    @Override
    public Map<Integer, RootDTO> getRootsAndIDs() {
        if (this.root != null) {
            return this.root.getRootsAndIDs();
        } else {
            System.err.println("Root object is null. Make sure it is properly initialized Get root id BLL.");
        }
        return null;
    }

    @Override
    public void setFascade(IDALFascade fascade) {
        if (this.root != null) {
            this.root.setFascade(fascade);
        } else {
            System.err.println("Root object is null. Make sure it is properly initialized set Fascade BLL.");
        }
    }

    // Root end.

    public void setBLLPoem(IPoems poems) {
        this.poems = poems;
    }

    @Override
    public int addPoem(String title, int bookId) {
        return poems.addPoem(title, bookId);
    }

    @Override
    public void addVerse(String verse1, String verse2) {
        poems.addVerse(verse1, verse2);
    }

    @Override
    public void assignPoemIdToVerses(int poemId) {
        poems.assignPoemIdToVerses(poemId);
    }

    @Override
    public void deleteVerse(int verseId, int poemId) {
        poems.deleteVerse(verseId, poemId);
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
    public void setIDAL(IDALFascade dal) {
        this.books.setIDAL(dal);
    }

    public void setIBooks(IBooks books) {
        this.books = books;
    }

    public void setIToken(IToken token) {
        this.token = token;
    }

    @Override
    public List<String> getVersesData(int verseId) {
        return this.token.getVersesData(verseId);
    }

    @Override
    public List<String> getTokens(int verseId) {
        return this.token.getTokens(verseId);
    }

    @Override
    public void tokenizeVerses(int verseId) {
        this.token.tokenizeVerses(verseId);
    }

    @Override
    public int getTokenId(int verseId, String token) {
        return this.token.getTokenId(verseId, token);
    }

    @Override
    public void deleteToken(int tokenId) {
        this.token.deleteToken(tokenId);
    }

    @Override
    public void saveToken(int verseId, String token, String pos) {
        this.token.saveToken(verseId, token, pos);
    }

    @Override
    public void updateToken(int verseId, String oldToken, String[] newTokens) {
        this.token.updateToken(verseId, oldToken, newTokens);
    }

    @Override
    public void closeDatabaseConnection() {
        this.token.closeDatabaseConnection();
    }

    @Override
    public String getPOSTagForToken(String token) {
        return this.token.getPOSTagForToken(token);
    }

    @Override
    public void setIBook(IBooks books) {
        this.books = books;
    }

	public void setITokenAssignedBO(ITokenAssignedBO itoken) {
		this.itoken = itoken;
	}

	@Override
	public int getVerseID(PoemsTO poem) {
		return itoken.getVerseID(poem);
	}

	@Override
	public void saveWords(PoemsTO poem) {
		itoken.saveWords(poem);
	}

	@Override
	public ArrayList<String> splitVerses(PoemsTO poem) {
		return itoken.splitVerses(poem);
	}

	@Override
	public void checkWhetherInsertionOrNot(PoemsTO poem, String misraOne, String misraTwo) {
		itoken.checkWhetherInsertionOrNot(poem, misraOne, misraTwo);
	}
	
	public void setIUpdateRootBo(IUpdateRootBo iroots) {
		this.iroots = iroots;
	}

	@Override
	public int getVerseIDForRoots(PoemsTO poem) {
		return iroots.getVerseIDForRoots(poem);
	}

	@Override
	public ArrayList<String> suggestRoots(PoemsTO poem) {
		return iroots.suggestRoots(poem);
	}

	@Override
	public void checkInsertionOrNot(PoemsTO poem, String misraOne, String misraTwo) {
		iroots.checkInsertionOrNot(poem, misraOne, misraTwo);
	}

	@Override
	public void saveWordsForRoots(PoemsTO poem, ArrayList<String> suggestedRoot) {
		iroots.saveWordsForRoots(poem, suggestedRoot);
	}
}
