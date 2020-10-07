package AldagaiAgerpenComparator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import domain.cmap.creator.Aldagaia;

public class AldagaiChainedComparator implements Comparator<Aldagaia>{

    private List<Comparator<Aldagaia>> listComparators;
	
    @SafeVarargs
    public AldagaiChainedComparator(Comparator<Aldagaia>... comparators) {
        this.listComparators = Arrays.asList(comparators);
    }
    
	@Override
	public int compare(Aldagaia o1, Aldagaia o2) {
		// TODO Auto-generated method stub
		for (Comparator<Aldagaia> comparator : listComparators) {
            int result = comparator.compare(o1, o2);
            if (result != 0) {
                return result;
            }
        }
		return 0;
	}

}
