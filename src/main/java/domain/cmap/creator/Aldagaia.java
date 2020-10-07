package domain.cmap.creator;

public class Aldagaia {
	
	private String aldagaIzena;
	
	
	public Aldagaia(String aldagaIzena, int agerpenKop) {
		super();
		this.aldagaIzena = aldagaIzena;
		this.agerpenKop = agerpenKop;
	}
	private int agerpenKop;
	
	
	public String getAldagaIzena() {
		return aldagaIzena;
	}
	public void setAldagaIzena(String aldagaIzena) {
		this.aldagaIzena = aldagaIzena;
	}
	public int getAgerpenKop() {
		return agerpenKop;
	}
	public void setAgerpenKop(int agerpenKop) {
		this.agerpenKop = agerpenKop;
	}
	
}
