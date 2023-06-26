package deques;

/**
 * @see Deque
 */
public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back;
    // Feel free to add any additional fields you may need, though.

    public LinkedDeque() {
        size = 0;
        front = new Node<T>(null);
        back = new Node<T>(null);
        front.next = back;
        back.prev = front;
    }

    public void addFirst(T item) {
        size += 1;
        // throw new UnsupportedOperationException("Not implemented yet.");
        Node<T> curr = new Node<T>(item);

        Node<T> temp = front.next;
        curr.next = temp;
        temp.prev = curr;
        curr.prev = front;
        front.next = curr;
    }

    public void addLast(T item) {
        size += 1;
        // throw new UnsupportedOperationException("Not implemented yet.");
        Node<T> curr = new Node<T>(item);

        Node<T> temp = back.prev;
        temp.next = curr;
        curr.prev = temp;
        back.prev = curr;
        curr.next = back;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        // throw new UnsupportedOperationException("Not implemented yet.");
        Node<T> curr = front.next;
        Node<T> temp = front.next.next;
        // remove pointer for curr
        curr.prev = null;
        curr.next = null;
        // connect new pointer for temp
        front.next = temp;
        temp.prev = front;
        return curr.value;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        // throw new UnsupportedOperationException("Not implemented yet.");
        Node<T> curr = back.prev;
        Node<T> temp = back.prev.prev;
        // remove pointer for curr
        curr.prev = null;
        curr.next = null;
        // connect new pointer for temp
        temp.next = back;
        back.prev = temp;

        return curr.value;
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        Node<T> curr;
        // throw new UnsupportedOperationException("Not implemented yet.");
        if (index <= size / 2) {
            curr = front.next;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
        } else {
            curr = back.prev;
            for (int i = 0; i < size - index - 1; i++) {
                curr = curr.prev;
            }
        }
        return curr.value;
    }

    public int size() {
        return size;
    }
}
