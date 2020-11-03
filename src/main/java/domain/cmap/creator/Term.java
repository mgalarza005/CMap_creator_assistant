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
	/*
	public -- getUpperLowerNumberSpecial(){
	
	
	
	
        String str = "#GeeKs01fOr@gEEks07"; 
        int upper = 0, lower = 0, number = 0, special = 0; 
  
        for(int i = 0; i < str.length(); i++) 
        { 
            char ch = str.charAt(i); 
            if (ch >= 'A' && ch <= 'Z') 
                upper++; 
            else if (ch >= 'a' && ch <= 'z') 
                lower++; 
            else if (ch >= '0' && ch <= '9') 
                number++; 
            else
                special++; 
        } 
  
        System.out.println("Lower case letters : " + lower); 
        System.out.println("Upper case letters : " + upper); 
        System.out.println("Number : " + number); 
        System.out.println("Special characters : " + special); 
    } 
	}
	
	 */
	
}
