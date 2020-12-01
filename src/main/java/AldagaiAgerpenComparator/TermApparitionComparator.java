package AldagaiAgerpenComparator;
import java.util.Comparator;

import domain.cmap.creator.Term;

public class TermApparitionComparator implements Comparator<Term> {

	@Override
	public int compare(Term t1, Term t2) {
		
		return t2.getapparitionCont() - t1.getapparitionCont();
	}

}
