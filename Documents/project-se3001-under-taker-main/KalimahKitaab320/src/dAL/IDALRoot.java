package dAL;

import java.util.Map;

import transferObject.RootDTO;

public interface IDALRoot {
	public void addRootsInDB(RootDTO root);

	public void deleteRootInDB(RootDTO root);
	
	public int getRootIDFromDB(RootDTO root);
	
	public void setDTO(RootDTO root);

	public void updateRootInDB(RootDTO root,int id);

	public Map<Integer, RootDTO> getRootsAndIDsFromDB();
}
