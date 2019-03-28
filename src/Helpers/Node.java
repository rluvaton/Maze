package Helpers;

import java.util.LinkedList;

public abstract class Node<T> {
    public T id;

    protected LinkedList<Node<T>> adjacent = new LinkedList<>();

    protected Node(T id) {
        if(id == null) {
            throw new IllegalArgumentException("id Cant be null");
        }
        this.id = id;
    }

    public Node(T id, LinkedList<Node<T>> adjacent) {
        if(id == null) {
            throw new IllegalArgumentException("id Cant be null");
        }
        this.id = id;
        this.adjacent = adjacent;
    }

    public abstract boolean equals(Node<T> node);

    public LinkedList<Node<T>> getAdjacent() {
        return adjacent;
    }

    public void setAdjacent(LinkedList<Node<T>> adjacent) {
        this.adjacent = adjacent;
    }


}
