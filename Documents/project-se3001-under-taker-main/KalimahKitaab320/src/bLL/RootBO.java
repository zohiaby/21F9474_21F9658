package bLL;

import java.util.Map;

import dAL.IDALFascade;
import transferObject.RootDTO;

public class RootBO implements IRoot {
	private IDALFascade fascade;

	public void addRoot(RootDTO root) {
		this.fascade.addRootsInDB(root);
	}

	public void setFascade(IDALFascade fascade) {
		this.fascade = fascade;
	}

	public void deleteRoot(RootDTO root) {
		this.fascade.deleteRootInDB(root);
	}

	public int getID(RootDTO root) {
		return this.fascade.getRootIDFromDB(root);
	}

	public void updateRoot(RootDTO root, int id) {
		this.fascade.updateRootInDB(root, id);
	}

	public Map<Integer, RootDTO> getRootsAndIDs() {
		return this.fascade.getRootsAndIDsFromDB();
	}
}
