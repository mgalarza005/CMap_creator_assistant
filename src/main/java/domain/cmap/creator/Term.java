package domain.cmap.creator;

public class Term {
	
	private String name;
	private int apparitionCont;
	
	public Term(String termName, int apparitionCont) {
		super();
		this.name = termName;
		this.apparitionCont = apparitionCont;
	}
	
	
	
	public String getTermName() {
		return name;
	}
	public void setTermName(String ternMame) {
		this.name = ternMame;
	}
	public int getapparitionCont() {
		return apparitionCont;
	}
	public void setapparitionCont(int apparitionCont) {
		this.apparitionCont = apparitionCont;
	}
	
}
