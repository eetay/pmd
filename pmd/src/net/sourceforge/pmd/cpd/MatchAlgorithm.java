package net.sourceforge.pmd.cpd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MatchAlgorithm {

    private Map pool = new TreeMap();
    private List code = new ArrayList();
    private List marks = new ArrayList();
    private List matches;
    private CPDListener cpdListener;

    public void add(TokenEntry token, CPDListener cpdListener) {
        if (!pool.containsKey(token)) {
            pool.put(token, token);
        }
        code.add(pool.get(token));
        marks.add(new Mark(code.size(), token.getTokenSrcID(), token.getIndex()));
        this.cpdListener = cpdListener;
    }

    public void findMatches(int min) {
       /*
         Assign sort codes to all the pooled code. This should speed
         up sorting them.
       */
        int count = 1;
        for (Iterator iter = pool.keySet().iterator(); iter.hasNext();) {
           TokenEntry token = (TokenEntry)iter.next();
           token.setSortCode(count++);
        }

        MarkComparator mc = new MarkComparator(cpdListener, code);
        Collections.sort(marks, mc);

        MatchCollector coll = new MatchCollector(marks, code, mc);
        matches = coll.collect(min);
        Collections.sort(matches);
    }

    public Iterator matches() {
        return matches.iterator();
    }
}

