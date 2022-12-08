package prereqchecker;

import java.util.*;

public class ArrayLists { 
    
    ArrayList<ArrayLists> prereq;
    String courseId;
    boolean isComplete = false;
    
    public ArrayLists (String name) {
        
        courseId = name;
        prereq = new ArrayList<ArrayLists>();

    }

    public void addEdge(ArrayLists list) {
        prereq.add(list);
    }

}