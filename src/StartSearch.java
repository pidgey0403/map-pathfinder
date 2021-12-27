
/**
 * The StartSearch class represents the algorithm for searching map cells and 
 * determining valid pathways. It has a constructor method, private method which 
 * determines the priority of mapCells, a bestCell method which determines the next 
 * best cell to visit, and a findPath method that determines the best path to take.
 * @author Gabby Niamat
 *
 */

import java.io.FileNotFoundException;
import java.io.IOException;

public class StartSearch {
    private Map map;

    /**
     * Constructor method that initializes the map object with parameter str,
     * accounting for potential thrown exceptions.
     * 
     * @param str parameter used to initialize the map object.
     */
    public StartSearch(String str) {
        try {
            map = new Map(str);
        } catch (InvalidMapException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Private helper method that determines the priority of current cell's
     * neighbours at a given position.
     * 
     * @param current the current MapCell to be checked for priority of all
     *                neighbours.
     * @param x       the position of the neighbour cell.
     * @return value; the priority of the mapCell's neighbour.
     */
    private int priority(MapCell current, int x) {
        int value = 5; // initialize temporary int variable to lowest priority

        try { // check if current cell's neighbour at position x is an exit cell AND isn't
              // marked.
            if (current.getNeighbour(x).isExit() && !current.getNeighbour(x).isMarked()) {
                value = 1; // assign with highest priority.
            }
        } catch (Exception e) {
        }

        try { // check if current cell's neighbour at position x is a donut cell AND isn't
              // marked.
            if (current.getNeighbour(x).isDonut() && !current.getNeighbour(x).isMarked()) {
                value = 2; // assign with second highest priority.
            }
        } catch (Exception e) {
        }

        try { // check if current cell's neighbour at position x is a crossPath cell AND isn't
              // marked.
            if (current.getNeighbour(x).isCrossPath() && !current.getNeighbour(x).isMarked()) {
                value = 3; // assign with third highest priority.
            }
        } catch (Exception e) {
        }

        try { //// check if current cell's neighbour at position x is a horizontal or vertical
              //// path cell AND isn't marked.
            if ((current.getNeighbour(x).isHorizontalPath() || current.getNeighbour(x).isVerticalPath())
                    && !current.getNeighbour(x).isMarked()) {
                value = 4; // assign with fourth highest priority.
            }
        } catch (Exception e) {
        }
        return value;
    }

    /**
     * Method that determines the best unmarked cell from the current cell to
     * continue the path, while following specified rules.
     * 
     * @param current the current MapCell to be checked.
     * @return the best neighbour cell (of current MapCell), or null if there are no
     *         valid best cells.
     */
    public MapCell bestCell(MapCell current) {
        int[] array = new int[4]; // initialize a temporary int array to hold the priority of each neighbour cell
                                  // of current.

        /*
         * The 4 following try-catch blocks check if current neighbours at index 0-3
         * (inclusive) are a covid cell. Returns null if they are, and catches
         * invalidIndexExceptions if neighbour index is out of bounds.
         */
        try {
            if (current.getNeighbour(0).isCovid()) {
                return null;
            }
        } catch (Exception invalidNeighbourIndexException) {
        }

        try {
            if (current.getNeighbour(1).isCovid()) {
                return null;
            }
        } catch (Exception invalidNeighbourIndexException) {
        }

        try {
            if (current.getNeighbour(2).isCovid()) {
                return null;
            }

        } catch (Exception invalidNeighbourIndexException) {
        }

        try {
            if (current.getNeighbour(3).isCovid()) {
                return null;
            }

        } catch (Exception invalidNeighbourIndexException) {
        }

        /* determine priority for each neighbour */
        int determinePriority; // holds priority of each neighbour cell.
        for (int i = 0; i < 4; i++) {
            determinePriority = priority(current, i); // call the private priority() method to determine the priority
                                                      // for
                                                      // each neighbour.
            array[i] = determinePriority; // store the priority of each neighbour in its corresponding position in the
                                          // array.
        }

        /* ensure that there is at least 1 valid neighbour cell */
        int sum = 0;
        for (int i = 0; i < 4; i++) {
            sum += array[i]; // add the value at each index in array to sum variable.
        }
        if (sum == 20) { // if sum equals 20, all slots in array are 5 (invalid priority), so return
                         // null.
            return null;
        }

        /*
         * determine the neighbour cell to be chosen with highest priority, and lowest
         * index value.
         */
        int lowestNeighbour = 5; // lowestNeighbour priority value
        int currentNeighbour;
        int currentIndex = 0;

        if (current.isCrossPath() || current.isDonut() || current.isStart() || current.isExit()) { // for crossPath,
                                                                                                   // donut, start, or
                                                                                                   // exit cells
            for (int i = 0; i < 4; i++) {
                currentNeighbour = array[i]; // set currentNeighbour cell to the value of the array at index i.
                if (currentNeighbour < lowestNeighbour) { // if currentNeighbour value is LOWER than lowestNeighbour
                                                          // value.
                    lowestNeighbour = currentNeighbour; // update lowestNeighbour value with currentNeighbour.
                    currentIndex = i; // update the currentIndex (of highest priority neighbour) to equal i.
                }
            }
            // current cell CANNOT move vertically (north/south) if it is a horizontal path.
            if ((currentIndex == 0 || currentIndex == 2) && (current.getNeighbour(currentIndex).isHorizontalPath())) {
                return null;
            }
            // current cell CANNOT move horizontally (west/east) if it is a vertical path.
            if ((currentIndex == 1 || currentIndex == 3) && (current.getNeighbour(currentIndex).isVerticalPath())) {
                return null;
            }
            return current.getNeighbour(currentIndex); // if current cell can move to the neighbour cell, return valid
                                                       // neighbour cell.

        } else if (current.isHorizontalPath()) { // if current cell is a horizontal path
            int east = array[1];
            int west = array[3];
            if (east <= west) { // if neighbour at index 1 has higher priority than neighbour at index 3
                return current.getNeighbour(1); // return neighbour at east (index 1)
            } else {
                return current.getNeighbour(3); // else return neighbour at west (index 3)
            }

        } else if (current.isVerticalPath()) { // if current cell is a vertical path, return the vertical neighbour cell
                                               // with higher priority
            int north = array[0];
            int south = array[2];
            if (north <= south) {
                return current.getNeighbour(0);
            } else {
                return current.getNeighbour(2);
            }
        } else {
            return null;
        }
    }

    /**
     * Method that searches for a valid path from starting to destination while
     * following specified rules.
     * 
     * @return actionString which displays pathway taken, and remaining energy after
     *         pathfinding.
     */
    public String findPath() {
        ArrayStack<MapCell> stack = new ArrayStack<>(); // initialize stack stack
        String actionString = ""; // initialize actionString
        boolean found = false; // initialize status flag
        int energyLevel = 10; // initialize energy

        MapCell startCell = map.getStart(); // get starting cell
        stack.push(startCell); // push starting cell onto stack
        startCell.markInStack(); // mark starting cell as inStack
        actionString += startCell.toString() + "-"; // add startCell to actionString

        while (!found && !stack.isEmpty()) {
            MapCell currentCell = stack.peek();
            MapCell nextCell = bestCell(currentCell); // peek the top of stack and determine nextCell by calling
                                                      // bestCell method onto currentCell

            if (nextCell != null && energyLevel > 0) { // if nextCell is valid and enough energy remains
                nextCell.markInStack(); // mark nextCell into stack
                if (nextCell.isExit()) {
                    found = true;
                    --energyLevel; // if nextCell is exit, exit the loop and decrement energy
                }
                if (nextCell.isDonut()) {
                    energyLevel += 3; // if nextCell is donut, increment energy by 3
                }
                if (!found) { // if nextCell is not destination, update actionString, push nextCell onto stack
                              // and mark said cell inStack. decrement energy. (repeat this as necessary)
                    actionString += nextCell + "-";
                    stack.push(nextCell);
                    nextCell.markInStack();
                    --energyLevel;
                }
            } else { // if there are no unmarked cells
                MapCell popped = stack.pop();
                popped.markOutStack(); // pop top cell and mark it out of stack
                try { // update actionString to add back the previous element after pop
                    actionString += stack.peek() + "-";
                } catch (Exception e) {
                }
                if (popped.isDonut()) {
                    energyLevel -= 3; // add back 3 energy if donut cell is popped
                }
                if (!popped.isStart()) {
                    ++energyLevel; // add back 1 energy if popped is starting cell
                }
            }
        }
        while (!stack.isEmpty()) {
            MapCell popped = stack.pop();
            popped.markOutStack(); // while stack is not empty, pop and mark popped cell out of the stack
        }
        return actionString + "E" + energyLevel;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("You must provide the name of the input file");
        }
        String mapFile = args[0];
        StartSearch ss = new StartSearch(mapFile);
        ss.findPath(); // invoke findPath on the StartSearch object
    }
}