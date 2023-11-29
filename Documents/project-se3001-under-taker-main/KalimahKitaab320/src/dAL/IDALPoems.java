package dAL;

import java.util.List;
import java.util.Map;

import javax.swing.JTable;

import transferObject.PoemsDTO;

public interface IDALPoems {
  
    public int addPoem(String title, int bookId);
    public void addVerse(String verse1, String verse2);
    public void assignPoemIdToVerses(int poemId);
	public JTable getAllPoemTitles(int bookid);
	public JTable getPoemVerses(int poemId);
	public void deletePoem(int poemId);
	public void deleteVerse(int verseId, int poemId);
	void updateVerse(String verse1, String verse2, String newVerse1, String newVerse2);
	public void addVerseToPoem(String verse1, String verse2, int currentPoemId);
	public void updatePoemTitle(int poemId, String newTitle);
	List<String[]> getPoemContentByID(int num);
	int getLastInsertedVerseId();
	
}
