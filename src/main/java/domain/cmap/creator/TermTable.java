package domain.cmap.creator;

import java.util.Arrays;
import java.util.List;

public class TermTable {
	private List<Term> termT;
	private static List<String> stopWords=Arrays.asList(new String[] {"break","case","catch","continue","default","delete","do","else","finally","for","function","if","in","instanceof","new","return","switch","this","trhow","try","typeof","var","void","while","with","abstract","boolean","byte",
			"char","class","const","debugger","double","enum", "", ":", " ","export","extends","final","float","===", "!==", "(err)","goto","implements","import","int","interface","long","native","package","private","protected",
			"public","short","static","super","syncronized","trhows","transient","volatile", "from", "value", "result", "get", "all", "has","start", "set","error", "close", "size", "load", "cancel", "read"
			,"defer", "reject", "abort", "print","time", "handler:", "color", "(err)"  });
	
	
	public List<String> getStopWords() {
		return stopWords;
	}

	public void setStopWords(List<String> stopWords) {
		TermTable.stopWords = stopWords;
	}

	public TermTable(List<Term> termT) {
		super();
		this.termT = termT;
		
	}

	public List<Term> getTermTable() {
		return termT;
	}

	public void setTermTable(List<Term> termT) {
		this.termT = termT;
	}
	
	public boolean appears(String a) {
		
		if(a!=null || a!=" ") {
			int j=0;
			while(j<this.termT.size()) {
				if(this.termT.get(j).getTermName().equals(a)) {
					return true;
				}
				j++;
			}
		}
		return false;
	}
	public void sumApparition(String izena) {
		
		int i=0;
		Boolean topatua=false;
		while(i<this.termT.size() && topatua.booleanValue()==false) {
			if(this.termT.get(i).getTermName().equals(izena)) {
				this.termT.get(i).setapparitionCont(this.termT.get(i).getapparitionCont()+1);
				topatua=true;
			}
			i++;
		}
	}

	public boolean itsStopWord(String param) {
		if (TermTable.stopWords.contains(param)) {
			return true;
		}else {
			return false;
		}
	}

	public void addWhereThisTermAppears(String term, String file) {
		int i=0;
		Boolean topatua=false;
		while(i<this.termT.size() && topatua.booleanValue()==false) {
			if(this.termT.get(i).getTermName().equals(term)) {
				if( !this.termT.get(i).getWhereThisTermAppears().contains(file) ){
					this.termT.get(i).getWhereThisTermAppears().add(file);
				}
				
				topatua=true;
			}
			i++;
		}
	}

}
