/* *****************************************************************************
 *  Name:    Charlie DiPrima
 *
 *  Description:  Creates a randomized queue data structure (commonly referred to as a bag). An array is 
 *  used since it will be simpler to pull random elements out of the data struture, without the overhead of
 *  transitioning through a linked list.
 *
 *  Written:       12/13/2021
 *  Last updated:  12/13/2021
 *
 *  % javac -cp .:algs4/algs4.jar RandomizedQueue.java
 *  % java -cp .:algs4/algs4.jar RandomizedQueue
 *
 **************************************************************************** */
import java.util.NoSuchElementException;
import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

// Suppress warnings for unchecked casts (java issue - is the correct way to do this)
 @SuppressWarnings("unchecked")

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private int size;
    private Item[] bag = (Item[]) new Object[1];

    // construct an empty randomized queue
    public RandomizedQueue() {
        bag[0] = null;
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        
        // Adds the item to the bag in a random location
        while (true) {
            int n = StdRandom.uniform(bag.length);
            if (bag[n] == null) {
                bag[n] = item;
                size++;
                break;
            }
        }

        // Doubles the size of the array if it becomes over 3/4 full
        if (size >= bag.length * 0.75) {
            int n = bag.length;
            Item[] tempBag = (Item[]) new Object[n];
            for (int i = 0; i < n; i++) {
                tempBag[i] = bag[i];
            }
            bag = (Item[]) new Object[n * 2];
            for (int i = 0; i < n; i++) {
                bag[i] = tempBag[i];
            }
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        
        Item item;

        while (true) {
            int n = StdRandom.uniform(bag.length);
            if (bag[n] != null) {
                item = bag[n];
                bag[n] = null;
                size--;
                break;
            }
        }

        // Halves the size of the array if it becomes 1/4 full
        if (size < bag.length * 0.25) {
            int n = bag.length;
            Item[] tempBag = (Item[]) new Object[n];

            for (int i = 0; i < n; i++) {
                tempBag[i] = bag[i];
            }

            bag = (Item[]) new Object[n / 2];

            int j = 0;
            for (int i = 0; i < n; i++) {
                if (tempBag[i] != null) {
                    bag[j] = tempBag[i];
                    j++;
                }
            }
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        
        while (true) {
            int n = StdRandom.uniform(bag.length);
            if (bag[n] != null) {
                return bag[n];
            }
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
        // Establish range of numbers (0 - n) before creating iterator, so that we are creating new unique numbers
    }

    private class ListIterator implements Iterator<Item> {
        int n = 0;
        
        public boolean hasNext() {
            while (true) {
                if (n < bag.length && bag[n] == null) {
                    n++;
                }
                else {
                    break;
                }
            }
            return n < bag.length;
        }

        public void remove() {
            throw new UnsupportedOperationException("This method not allowed");
        }

        public Item next() {
            if (isEmpty()) {
                throw new NoSuchElementException("No items in randomized queue");
            }
            
            Item item = bag[n];
            n++;
            return item;   
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> bag = new RandomizedQueue<Integer>();
        
        bag.enqueue(42);
        bag.enqueue(30);
        bag.enqueue(36);
        for (Integer item : bag) {
            StdOut.println(item);
        }
        bag.enqueue(7);
        bag.enqueue(48);
        bag.enqueue(29);
        bag.enqueue(29);
        for (Integer item : bag) {
            StdOut.println(item);
        }
    }
}