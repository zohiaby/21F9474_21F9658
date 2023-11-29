package bLL;

import java.util.ArrayList;

import transferObject.PoemsTO;

public interface IUpdateRootBo {
	public int getVerseIDForRoots(PoemsTO poem);

	public ArrayList<String> suggestRoots(PoemsTO poem);

	public void checkInsertionOrNot(PoemsTO poem, String misraOne, String misraTwo);

	public void saveWordsForRoots(PoemsTO poem, ArrayList<String> suggestedRoot);

}
