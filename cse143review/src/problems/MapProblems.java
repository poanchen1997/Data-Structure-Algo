package problems;

import java.util.HashMap;
// import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * See the spec on the website for example behavior.
 */
public class MapProblems {

    /**
     * Returns true if any string appears at least 3 times in the given list; false otherwise.
     */
    public static boolean contains3(List<String> list) {
        // throw new UnsupportedOperationException("Not implemented yet.");
        HashMap<String, Integer> m = new HashMap<String, Integer>();

        for (String s : list) {
            int temp = m.containsKey(s) ? m.get(s) : 0;
            if (temp == 2) { // check here so we don't need to iterate all the map to check for 3 times.
                return true;
            }
            m.put(s, temp + 1);
        }
        return false;
    }

    /**
     * Returns a map containing the intersection of the two input maps.
     * A key-value pair exists in the output iff the same key-value pair exists in both input maps.
     */
    public static Map<String, Integer> intersect(Map<String, Integer> m1, Map<String, Integer> m2) {
        // throw new UnsupportedOperationException("Not implemented yet.");
        HashMap<String, Integer> res = new HashMap<String, Integer>();

        for (String name : m1.keySet()) {
            if (m2.containsKey(name) && m1.get(name) == m2.get(name)) {
                res.put(name, m1.get(name));
            }
        }
        return res;
    }
}
