package bLL;

import java.util.ArrayList;

import transferObject.PoemsTO;

public interface ITokenAssignedBO {

	public int getVerseID(PoemsTO poem);

	public void saveWords(PoemsTO poem);

	public ArrayList<String> splitVerses(PoemsTO poem);

	public void checkWhetherInsertionOrNot(PoemsTO poem, String misraOne, String misraTwo);

}
