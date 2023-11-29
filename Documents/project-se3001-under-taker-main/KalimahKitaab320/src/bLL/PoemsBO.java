package bLL;

import java.util.List;
import java.util.Map;

import javax.swing.JTable;

import dAL.DALFascade;
import dAL.IDALFascade;
import transferObject.PoemsDTO;

public class PoemsBO implements IPoems {

    private IDALFascade poems;

    public PoemsBO() {
        poems = new DALFascade(); // Initialize the DAO instance.
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

    // Add method to get all poem titles
    public JTable getAllPoemTitles(int bookId) {
        return poems.getAllPoemTitles(bookId);
    }

    // Add method to get poem content by title
    public JTable getPoemVerses(int poemId) {
        return poems.getPoemVerses(poemId);
    }

	public void deletePoem(int poemId) {
		poems.deletePoem(poemId);
	}

	public void updatePoemTitle(int poemId, String newTitle) {
		poems.updatePoemTitle(poemId, newTitle);
	}

	public void deleteVerse(int verseId, int poemId) {
		poems.deleteVerse(verseId,poemId);
	}

	public void updateVerse(String verse1, String verse2, String newVerse1, String newVerse2) {
		poems.updateVerse(verse1, verse2, newVerse1, newVerse2);
		
	}

	public void addVerseToPoem(String verse1, String verse2, int currentPoemId) {
		poems.addVerseToPoem(verse1, verse2, currentPoemId);
	}

	


	public List<String[]> getPoemContentByID(int num){
		return poems.getPoemContentByID(num);
	}

	public int getLastInsertedVerseId() {
		// TODO Auto-generated method stub
		return poems.getLastInsertedVerseId();
	}

	
}
