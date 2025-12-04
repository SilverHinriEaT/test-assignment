/*
 * Copyright (c) 2014, NTUU KPI, Computer systems department and/or its affiliates. All rights reserved.
 * NTUU KPI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 */

package ua.kpi.comsys.test2.implementation;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ua.kpi.comsys.test2.NumberList;

/**
 * Custom implementation of NumberList interface.
 * Doubly linked linear list representing a number in ternary (base 3) notation.
 * 
 * Parameters (calculated from student number 6 in group list):
 * - C3 = 6 % 3 = 0: Doubly linked linear list
 * - C5 = 6 % 5 = 1: Ternary (base 3) notation
 * - C7 = 6 % 7 = 6: Bitwise OR operation
 * - Additional system: (1+1) % 5 = 2: Octal (base 8) notation
 *
 * @author Rostyslav Diadiushka, IA-33, № залікової книжки 6
 *
 */
public class NumberListImpl implements NumberList {
    
    /**
     * Node class for doubly linked list
     */
    private class Node {
        Byte value;
        Node next;
        Node prev;
        
        Node(Byte value) {
            this.value = value;
            this.next = null;
            this.prev = null;
        }
    }
    
    private Node head;
    private Node tail;
    private int size;
    private int base; // Current base of notation (3 for ternary, 8 for octal)

    /**
     * Default constructor. Returns empty <tt>NumberListImpl</tt>
     */
    public NumberListImpl() {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.base = 3; // Default base is ternary
    }


    /**
     * Constructs new <tt>NumberListImpl</tt> by <b>decimal</b> number
     * from file, defined in string format.
     *
     * @param file - file where number is stored.
     */
    public NumberListImpl(File file) {
        this();
        if (file == null || !file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line != null && !line.trim().isEmpty()) {
                initializeFromDecimal(line.trim());
            }
        } catch (IOException e) {
            // File read error, list remains empty
        }
    }


    /**
     * Constructs new <tt>NumberListImpl</tt> by <b>decimal</b> number
     * in string notation.
     *
     * @param value - number in string notation.
     */
    public NumberListImpl(String value) {
        this();
        if (value != null && !value.trim().isEmpty()) {
            initializeFromDecimal(value.trim());
        }
    }
    
    /**
     * Helper method to initialize list from decimal string
     */
    private void initializeFromDecimal(String decimalStr) {
        // Validate string contains only digits
        if (!decimalStr.matches("\\d+")) {
            return;
        }
        
        // Convert from decimal to ternary
        java.math.BigInteger num = new java.math.BigInteger(decimalStr);
        if (num.signum() <= 0) {
            return;
        }
        
        java.math.BigInteger base = java.math.BigInteger.valueOf(3);
        while (num.signum() > 0) {
            Byte digit = (byte) num.remainder(base).intValue();
            addFirst(digit);
            num = num.divide(base);
        }
    }
    
    /**
     * Add element to the beginning of the list
     */
    private void addFirst(Byte value) {
        Node newNode = new Node(value);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }


    /**
     * Saves the number, stored in the list, into specified file
     * in <b>decimal</b> scale of notation.
     *
     * @param file - file where number has to be stored.
     */
    public void saveList(File file) {
        if (file == null) {
            return;
        }
        try (java.io.FileWriter fw = new java.io.FileWriter(file)) {
            fw.write(toDecimalString());
        } catch (IOException e) {
            // File write error
        }
    }


    /**
     * Returns student's record book number, which has 4 decimal digits.
     *
     * @return student's record book number.
     */
    public static int getRecordBookNumber() {
        return 6; // Student number 6 from group list
    }


    /**
     * Returns new <tt>NumberListImpl</tt> which represents the same number
     * in other scale of notation, defined by personal test assignment.<p>
     *
     * Does not impact the original list.
     * Converts from ternary to octal (base 8).
     *
     * @return <tt>NumberListImpl</tt> in octal scale of notation.
     */
    public NumberListImpl changeScale() {
        // First convert current base to decimal
        java.math.BigInteger decimal = toDecimal();
        if (decimal.signum() <= 0) {
            return new NumberListImpl();
        }
        
        // Then convert decimal to octal (base 8)
        NumberListImpl result = new NumberListImpl();
        result.base = 8; // Set result to octal
        java.math.BigInteger baseValue = java.math.BigInteger.valueOf(8);
        while (decimal.signum() > 0) {
            Byte digit = (byte) decimal.remainder(baseValue).intValue();
            result.addFirst(digit);
            decimal = decimal.divide(baseValue);
        }
        return result;
    }
    
    /**
     * Convert current list (with base) to BigInteger (decimal)
     */
    private java.math.BigInteger toDecimal() {
        java.math.BigInteger result = java.math.BigInteger.ZERO;
        java.math.BigInteger baseMult = java.math.BigInteger.ONE;
        java.math.BigInteger baseValue = java.math.BigInteger.valueOf(base);
        
        Node current = tail;
        while (current != null) {
            java.math.BigInteger digit = java.math.BigInteger.valueOf(current.value);
            result = result.add(digit.multiply(baseMult));
            baseMult = baseMult.multiply(baseValue);
            current = current.prev;
        }
        return result;
    }


    /**
     * Returns new <tt>NumberListImpl</tt> which represents the result of
     * additional operation, defined by personal test assignment.<p>
     *
     * Does not impact the original list.
     * Performs bitwise OR of two numbers in ternary representation.
     *
     * @param arg - second argument of additional operation
     *
     * @return result of additional operation.
     */
    public NumberListImpl additionalOperation(NumberList arg) {
        if (arg == null || !(arg instanceof NumberListImpl)) {
            return null;
        }
        
        NumberListImpl other = (NumberListImpl) arg;
        
        // Convert both to decimal
        java.math.BigInteger num1 = this.toDecimal();
        java.math.BigInteger num2 = other.toDecimal();
        
        // Perform bitwise OR
        java.math.BigInteger result = num1.or(num2);
        
        // Convert result back to ternary
        if (result.signum() <= 0) {
            return new NumberListImpl();
        }
        
        NumberListImpl resultList = new NumberListImpl();
        java.math.BigInteger base = java.math.BigInteger.valueOf(3);
        while (result.signum() > 0) {
            Byte digit = (byte) result.remainder(base).intValue();
            resultList.addFirst(digit);
            result = result.divide(base);
        }
        return resultList;
    }


    /**
     * Returns string representation of number, stored in the list
     * in <b>decimal</b> scale of notation.
     *
     * @return string representation in <b>decimal</b> scale.
     */
    public String toDecimalString() {
        if (isEmpty()) {
            return "";
        }
        return toDecimal().toString();
    }


    @Override
    public String toString() {
        if (isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Node current = head;
        while (current != null) {
            sb.append(current.value);
            current = current.next;
        }
        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NumberListImpl)) return false;
        NumberListImpl other = (NumberListImpl) o;
        
        if (this.size != other.size) return false;
        
        Node current1 = this.head;
        Node current2 = other.head;
        while (current1 != null) {
            if (!current1.value.equals(current2.value)) return false;
            current1 = current1.next;
            current2 = current2.next;
        }
        return true;
    }


    @Override
    public int size() {
        return size;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }


    @Override
    public Iterator<Byte> iterator() {
        return new Iterator<Byte>() {
            Node current = head;
            
            @Override
            public boolean hasNext() {
                return current != null;
            }
            
            @Override
            public Byte next() {
                if (current == null) throw new java.util.NoSuchElementException();
                Byte value = current.value;
                current = current.next;
                return value;
            }
        };
    }


    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        Node current = head;
        int i = 0;
        while (current != null) {
            arr[i++] = current.value;
            current = current.next;
        }
        return arr;
    }


    @Override
    public <T> T[] toArray(T[] a) {
        // Not required to implement
        return null;
    }


    @Override
    public boolean add(Byte e) {
        if (e == null) return false;
        Node newNode = new Node(e);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
        return true;
    }


    @Override
    public boolean remove(Object o) {
        Node current = head;
        while (current != null) {
            if (current.value.equals(o)) {
                removeNode(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }


    @Override
    public boolean addAll(Collection<? extends Byte> c) {
        for (Byte b : c) {
            add(b);
        }
        return true;
    }


    @Override
    public boolean addAll(int index, Collection<? extends Byte> c) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        for (Byte b : c) {
            add(index++, b);
        }
        return true;
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Node current = head;
        while (current != null) {
            Node next = current.next;
            if (c.contains(current.value)) {
                removeNode(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node current = head;
        while (current != null) {
            Node next = current.next;
            if (!c.contains(current.value)) {
                removeNode(current);
                modified = true;
            }
            current = next;
        }
        return modified;
    }


    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
        base = 3; // Reset to default ternary base
    }


    @Override
    public Byte get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return getNode(index).value;
    }


    @Override
    public Byte set(int index, Byte element) {
        if (element == null) throw new IllegalArgumentException();
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node node = getNode(index);
        Byte oldValue = node.value;
        node.value = element;
        return oldValue;
    }


    @Override
    public void add(int index, Byte element) {
        if (element == null) throw new IllegalArgumentException();
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        
        if (index == 0) {
            addFirst(element);
        } else if (index == size) {
            add(element);
        } else {
            Node nodeAtIndex = getNode(index);
            Node newNode = new Node(element);
            newNode.next = nodeAtIndex;
            newNode.prev = nodeAtIndex.prev;
            nodeAtIndex.prev.next = newNode;
            nodeAtIndex.prev = newNode;
            size++;
        }
    }


    @Override
    public Byte remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node node = getNode(index);
        Byte value = node.value;
        removeNode(node);
        return value;
    }


    @Override
    public int indexOf(Object o) {
        Node current = head;
        int index = 0;
        while (current != null) {
            if (current.value.equals(o)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }


    @Override
    public int lastIndexOf(Object o) {
        Node current = tail;
        int index = size - 1;
        while (current != null) {
            if (current.value.equals(o)) {
                return index;
            }
            current = current.prev;
            index--;
        }
        return -1;
    }


    @Override
    public ListIterator<Byte> listIterator() {
        return listIterator(0);
    }


    @Override
    public ListIterator<Byte> listIterator(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        return new ListIterator<Byte>() {
            Node current = (index == 0) ? head : getNode(index);
            int currentIndex = index;
            
            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }
            
            @Override
            public Byte next() {
                if (!hasNext()) throw new java.util.NoSuchElementException();
                Byte value = current.value;
                current = current.next;
                currentIndex++;
                return value;
            }
            
            @Override
            public boolean hasPrevious() {
                return currentIndex > 0;
            }
            
            @Override
            public Byte previous() {
                if (!hasPrevious()) throw new java.util.NoSuchElementException();
                current = (current == null) ? tail : current.prev;
                currentIndex--;
                return current.value;
            }
            
            @Override
            public int nextIndex() {
                return currentIndex;
            }
            
            @Override
            public int previousIndex() {
                return currentIndex - 1;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void set(Byte e) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void add(Byte e) {
                throw new UnsupportedOperationException();
            }
        };
    }


    @Override
    public List<Byte> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        NumberListImpl sublist = new NumberListImpl();
        for (int i = fromIndex; i < toIndex; i++) {
            sublist.add(get(i));
        }
        return sublist;
    }


    @Override
    public boolean swap(int index1, int index2) {
        if (index1 < 0 || index1 >= size || index2 < 0 || index2 >= size) {
            return false;
        }
        if (index1 == index2) return true;
        
        Node node1 = getNode(index1);
        Node node2 = getNode(index2);
        
        Byte temp = node1.value;
        node1.value = node2.value;
        node2.value = temp;
        
        return true;
    }


    @Override
    public void sortAscending() {
        if (size <= 1) return;
        
        // Bubble sort
        for (int i = 0; i < size - 1; i++) {
            Node current = head;
            for (int j = 0; j < size - i - 1; j++) {
                if (current.value > current.next.value) {
                    Byte temp = current.value;
                    current.value = current.next.value;
                    current.next.value = temp;
                }
                current = current.next;
            }
        }
    }


    @Override
    public void sortDescending() {
        if (size <= 1) return;
        
        // Bubble sort descending
        for (int i = 0; i < size - 1; i++) {
            Node current = head;
            for (int j = 0; j < size - i - 1; j++) {
                if (current.value < current.next.value) {
                    Byte temp = current.value;
                    current.value = current.next.value;
                    current.next.value = temp;
                }
                current = current.next;
            }
        }
    }


    @Override
    public void shiftLeft() {
        if (size <= 1) return;
        
        Byte firstValue = head.value;
        Node current = head;
        while (current.next != null) {
            current.value = current.next.value;
            current = current.next;
        }
        current.value = firstValue;
    }


    @Override
    public void shiftRight() {
        if (size <= 1) return;
        
        Byte lastValue = tail.value;
        Node current = tail;
        while (current.prev != null) {
            current.value = current.prev.value;
            current = current.prev;
        }
        current.value = lastValue;
    }
    
    /**
     * Helper method to get node at index
     */
    private Node getNode(int index) {
        if (index < size / 2) {
            Node current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        } else {
            Node current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
    }
    
    /**
     * Helper method to remove a node
     */
    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        
        size--;
    }
}
