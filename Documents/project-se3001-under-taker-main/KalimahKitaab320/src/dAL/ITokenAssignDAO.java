package dAL;

import java.util.ArrayList;

import transferObject.PoemsTO;

public interface ITokenAssignDAO {
	
	public int getVerseID(PoemsTO poem);
	public void saveWords(PoemsTO poem, ArrayList<String> tokens);
	public void checkWhetherInsertionOrNot(PoemsTO poem, String misraOne, String misraTwo);
}
