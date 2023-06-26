package priorityqueues;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;
    List<PriorityNode<T>> items;
    // add as I want
    HashMap<T, Double> map;
    HashMap<T, Integer> mapForIdx;
    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        // Add as I want
        map = new HashMap<>();
        mapForIdx = new HashMap<>();
        items.add(new PriorityNode<>(null, Double.NEGATIVE_INFINITY)); // add a node for the first one
        map.put(null, Double.NEGATIVE_INFINITY);
        mapForIdx.put(null, 0);
    }

    // add some function for easy get access to child or parent
    public int getLeftChild(int idx) {
        // special formula for starting at 1
        return 2 * idx;
    }

    public int getRightChild(int idx) {
        // again, special formula
        return 2 * idx + 1;
    }

    public int getParent(int idx) {
        // special formula
        return idx / 2;
    }
    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        // revise index
        int tempIdx = mapForIdx.get(items.get(a).getItem());
        mapForIdx.put(items.get(a).getItem(), mapForIdx.get(items.get(b).getItem()));
        mapForIdx.put(items.get(b).getItem(), tempIdx);
        // revise map
        PriorityNode<T> temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        // instead of adding a priority in PriorityNode, I just insert 1 for all the node.
        // when I want to compare the priority, I'll just look up at the hashmap
        PriorityNode<T> curr = new PriorityNode<>(item, 1);
        items.add(curr);
        map.put(item, priority);
        mapForIdx.put(item, items.size() - 1);
        // now check if we need to swap
        int currIdx = items.size() - 1;
        int currParentIdx = getParent(currIdx);
        while (map.get(items.get(currIdx).getItem()) < map.get(items.get(currParentIdx).getItem())) {
            swap(currIdx, currParentIdx);
            currIdx = currParentIdx;
            currParentIdx = getParent(currIdx);
        }
    }

    @Override
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (items.size() == 1) {
            throw new NoSuchElementException();
        }
        return items.get(1).getItem();
    }

    @Override
    public T removeMin() {
        if (items.size() == 1) {
            throw new NoSuchElementException();
        } else if (items.size() == 2) {
            T res = items.get(1).getItem();
            items.remove(items.size() - 1);
            map.remove(res); // remove specific T from the hashmap
            mapForIdx.remove(res);
            return res;
        }
        // first swap the first and the last item, and them remove the last item
        swap(1, items.size() - 1);
        T res = items.get(items.size() - 1).getItem();
        items.remove(items.size() - 1);
        map.remove(res); // same as above
        mapForIdx.remove(res);
        // check if we need to percolateDown
        int currIdx = 1;
        int leftChild = getLeftChild(currIdx);
        int rightChild = getRightChild(currIdx);
        while ((leftChild < items.size() &&
                    map.get(items.get(currIdx).getItem()) > map.get(items.get(leftChild).getItem())) ||
              (rightChild < items.size() &&
                    map.get(items.get(currIdx).getItem()) > map.get(items.get(rightChild).getItem()))) {
            // corner case for the right child is out of bound
            // only check right child because the heap needs to be complete,
            // so there won't have the case that have no left child but have right child.
            if (rightChild >= items.size()) {
                swap(currIdx, leftChild);
                currIdx = leftChild;
                leftChild = getLeftChild(currIdx);
                rightChild = getRightChild(currIdx);
                continue;
            }
            // normal case:
            // find the min of the child
            if (map.get(items.get(leftChild).getItem()) < map.get(items.get(rightChild).getItem())) {
                swap(currIdx, leftChild);
                currIdx = leftChild;
            } else {
                swap(currIdx, rightChild);
                currIdx = rightChild;
            }
            leftChild = getLeftChild(currIdx);
            rightChild = getRightChild(currIdx);
        }
        return res;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }

        // first change the priority in the hashmap
        map.put(item, priority);
        // here find the place where we change the priority
        // // here we have O(n)
        // int stopPoint = 1;
        // for (int i = 1; i < items.size(); i++) {
        //     if (items.get(i).getItem().equals(item)) {
        //         stopPoint = i;
        //         break;
        //     }
        // }
        // right now, I hold another hashmap for index in the items


        // after changing the priority, check if we need to swap with up and down
        // check if we need to swap with parent
        // int currIdx = stopPoint;
        int currIdx = mapForIdx.get(item);
        int currParentIdx = getParent(currIdx);
        while (map.get(items.get(currIdx).getItem()) < map.get(items.get(currParentIdx).getItem())) {
            swap(currIdx, currParentIdx);
            currIdx = currParentIdx;
            currParentIdx = getParent(currIdx);
        }
        // now check if we want to go down
        int leftChild = getLeftChild(currIdx);
        int rightChild = getRightChild(currIdx);
        while ((leftChild < items.size() &&
                    map.get(items.get(currIdx).getItem()) > map.get(items.get(leftChild).getItem())) ||
               (rightChild < items.size() &&
                    map.get(items.get(currIdx).getItem()) > map.get(items.get(rightChild).getItem()))) {
            // corner case for the right child is out of bound
            // only check right child because the heap needs to be complete,
            // so there won't have the case that have no left child but have right child.
            if (rightChild >= items.size()) {
                swap(currIdx, leftChild);
                currIdx = leftChild;
                leftChild = getLeftChild(currIdx);
                rightChild = getRightChild(currIdx);
                continue;
            }
            // find the min of the child
            if (map.get(items.get(leftChild).getItem()) < map.get(items.get(rightChild).getItem())) {
                swap(currIdx, leftChild);
                currIdx = leftChild;
            } else {
                swap(currIdx, rightChild);
                currIdx = rightChild;
            }
            leftChild = getLeftChild(currIdx);
            rightChild = getRightChild(currIdx);
        }
    }

    @Override
    public int size() {
        return items.size() - 1;
    }
}
