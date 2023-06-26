package seamcarving;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;


/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 * @see SeamCarver
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {
    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        // because the program seems to transpose the picture, so I swap the program for horizon and vertical !!!

        List<Integer> res = new ArrayList<>();
        int m = energies.length;
        int n = energies[0].length;
        // hashmap for storing parent
        HashMap<String, String> map = new HashMap<>(); // child : parent
        // hashmap for storing previous index (for y)
        HashMap<String, Integer> map2 = new HashMap<>();

        double[][] accumulation = new double[m][n];

        // fill up the first row
        for (int j = 0; j < n; j++) {
            accumulation[0][j] = energies[0][j];
        }

        // iterate the other part
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < n; j++) {
                double upleft = j == 0 ? Double.MAX_VALUE : accumulation[i - 1][j - 1];
                double upright = j == n - 1 ? Double.MAX_VALUE : accumulation[i - 1][j + 1];
                double up = accumulation[i - 1][j];
                int[] current = new int[]{i, j};

                if (upleft <= upright && upleft <= up) {
                    // upleft is the smallest
                    accumulation[i][j] = upleft + energies[i][j];
                    int[] previous = new int[]{i - 1, j - 1};
                    map.put(Arrays.toString(current), Arrays.toString(previous));
                    map2.put(Arrays.toString(previous), j - 1);
                } else if (up <= upleft && up <= upright) {
                    // up is the smallest
                    accumulation[i][j] = up + energies[i][j];
                    int[] previous = new int[]{i - 1, j};
                    map.put(Arrays.toString(current), Arrays.toString(previous));
                    map2.put(Arrays.toString(previous), j);
                } else {
                    // leftdown is the smallest
                    accumulation[i][j] = upright + energies[i][j];
                    int[] previous = new int[]{i - 1, j + 1};
                    map.put(Arrays.toString(current), Arrays.toString(previous));
                    map2.put(Arrays.toString(previous), j + 1);
                }
            }
        }

        // now go through all the number in the last row, find the smallest one
        int idx = 0;
        double smallest = accumulation[m - 1][0];

        for (int j = 1; j < n; j++) {
            if (accumulation[m - 1][j] < smallest) {
                smallest = accumulation[m - 1][j];
                idx = j;
            }
        }
        // insert the index of idx
        res.add(idx);
        // current column is ...
        String currCell = Arrays.toString(new int[]{m - 1, idx});
        // go back "n - 1" time for trace back
        while (map.get(currCell) != null) {
            res.add(map2.get(map.get(currCell)));
            currCell = map.get(currCell);
        }

        // reverse to get the start to end
        Collections.reverse(res);
        return res;
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        List<Integer> res = new ArrayList<>();
        int m = energies.length;
        int n = energies[0].length;
        // hashmap for storing parent
        HashMap<String, String> map = new HashMap<>(); // child : parent
        HashMap<String, Integer> map2 = new HashMap<>();

        double[][] accumulation = new double[m][n];

        // fill up the first column
        for (int i = 0; i < m; i++) {
            accumulation[i][0] = energies[i][0];
        }

        // iterate the other part
        for (int j = 1; j < n; j++) {
            for (int i = 0; i < m; i++) {
                double leftup = i == 0 ? Double.MAX_VALUE : accumulation[i - 1][j - 1];
                double leftdown = i == m - 1 ? Double.MAX_VALUE : accumulation[i + 1][j - 1];
                double left = accumulation[i][j - 1];
                int[] current = new int[]{i, j};

                if (leftup <= leftdown && leftup <= left) {
                    // leftup is the smallest
                    accumulation[i][j] = leftup + energies[i][j];
                    int[] previous = new int[]{i - 1, j - 1};
                    map.put(Arrays.toString(current), Arrays.toString(previous));
                    map2.put(Arrays.toString(previous), i - 1);
                } else if (left <= leftup && left <= leftdown) {
                    // left is the smallest
                    accumulation[i][j] = left + energies[i][j];
                    int[] previous = new int[]{i, j - 1};
                    map.put(Arrays.toString(current), Arrays.toString(previous));
                    map2.put(Arrays.toString(previous), i);
                } else {
                    // leftdown is the smallest
                    accumulation[i][j] = leftdown + energies[i][j];
                    int[] previous = new int[]{i + 1, j - 1};
                    map.put(Arrays.toString(current), Arrays.toString(previous));
                    map2.put(Arrays.toString(previous), i + 1);
                }
            }
        }

        // now go through all the number in the last column, find the smallest one
        int idx = 0;
        double smallest = accumulation[0][n - 1];

        for (int i = 1; i < m; i++) {
            if (accumulation[i][n - 1] < smallest) {
                smallest = accumulation[i][n - 1];
                idx = i;
            }
        }
        // insert the index of idx
        res.add(idx);
        // current column is ...
        String currCell = Arrays.toString(new int[]{idx, n - 1});
        // go back "n - 1" time for trace back
        while (map.get(currCell) != null) {
            res.add(map2.get(map.get(currCell)));
            currCell = map.get(currCell);
        }

        // reverse to get the start to end
        Collections.reverse(res);
        return res;
    }
}

