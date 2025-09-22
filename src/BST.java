//∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗
//∗ @file: BST.java
//∗ @description: This is a BST tree program that connects nodes to a tree
//∗ @author: Eric Gao
//∗ @date: September 22, 2025
//∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗
import java.util.*;

public class BST<T extends Comparable <? super T>> implements Iterable<T> {
    private Node<T> root;
    private int size;

    // Implement the constructor
    public BST(){
        this.root = null;
        this.size = 0;
    }
    // Implement the clear method
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    // Implement the size method
    public int size () {
        return this.size;
    }

    // Implement the insert method

    public void insert (T val) {
        if (val == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            root = new Node<>(val);
            size ++;
            return;
        }
        Node<T> current = root;
        Node<T> parent = null;
        int compare = 0;
        while (current != null) {
            parent = current;
            compare = val.compareTo(current.getValue());
            if (compare == 0) {
                return;
            }
            if(compare < 0){
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
        Node<T> New = new Node<>(val);
        if (compare < 0) {
            parent.setLeft(New);
        } else {
            parent.setRight(New);
        }
        size++;

    }

    // Implement the remove method
    public T remove (T val) {
        if (val == null) {
            throw new IllegalArgumentException();
        }
        Node<T> current = root;
        Node<T> parent = null;
        while (current != null) {
            int compare = val.compareTo(current.getValue());
            if (compare == 0) {
                break;
            }
            parent = current;
            if (compare < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
        if (current == null) {
            return null;
        }
        T removedVal = current.getValue();
        // If node has two children, replace its value with in-order successor, then remove successor.
        if (current.hasLeft() && current.hasRight()) {
            Node<T> successorParent = current;
            Node<T> successor = current.getRight();
            while (successor.getLeft() != null) {
                successorParent = successor;
                successor = successor.getLeft();
            }

            current.setValue(successor.getValue());
            current = successor;
            parent = successorParent;
        }

        // Splice out node with at most one child.
        Node<T> child = null;
        if (current.getLeft() != null) {
            child = current.getLeft();
        } else {
            child = current.getRight();
        }

        if (parent == null) {
            root = child;
        } else if (parent.getLeft() == current) {
            parent.setLeft(child);
        } else {
            parent.setRight(child);
        }

        size --;
        return removedVal;
    }

    // Implement the search method
    public boolean search (T value) {
        Node<T> n = root;
        while (n != null) {
            int cmp = value.compareTo(n.getValue());
            if (cmp == 0) return true;
            n = (cmp < 0) ? n.getLeft() : n.getRight();
        }
        return false;
    }

    // Implement the iterator method

    private void preOrder(Node<T> o, List<T> list) {
        if (o == null) return;
        list.add(o.getValue());
        preOrder(o.getLeft(), list);
        preOrder(o.getRight(), list);
    }

    private void inorder(Node<T> o, List<T> list) {
        if (o == null) return;
        inorder(o.getLeft(), list);
        list.add(o.getValue());
        inorder(o.getRight(), list);
    }

    private void postOrder(Node<T> o, List<T> list) {
        if (o == null) return;
        postOrder(o.getLeft(), list);
        postOrder(o.getRight(), list);
        list.add(o.getValue());
    }

    @Override
    public Iterator<T> iterator() {
        return new inOrderIterator<>(root);
    }

    // Implement the BSTIterator class
    private static class inOrderIterator <T extends Comparable<? super T>> implements Iterator<T> {
        private Stack<Node<T>> stack = new Stack<>();

        // Initializes the stack with the left spine from root.
        public inOrderIterator(Node<T>root)
        {
            stack = new Stack<>();
            pushAllLeft(root);
        }

        // True if there is another element to iterate.
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        // Pops next in-order node; pushes right child's left spine if present.
        public T next() {
            if (stack.isEmpty()) {
                throw new NoSuchElementException();
            }
            Node<T> node = stack.pop();
            if (node.getRight() != null) {
                pushAllLeft(node.getRight());
            }
            return node.getValue();
        }

        // Pushes a node and all its left descendants onto the stack.
        private void pushAllLeft(Node<T> node) {
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }
        }

    }

}
