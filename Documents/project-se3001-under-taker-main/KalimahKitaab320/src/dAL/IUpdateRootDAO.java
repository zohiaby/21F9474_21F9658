package dAL;

import java.util.ArrayList;

import transferObject.PoemsTO;

public interface IUpdateRootDAO {
	
	public int getVerseIDForRoots(PoemsTO poem);
	public void saveRootsInDB(PoemsTO poem, ArrayList<String> tokens);
	public void checkInsertionOrNot(PoemsTO poem, String misraOne, String misraTwo);
}
