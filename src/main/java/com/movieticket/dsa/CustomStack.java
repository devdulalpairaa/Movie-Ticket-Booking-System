package com.movieticket.dsa;

import java.util.EmptyStackException;

/**
 * A custom, array-backed Stack implementation (LIFO) written from scratch
 * -- no java.util.Stack / Deque used. Used by BookingService to support
 * "Undo Last Booking".
 *
 * @param <T> type of elements stored
 */
public class CustomStack<T> {

    private Object[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    public CustomStack() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public void push(T item) {
        ensureCapacity();
        elements[size++] = item;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        T item = (T) elements[size - 1];
        elements[--size] = null;
        return item;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return (T) elements[size - 1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] bigger = new Object[elements.length * 2];
            System.arraycopy(elements, 0, bigger, 0, elements.length);
            elements = bigger;
        }
    }
}
