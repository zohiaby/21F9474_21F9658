package bLL;

import java.util.ArrayList;
import java.util.List;

import dAL.IUpdateRootDAO;
import dAL.UpdateRootDAO;
import transferObject.PoemsTO;

public class UpdateRootBO extends BLLFascade {
	// IDALFascade temp = new DALFascade();
	// IUpdateRootDAO root1 = new UpdateRootDAO();
	IUpdateRootDAO root1;
	UpdateRootDAO iroots = new UpdateRootDAO(root1);
	ArrayList<String> suggestedRoot;

	public UpdateRootBO(IUpdateRootBo token1) {
		setIUpdateRootBo(token1);
	}
	//UpdateRootDAO iroots = new UpdateRootDAO();
	// janu.setIUpdateRootDAO(root1);
	//ArrayList<String> suggestedRoot;

	public int getVerseIDForRoots(PoemsTO poem) {
		return iroots.getVerseID(poem);
	}

	// Function to Suggest Root for Verses
	public ArrayList<String> suggestRoots(PoemsTO poem) {

		ArrayList<String> suggestedRoot = new ArrayList<>();

		String verse = poem.getMisra_1() + " " + poem.getMisra_2();

		String[] splittedVerse = verse.split(" ");

		for (String word : splittedVerse) {
			List<String> roots = net.oujda_nlp_team.AlKhalil2Analyzer.getInstance().processToken(word).getAllRoots();
			for (String suggRoot : roots) {
				if (suggRoot.contains("-") || suggRoot.contains(" ") || suggRoot.contains(":")) {
					// TODO
				} else {
					suggestedRoot.add(suggRoot);
				}
			}
		}

		this.suggestedRoot = suggestedRoot;

		return suggestedRoot;
	}

	// Function to save words/tokens along with Verses in Database in BO
	public void saveWordsForRoots(PoemsTO poem) {
		iroots.saveWords(poem, suggestedRoot);
	}

	// Function to check whether the verse is different from already in poem or not
	// if diff then insert a new verse otherwise move on
	public void checkInsertionOrNot(PoemsTO poem, String misraOne, String misraTwo) {
		if (poem == null) {
			return;
		} else if (misraOne.isEmpty() || misraTwo.isEmpty()) {
			return;
		}

		iroots.checkWhetherInsertionOrNot(poem, misraOne, misraTwo);
	}
}
