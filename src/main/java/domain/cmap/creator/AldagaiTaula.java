package domain.cmap.creator;

import java.util.Arrays;
import java.util.List;

public class AldagaiTaula {
	private List<Aldagaia> aldagaiT;
	private static List<String> stopWords=Arrays.asList(new String[] {"break","case","catch","continue","default","delete","do","else","finally","for","function","if","in","instanceof","new","return","switch","this","trhow","try","typeof","var","void","while","with","abstract","boolean","byte",
			"char","class","const","debugger","double","enum","export","extends","final","float","goto","implements","import","int","interface","long","native","package","private","protected",
			"public","short","static","super","syncronized","trhows","transient","volatile", "from", "value", "result"});
	
	
	public List<String> getStopWords() {
		return stopWords;
	}

	public void setStopWords(List<String> stopWords) {
		AldagaiTaula.stopWords = stopWords;
	}

	public AldagaiTaula(List<Aldagaia> aldagaiT) {
		super();
		this.aldagaiT = aldagaiT;
		
	}

	public List<Aldagaia> getAldagaiT() {
		return aldagaiT;
	}

	public void setAldagaiT(List<Aldagaia> aldagaiT) {
		this.aldagaiT = aldagaiT;
	}
	
	public boolean agertzenDa(String a) {
		
		if(a!=null || a!=" ") {
			int j=0;
			while(j<this.aldagaiT.size()) {
				if(this.aldagaiT.get(j).getAldagaIzena().equals(a)) {
					return true;
				}
				j++;
			}
		}
		return false;
	}
	public void gehituKopurua(String izena) {
		
		int i=0;
		Boolean topatua=false;
		while(i<this.aldagaiT.size() && topatua.booleanValue()==false) {
			if(this.aldagaiT.get(i).getAldagaIzena().equals(izena)) {
				this.aldagaiT.get(i).setAgerpenKop(this.aldagaiT.get(i).getAgerpenKop()+1);
				topatua=true;
			}
			i++;
		}
	}

	public boolean stopWordAgertzenDa(String param) {
		if (AldagaiTaula.stopWords.contains(param)) {
			return true;
		}else {
			return false;
		}
	}

}
