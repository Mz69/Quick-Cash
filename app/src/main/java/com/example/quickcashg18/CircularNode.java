package com.example.quickcashg18;

/**
 * Circular linked list without duplicates.
 */
public class CircularNode<T> {
    private T data;
    private CircularNode<T> next;
    private CircularNode<T> prev;

    public CircularNode() {
        this.data = null;
        this.next = null;
        this.prev = null;
    }

    public CircularNode(T data) {
        this.data = data;
        this.next = this;
        this.prev = this;
    }

    public T getData() { return data; }

    private void setNext(CircularNode<T> node) {
        this.next = node;
    }

    private void setPrev(CircularNode<T> node) {
        this.prev = node;
    }

    private void setData(T data) {
        this.data = data;
    }

    public CircularNode<T> getNext() {
        return this.next;
    }
    public CircularNode<T> getPrev() { return this.prev; }

    public boolean isEmpty() {
        return this.next == null;
    }

    public boolean contains(T item) {
        if (this.data == null) { return false; }
        CircularNode<T> curr = this;
        do {
            if (curr.getData().equals(item)) {
                return true;
            }
        } while (!curr.getData().equals(getData()));
        return false;
    }

    public void add(T item) {
        if (this.next == null) {
            this.data = item;
            this.next = this;
            this.prev = this;
        }

        if (!contains(item)) {
            CircularNode<T> itemNode = new CircularNode<T>();
            itemNode.setData(item);
            CircularNode<T> itemNodeNext = getNext();
            itemNodeNext.setPrev(itemNode);
            setNext(itemNode);
            itemNode.setNext(itemNodeNext);
            itemNode.setPrev(this);
        }
    }

    public void enumerate() {
        CircularNode<T> curr = this;
        System.out.println(curr.getData());
        while (!(curr = curr.getNext()).getData().equals(getData())) {
            System.out.println(curr.getData());
        }
    }

}
