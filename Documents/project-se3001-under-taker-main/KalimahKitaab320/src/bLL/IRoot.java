package bLL;

import java.util.Map;

import dAL.IDALFascade;
import transferObject.RootDTO;

public interface IRoot {

	public void setFascade(IDALFascade fascade);

	public void addRoot(RootDTO root);

	public void deleteRoot(RootDTO root);

	public int getID(RootDTO root);

	public void updateRoot(RootDTO root, int id);

	public Map<Integer, RootDTO> getRootsAndIDs();
}
