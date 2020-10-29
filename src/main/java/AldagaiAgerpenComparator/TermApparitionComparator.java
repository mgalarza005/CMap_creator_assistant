package AldagaiAgerpenComparator;
import java.util.Comparator;

import domain.cmap.creator.Term;

public class TermApparitionComparator implements Comparator<Term> {

	@Override
	public int compare(Term a1, Term a2) {
		
		return a2.getapparitionCont() - a1.getapparitionCont();
	}

}
