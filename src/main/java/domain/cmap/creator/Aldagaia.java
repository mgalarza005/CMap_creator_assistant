package domain.cmap.creator;

public class Aldagaia {
	
	private String izena;
	
	
	public Aldagaia(String aldagaIzena, int agerpenKop) {
		super();
		this.izena = aldagaIzena;
		this.agerpenKop = agerpenKop;
	}
	private int agerpenKop;
	
	
	public String getAldagaIzena() {
		return izena;
	}
	public void setAldagaIzena(String aldagaIzena) {
		this.izena = aldagaIzena;
	}
	public int getAgerpenKop() {
		return agerpenKop;
	}
	public void setAgerpenKop(int agerpenKop) {
		this.agerpenKop = agerpenKop;
	}
	
}
