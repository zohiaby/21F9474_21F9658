package transferObject;

public class PoemsTO {
	
	private String misra_1, misra_2, poemTitle;
	private int poem_id;

	

	public int getPoem_id() {
		return poem_id;
	}

	public void setPoem_id(int poem_id) {
		this.poem_id = poem_id;
	}

	public String getMisra_1() {
		return misra_1;
	}

	public void setMisra_1(String misra_1) {
		this.misra_1 = misra_1;
	}

	public String getMisra_2() {
		return misra_2;
	}

	public void setMisra_2(String misra_2) {
		this.misra_2 = misra_2;
	}

	public String getPoemTitle() {
		return poemTitle;
	}

	public void setPoemTitle(String poemTitle) {
		this.poemTitle = poemTitle;
	}

	public void setTitle(String poemTitle)
	{
		this.poemTitle=poemTitle;
	}
}
