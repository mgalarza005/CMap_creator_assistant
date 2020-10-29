package AldagaiAgerpenComparator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import domain.cmap.creator.Term;

public class TermChainedComparator implements Comparator<Term>{

    private List<Comparator<Term>> listComparators;
	
    @SafeVarargs
    public TermChainedComparator(Comparator<Term>... comparators) {
        this.listComparators = Arrays.asList(comparators);
    }
    
	@Override
	public int compare(Term o1, Term o2) {
		// TODO Auto-generated method stub
		for (Comparator<Term> comparator : listComparators) {
            int result = comparator.compare(o1, o2);
            if (result != 0) {
                return result;
            }
        }
		return 0;
	}

}
