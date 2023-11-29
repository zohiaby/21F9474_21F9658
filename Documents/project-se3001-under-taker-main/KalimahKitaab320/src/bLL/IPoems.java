package bLL;

public interface IPoems {

	public int addPoem(String title, int bookId);

	public void addVerse(String verse1, String verse2);

	public void assignPoemIdToVerses(int poemId);

	public void deleteVerse(int verseId, int poemId);


}
