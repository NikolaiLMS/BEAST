package edu.pse.beast.propertychecker;

import java.util.ArrayList;
import java.util.List;

public class CBMC_Result_Wrapper_multiArray {
    private final int index;
    private final String name;
    private final List<ArrayList<Long>> list = new ArrayList<ArrayList<Long>>();
    
    public CBMC_Result_Wrapper_multiArray(int index, String name) {
        this.index = index;
        this.name = name;
    }
    
    public void addTo(int firstIndex, int secondIndex, long toAdd) {
        if (list.size() > firstIndex) {
            addToLongList(list.get(firstIndex), secondIndex, toAdd);
        } else {
            for (int i = list.size(); i < firstIndex; i++) {
                list.add(new ArrayList<Long>());
            }
            addToLongList(list.get(firstIndex), secondIndex, toAdd);
        }
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
    
    public Long[][] getList() {
        Long[][] toReturn;
        if((list != null) && (list.size() > 0) && (list.get(0) != null) && (list.get(0).size() > 0)) {
            toReturn = new Long[list.size()][list.get(0).size()];
        } else {
            toReturn = new Long[0][0];
        }

        
        for (int i = 0; i < toReturn.length; i++) {
            boolean indexWorks = list.size() >= i && list.get(i) != null;
            for (int j = 0; j < toReturn[0].length; j++) {
                if (indexWorks && list.get(i).size() >= j && list.get(i).get(j) != null) {
                    toReturn[i][j] = list.get(i).get(j);
                } else {
                    toReturn[i][j] = -1L;
                }
            }
        }
       
        return toReturn;
    }
    
    private void addToLongList(List<Long> list, int indexToAddAt, long toAdd) {
        if (list.size() > indexToAddAt) {
            list.set(indexToAddAt, toAdd);
        } else {
            for (int i = list.size(); i < indexToAddAt; i++) {
                list.add(-1l);
            }
            list.add(toAdd);
        }
    }
}
