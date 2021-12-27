/* *****************************************************************************
 *  Name:    Charlie DiPrima
 *
 *  Description:  Creates a deque data structure. A linked list is used for this due to the fact that
 *  the items need to be ordered and it is simpler to add/remove items from a linked list.
 *
 *  Written:       12/13/2021
 *  Last updated:  12/13/2021
 *
 *  % javac -cp .:algs4/algs4.jar Deque.java
 *  % java -cp .:algs4/algs4.jar Deque
 *
 **************************************************************************** */
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int size;

    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        if (first == null) {
            return true;
        }
        return false;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null argument to addFirst");
        }

        // Create new node
        Node currentNode = new Node();
        currentNode.item = item;
        currentNode.previous = null;

        // Link the new node in to the front of the deque
        if (size == 0) {
            currentNode.next = null;
            last = currentNode;
        }
        else {
            currentNode.next = first;
            first.previous = currentNode;
        }
        first = currentNode;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null argument to addLast");
        }

        // Create new node
        Node currentNode = new Node();
        currentNode.item = item;
        currentNode.next = null;

        // Link the new node in to the back of the deque
        if (size == 0) {
            currentNode.previous = null;
            first = currentNode;
        }
        else {
            currentNode.previous = last;
            currentNode.previous.next = currentNode;
        }   
        last = currentNode;     
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("No items in deque");
        }
        
        // Remove the first node from the deque amd return that first item
        Item oldFirstItem = first.item;
        if (size == 1) {
            first = null;
            last = null;
        }
        else {
            first.item = null;
            first = first.next;
            first.previous = null;
        }
        size--;
        return oldFirstItem;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("No items in deque");
        } 

        // Remove the last node from the deque amd return that last item
        Item oldLastItem = last.item;
        if (size == 1) {
            first = null;
            last = null;
        }
        else {
            last.item = null;
            last = last.previous;
            last.next = null;
        }
        size--;
        return oldLastItem;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }
    
    private class ListIterator implements Iterator<Item> {
        Node currentNode = first;

        public boolean hasNext() {
            return currentNode != null;
        }

        public void remove() {
            throw new UnsupportedOperationException("This method not allowed");
        }

        public Item next() {
            if (currentNode == null) {
                throw new NoSuchElementException("No items in deque");
            }
            Item item = currentNode.item;
            currentNode = currentNode.next;
            return item;
        }
    }

    // unit dequeing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        
        int n = 10;

        StdOut.println("Testing addFirst / removeLast");

        // Add items to the deque
        for (int i = 0; i < n; i++) {
            deque.addFirst(i);
        }
    
        StdOut.println(deque.size());

        // Iteration - Option #1
        for (Integer item : deque) {
            StdOut.println(item);
        }

        // Iteration - Option #2
        Iterator<Integer> iterator = deque.iterator();
        while (iterator.hasNext()) {
            Integer item = iterator.next();
            StdOut.println(item);
        }

        // Iteration - Option #3
        /* deque.forEach((item) -> {
            StdOut.println(item);
        }); */

        // Remove the items from the deque 
        for (int i = 0; i < n; i++) {
            StdOut.println(deque.removeLast());
        }

        StdOut.println(deque.size());

        StdOut.println("Testing addLast / removeFirst");

        // Add items to the deque
        for (int i = 0; i < n; i++) {
            deque.addLast(i);
        }

        for (Integer item : deque) {
            StdOut.println(item);
        }

        // Remove the items from the deque 
        for (int i = 0; i < n; i++) {
            StdOut.println(deque.removeFirst());
        }
    }
}