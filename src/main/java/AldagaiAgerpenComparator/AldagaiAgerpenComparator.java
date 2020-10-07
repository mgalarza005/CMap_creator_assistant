package AldagaiAgerpenComparator;
import java.util.Comparator;

import domain.cmap.creator.Aldagaia;

public class AldagaiAgerpenComparator implements Comparator<Aldagaia> {

	@Override
	public int compare(Aldagaia a1, Aldagaia a2) {
		
		return a2.getAgerpenKop() - a1.getAgerpenKop();
	}

}
