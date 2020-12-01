package domain.cmap.creator;

import java.util.ArrayList;
import java.util.List;

public class Term {
	
	private String name;
	private int apparitionCont;
	private List<File> whereThisTermAppears=new ArrayList<File>();
	
	public List<File> getWhereThisTermAppears() {
		return this.whereThisTermAppears;
	}

	public void setWhereThisTermAppears(List<File> whereThisTermAppears) {
		this.whereThisTermAppears = whereThisTermAppears;
	}
	
	public void addNewFile(String name, String path) {
		if(!contains(name)) {
			File f = new File(name, path);
			this.whereThisTermAppears.add(f);
		}
		
		
	}
	public boolean contains(String name) {
		if(sameName(name)) {
			return true;
				
		}else {
			return false;
		}
		
	}	
	private boolean sameName(String name2) {
		int i =0;
		while (i<this.whereThisTermAppears.size()) {
			if(this.whereThisTermAppears.get(i).getName().equals(name2)) {
				return true;
			}else {
				i++;
			}
		}
		return false;
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

	public boolean isCamelCase() {
		if(this.name.equals(this.name.toLowerCase())) {
			
			return false;
		}else {
			return true;
		}
	}

	public int getUpperCaseCont() {
		int upper = 0;
		for(int i = 0; i < this.name.length(); i++) {
			 char ch = this.name.charAt(i); 
	            if (ch >= 'A' && ch <= 'Z') 
	                upper++;
		}
		
		return upper;
	}


	public boolean firsChartUpper() {
		char ch = this.name.charAt(0); 
        if (ch >= 'A' && ch <= 'Z') 
            return true;
        else 
        	return false;
	}
	
}
