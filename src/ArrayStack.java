/**
 * The ArrayStack class represents a stack implemented with an array data
 * structure. It implements the StackADT, and is of generic type T. Uniquely,
 * elements are added to the end of the array, with newer elements added to the
 * right most empty index.
 * 
 * @author Gabby Niamat
 *
 */

public class ArrayStack<T> implements StackADT<T> {
    private T[] array;
    private int top;

    /**
     * Constructor method that initializes a generic array with a new array object
     * of 10 slots, and sets top to the last index.
     */
    public ArrayStack() {
        array = (T[]) new Object[10];
        top = 9;
    }

    /**
     * Constructor method (overloaded) that initializes a generic array with a new
     * object of specified slots, and sets top to the last index.
     * 
     * @param n the number of slots for the array.
     */
    public ArrayStack(int n) {
        array = (T[]) new Object[n];
        top = n - 1;
    }

    /**
     * Modifier method that adds input parameter to top of stack and updates top.
     * 
     * @param element
     */
    @Override
    public void push(T element) {
        if (size() == array.length - 1) { // if the stack is full, call expandCapacity() method to increase size of the
                                          // array.
            expandCapacity();
        }
        array[top] = element; // set top slot of array to new specified element
        top--; // set new top to be the next empty index

    }

    /**
     * Private helper method that expands the capacity of an array.
     */
    private void expandCapacity() {
        T[] expanded = (T[]) (new Object[array.length + 5]); // create new generic array and set it to size of old array
                                                             // plus 5
        top = expanded.length - array.length; // update top to be the length of larger array - length of smaller array
                                              // (as old array is full)
        int count = 1; // initialize counter variable
        for (int i = array.length - 1; i >= 0; --i) { // iterate through new array and set each index to the value of
                                                      // the old array at the corresponding index (from right to left)
            expanded[expanded.length - count] = array[i];
            ++count;
        }
        array = expanded; // set old array to equal new array with updated values, and additional empty
                          // index's
    }

    /**
     * Modifier method that removes and returns the element from the top of the
     * stack, and updates top.
     * 
     * @return result; the popped element from the stack.
     */
    @Override
    public T pop() throws StackException {
        if (isEmpty()) { // if stack is empty, throw a new StackException
            throw new StackException("Stack");
        }
        T result = array[top + 1]; // store result variable with popped element
        array[top + 1] = null; // set array at popped element index to null (removing it)
        top++;
        return result;
    }

    /**
     * Accessor method that returns the element at the top of the stack.
     * 
     * @return value of the array at the top of the stack.
     */
    @Override
    public T peek() throws StackException {
        if (isEmpty()) { // if stack is empty throw a new StackException
            throw new StackException("Stack");
        }
        return array[top + 1];
    }

    /**
     * Accessor method that returns whether or not the stack is empty.
     * 
     * @return true if the stack contains 0 elements, else false;
     */
    @Override
    public boolean isEmpty() {
        return top == array.length - 1; // return whether the top index equals the array's last index
    }

    /**
     * Accessor method that returns the number of elements in the stack.
     * 
     * @return count; number of elements in the stack.
     */
    @Override
    public int size() {
        int count = 0;
        for (int i = array.length - 1; i >= 0; i--) { // for loop that increments count variable if the array at index
                                                      // is not null
            if (array[i] != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Accessor ethod that returns the number of slots in the array.
     * 
     * @return length of the array.
     */
    public int getLength() {
        return array.length;
    }

    /**
     * Accessor method that returns the top index of the array.
     * 
     * @return top variable.
     */
    public int getTop() {
        return top;
    }

    /**
     * Method that prints the elements in the stack from top to bottom, in specified
     * format.
     * 
     * @return str that contains all elements in the stack, or specified message if
     *         empty.
     */
    public String toString() {
        if (isEmpty()) {
            return "The stack is empty.";
        }

        String str = "Stack: ";
        for (int i = getTop() + 1; i < array.length; i++) { // loop through the array starting with last index
            if (array[i] != null && i != array.length - 1) { // if the array at index isn't null and this is not the
                                                             // last index
                str += array[i] + ", ";
            } else {
                str += array[i] + "."; // add . after the last element in the array
            }
        }
        return str;
    }
}