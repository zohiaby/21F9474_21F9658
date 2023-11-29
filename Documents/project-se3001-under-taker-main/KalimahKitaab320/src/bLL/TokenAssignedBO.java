package bLL;

import java.util.ArrayList;

import dAL.ITokenAssignDAO;
import dAL.TokenAssignDAO;
import transferObject.PoemsTO;

public class TokenAssignedBO extends BLLFascade {

	ITokenAssignDAO token1;
	TokenAssignDAO itoken = new TokenAssignDAO(token1);
	ArrayList<String> tokens;

	public TokenAssignedBO(ITokenAssignedBO token1) {
		setITokenAssignedBO(token1);
	}

	public int getVerseID(PoemsTO poem) {
		return itoken.getVerseID(poem);
	}

	// Function to Split Verses
	public ArrayList<String> splitVerses(PoemsTO poem) {

		ArrayList<String> tokens = new ArrayList<>();

		String verse = poem.getMisra_1() + " " + poem.getMisra_2();

		String[] words = verse.split(" ");

		for (String word : words) {
			tokens.add(word);
		}

		this.tokens = tokens;

		return tokens;
	}

	// Function to save words/tokens along with Verses in Database in BO
	public void saveWords(PoemsTO poem) {

		itoken.saveWords(poem, tokens);
	}

	// Function to check whether the verse is different from already in poem or not
	// it diff then insert a new verse otherwise go on
	public void checkWhetherInsertionOrNot(PoemsTO poem, String misraOne, String misraTwo) {
		if (poem == null) {
			return;
		} else if (misraOne.isEmpty() || misraTwo.isEmpty()) {
			return;
		}

		itoken.checkWhetherInsertionOrNot(poem, misraOne, misraTwo);
	}
}
