package EventsPackage;

public class Queue {
    private Node head;
    private Node tail;

    public Queue() {
        this.head = null;
        this.tail = null;
    }

    public void enqueue(Node node) {

        // head --> tail
        //head and tail are the same thing if queue is empty
        if (head == null) {
            tail = node;
            head = tail;
        } else {
            tail.setNext(node);
            tail = node;
        }
    }

    public Node peek() {
        if (isEmpty()) {
            return null;
        } else {
            return head;
        }
    }

    public void dequeue() {
        if (head != null) {
            head = head.getNext();
        }
        if (head == null) {
            tail = null;
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void display() {
        Node current = head;

        while (current != null) {
            System.out.println(current.getName());
            current = current.getNext();
        }
    }
}
