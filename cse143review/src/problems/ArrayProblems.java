package problems;

/**
 * See the spec on the website for example behavior.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - Do not add any additional imports
 * - Do not create new `int[]` objects for `toString` or `rotateRight`
 */
public class ArrayProblems {

    /**
     * Returns a `String` representation of the input array.
     * Always starts with '[' and ends with ']'; elements are separated by ',' and a space.
     */
    public static String toString(int[] array) {
        // throw new UnsupportedOperationException("Not implemented yet.");
        if (array.length == 0) {
            return "[]";
        }
        String res = "[";
        for (int num : array) {
            res += num + ", ";
        }
        // get rid of the last ", "
        res = res.substring(0, res.length() - 2);
        res += "]";
        return res;
    }

    /**
     * Returns a new array containing the input array's elements in reversed order.
     * Does not modify the input array.
     */
    public static int[] reverse(int[] array) {
        // throw new UnsupportedOperationException("Not implemented yet.");
        int n = array.length;
        int[] res = new int[n];

        for (int i = 0; i < n; i++) {
            res[i] = array[n - i - 1];
        }
        return res;
    }

    /**
     * Rotates the values in the array to the right.
     */
    public static void rotateRight(int[] array) {
        // throw new UnsupportedOperationException("Not implemented yet.");
        int n = array.length;
        if (n == 0) {
            return;
        }
        int temp = array[n - 1];
        for (int i = n - 1; i > 0; i--) {
            array[i] = array[i - 1];
        }
        array[0] = temp;
    }
}
