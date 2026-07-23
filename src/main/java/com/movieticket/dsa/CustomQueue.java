package com.movieticket.dsa;

import java.util.NoSuchElementException;

/**
 * A custom, circular-array-backed Queue implementation (FIFO) written from
 * scratch -- no java.util.Queue / LinkedList used. Used by BookingService to
 * maintain a waiting list when a show is fully booked.
 *
 * @param <T> type of elements stored
 */
public class CustomQueue<T> {

    private Object[] elements;
    private int front;
    private int rear;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    public CustomQueue() {
        elements = new Object[DEFAULT_CAPACITY];
        front = 0;
        rear = -1;
        size = 0;
    }

    public void enqueue(T item) {
        ensureCapacity();
        rear = (rear + 1) % elements.length;
        elements[rear] = item;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        T item = (T) elements[front];
        elements[front] = null;
        front = (front + 1) % elements.length;
        size--;
        return item;
    }

    @SuppressWarnings("unchecked")
    public T peekFront() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return (T) elements[front];
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
            for (int i = 0; i < size; i++) {
                bigger[i] = elements[(front + i) % elements.length];
            }
            elements = bigger;
            front = 0;
            rear = size - 1;
        }
    }
}
