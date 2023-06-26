package problems;

import datastructures.LinkedIntList;
// Checkstyle will complain that this is an unused import until you use it in your code.
import datastructures.LinkedIntList.ListNode;

// import java.util.List;

/**
 * See the spec on the website for example behavior.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not call any methods on the `LinkedIntList` objects.
 * - do not construct new `ListNode` objects for `reverse3` or `firstToLast`
 *      (though you may have as many `ListNode` variables as you like).
 * - do not construct any external data structures such as arrays, queues, lists, etc.
 * - do not mutate the `data` field of any node; instead, change the list only by modifying
 *      links between nodes.
 */

public class LinkedIntListProblems {

    /**
     * Reverses the 3 elements in the `LinkedIntList` (assume there are exactly 3 elements).
     */
    public static void reverse3(LinkedIntList list) {
        // throw new UnsupportedOperationException("Not implemented yet.");
        ListNode prev = null;
        ListNode head = list.front;
        while (head != null) {
            ListNode nxt = head.next;
            head.next = prev;
            prev = head;
            head = nxt;
        }
        list.front = prev;
    }

    /**
     * Moves the first element of the input list to the back of the list.
     */
    public static void firstToLast(LinkedIntList list) {
        // throw new UnsupportedOperationException("Not implemented yet.");
        if (list.front == null || list.front.next == null) {
            return;
        }
        ListNode head = list.front;
        ListNode second = list.front.next;
        ListNode checkpoint = list.front.next;
        head.next = null;

        while (second.next != null) {
            second = second.next;
        }
        second.next = head;
        list.front = checkpoint;
    }

    /**
     * Returns a list consisting of the integers of a followed by the integers
     * of n. Does not modify items of A or B.
     */
    public static LinkedIntList concatenate(LinkedIntList a, LinkedIntList b) {
        // Hint: you'll need to use the 'new' keyword to construct new objects.
        // throw new UnsupportedOperationException("Not implemented yet.");
        ListNode aa = a.front;
        ListNode bb = b.front;
        LinkedIntList res = new LinkedIntList();
        if (aa == null) {
            res.front = bb;
        } else {
            // using linklist a to create first node
            ListNode rr = new ListNode(aa.data);
            // record the pointer of first
            ListNode head = rr;
            aa = aa.next;

            while (aa != null) {
                rr.next = new ListNode(aa.data);
                rr = rr.next;
                aa = aa.next;
            }
            while (bb != null) {
                rr.next = new ListNode(bb.data);
                rr = rr.next;
                bb = bb.next;
            }
            // assign res.front = pointer.
            res.front = head;
        }
        return res;
    }
}
