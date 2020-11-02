package domain.cmap.creator;

import java.util.ArrayList;
import java.util.List;

public class Term {
	
	private String name;
	private int apparitionCont;
	private List<String> whereThisTermAppears=new ArrayList<String>();
	
	public List<String> getWhereThisTermAppears() {
		return whereThisTermAppears;
	}

	public void setWhereThisTermAppears(List<String> whereThisTermAppears) {
		this.whereThisTermAppears = whereThisTermAppears;
	}

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
