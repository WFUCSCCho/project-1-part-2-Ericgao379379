//∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗
//∗ @file: Node.java
//∗ @description: A node program that store values and can have left and right sub nodes
//∗ @author: Eric Gao
//∗ @date: September 22, 2025
//∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗
public class Node <T extends Comparable<? super T>> implements Comparable<Node<T>> {
    private T value;
    private Node<T> left;
    private Node<T> right;

    // Implement the constructor
    public Node(T value) {
        this.value = value;
    }

    // Implement the setElement method
    public void setValue(T value) {
        this.value = value;
    }

    // Implement the setLeft method
    public void setLeft(Node<T> left) {
        this.left = left;
    }

    // Implement the setRight method
    public void setRight(Node<T> right) {
        this.right = right;
    }

    // Implement the getLeft method
    public Node<T> getLeft() {
        return left;
    }

    // Implement the getRight method
    public Node<T> getRight() {
        return right;
    }

    // Implement the getElement method
    public T getValue() {
        return value;
    }

    // Implement the isLeaf method
    public boolean isLeaf(){
        return left == null && right == null;
    }

    // Returns true if a left child exists; false otherwise.
    public boolean hasLeft () {
        if (left != null) {
            return true;
        }
        return false;
    }

    // Returns true if a right child exists; false otherwise.
    public boolean hasRight () {
        if (right != null) {
            return true;
        }
        return false;
    }

    @Override
    // The required compareTo method
    public int compareTo(Node<T> o) {
        return this.value.compareTo(o.value);
    }
}
