package domain.cmap.creator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AldagaiTaula {
	private List<Aldagaia> aldagaiT;
	
	
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

}
