package prereqchecker;

import java.util.*;

public class HashMaps { 
    
    //HashMap<String, ArrayLists> hashm = new HashMap<String, ArrayLists>();
    private HashMap<String, ArrayLists> hashm;

    //constructor
    public HashMaps (String file) {
        
        StdIn.setFile(file);

        hashm = new HashMap<String, ArrayLists>();
        int numCourses = StdIn.readInt();
        StdIn.readLine();
        for (int i = 0; i < numCourses; i++) {
            
            String courseId = StdIn.readLine();
            hashm.put(courseId, new ArrayLists(courseId));

        }

        int numPrereqWithCourses = StdIn.readInt();
        StdIn.readLine();
        for (int i = 0; i < numPrereqWithCourses; i++) {
            
            String one = StdIn.readString();
            String two = StdIn.readString();
            hashm.get(one).addEdge(hashm.get(two));

        }

        
    }
    

//
    public void outputAdjList(String file) {
        
        StdOut.setFile(file);

        for (ArrayLists one : hashm.values()) {
            StdOut.print(one.courseId + " ");
            for (ArrayLists two : one.prereq) {
                StdOut.print(two.courseId + " ");
            }
            StdOut.println();
        }

    }


//
    public boolean isValidPrereq(String file) {
        
        StdIn.setFile(file);
        return checkPossible(hashm.get(StdIn.readLine()), hashm.get(StdIn.readLine()));
        
    }

    public boolean checkPossible(ArrayLists constant, ArrayLists ptr) {
        
        ptr.isComplete = true;
        
        if (ptr.prereq.size() > 0) {
            for (ArrayLists ptrPrereq : ptr.prereq) {
                if (ptrPrereq.isComplete == false) {
                    if (checkPossible(constant, ptrPrereq) == false) return false;
                }
            }
        }

        if (constant.equals(ptr)) return false;
        else return true;

    }


//
    public void eligibleMethod(String inputFile, String outputFile) {

        StdIn.setFile(inputFile);
        StdOut.setFile(outputFile);

        int numCourses = StdIn.readInt();
        StdIn.readLine();
        while (numCourses > 0) {
            transitivityImplication(hashm.get(StdIn.readLine()));
            numCourses--;
        }

        for (ArrayLists ptr : hashm.values()) {
            if (ptr.isComplete == false && returnEligible(ptr) == true) StdOut.println(ptr.courseId);
        }

    }

    public void transitivityImplication(ArrayLists ptr) {
        
        ptr.isComplete = true;
        if (ptr.prereq.size() > 0) {
            for (ArrayLists prereqTrav : ptr.prereq) {
                if (prereqTrav.isComplete == false) transitivityImplication(prereqTrav);
            }
        }

    }

    public boolean returnEligible(ArrayLists ptr) {

        for (ArrayLists ptrPrereq : ptr.prereq) {
            if (ptrPrereq.isComplete == false) {
                return false;
            }
        }

        return true;

    }


//
    public void needToTakeMethod(String inputFile, String outputFile) {

        StdIn.setFile(inputFile);
        StdOut.setFile(outputFile);

        ArrayLists targetCourse = hashm.get(StdIn.readLine());
        int numTakenCourses = StdIn.readInt();
        StdIn.readLine();

        while (numTakenCourses > 0) {
            transitivityImplication(hashm.get(StdIn.readLine()));
            numTakenCourses--;
        }

        targetCourse.isComplete = true;
        ArrayList<String> output = new ArrayList<String>();
        checkCompleted(targetCourse, output);

        for (String courseIdTrav : output) {
            StdOut.println(courseIdTrav);
        }

    }

    public void checkCompleted(ArrayLists ptr, ArrayList<String> output) {

        if (ptr.isComplete == false) output.add(ptr.courseId);

        ptr.isComplete = true;
        if (ptr.prereq.size()>0) {
            for (ArrayLists prereqTrav : ptr.prereq) {
                if (prereqTrav.isComplete == false) checkCompleted(prereqTrav, output);
            }
        }
        
    }


//
    public void schedulePlan(String inputFile, String outputFile) {
        
        StdIn.setFile(inputFile);
        StdOut.setFile(outputFile);

        ArrayLists targetCourse = hashm.get(StdIn.readLine());
        int numTakenCourses = StdIn.readInt();
        StdIn.readLine();

        while (numTakenCourses > 0) {
            transitivityImplication(hashm.get(StdIn.readLine()));
            numTakenCourses--;
        }

        HashMap<ArrayLists, Integer> schedule = new HashMap<ArrayLists, Integer>();
        int numSemesters = checkSemester(schedule, -1, targetCourse);
        schedule.remove(targetCourse);

        ArrayList<ArrayList<ArrayLists>> courseList = new ArrayList<ArrayList<ArrayLists>>();
        //System.out.println("poopoo:" + numSemesters);
        for (int i = 0; i < numSemesters + 1; i++) {
            courseList.add(new ArrayList<ArrayLists>());
        }
        //System.out.println("peepee:" + courseList.size());
        for (Map.Entry<ArrayLists, Integer> toTake : schedule.entrySet()) {
            courseList.get(toTake.getValue()).add(toTake.getKey());
        }

        
        StdOut.println(courseList.size());
        for (int i = courseList.size(); i > 0; i--) {
            for (ArrayLists ptrCourse : courseList.get(i-1)) StdOut.print(ptrCourse.courseId + " ");
            StdOut.println();
        }

    }

    public int checkSemester(HashMap<ArrayLists, Integer> schedule, int semester, ArrayLists course) {
        
        course.isComplete = true;
        int numSemesters = semester;
        
        schedule.put(course, semester);
        if (course.prereq.size() > 0) {
            for (ArrayLists ptrPrereq : course.prereq) {
                //this part is kinda weird
                //numSemesters Math.max checkSemester
                if (!ptrPrereq.isComplete || (schedule.containsKey(ptrPrereq) && !(schedule.get(ptrPrereq) > semester))) {
                    numSemesters = Math.max(numSemesters, checkSemester(schedule, semester+1,ptrPrereq));
                }

            }
        }
        
        return numSemesters;

    }

}