package seamcarving;

import graphs.BaseEdge;
import graphs.Edge;
import graphs.Graph;
import graphs.shortestpaths.DijkstraShortestPathFinder;
import graphs.shortestpaths.ShortestPath;
import graphs.shortestpaths.ShortestPathFinder;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
// import java.util.Objects;
import java.util.Set;

public class DijkstraSeamFinder implements SeamFinder {
    // nest class for graph
    private static class MyGraph<V, E extends BaseEdge<V, E>> implements graphs.Graph<V, E> {
        // Note: use "static" in the class header if it doesn't use any non-static fields
        // in the outer class. Otherwise, remove the static keyword.

        // fields
        private final List<E> allEdges;
        private final Map<V, Set<E>> adjacencyList;
        public MyGraph(Collection<E> edges) {
            // initialize fields
            // Note: you don't need to create an actual adjacency list
            this.allEdges = new ArrayList<>();
            this.adjacencyList = new HashMap<>();
            edges.forEach(e -> {
                if (adjacencyList.computeIfAbsent(e.from(), v -> new HashSet<>()).add(e)) {
                    allEdges.add(e);
                }
                // adjacencyList.computeIfAbsent(e.to(), v -> new HashSet<>()).add(e.reversed());
            });
        }

        @Override
        public Collection<E> outgoingEdgesFrom(V vertex) {
            return Collections.unmodifiableSet(adjacencyList.getOrDefault(vertex, Set.of()));
        }

        // helper methods here
    }
    private final ShortestPathFinder<Graph<int[], Edge<int[]>>, int[], Edge<int[]>> pathFinder;

    // special function for edge
    protected <V> Edge<V> edge(V from, V to, double weight) {
        return new Edge<>(from, to, weight);
    }

    protected final <V, E extends BaseEdge<V, E>> MyGraph<V, E> directedGraph(List<Edge<Object>> edges) {
        return new MyGraph<>((Collection<E>) edges);
    }

    public DijkstraSeamFinder() {
        this.pathFinder = createPathFinder();
    }

    protected <G extends Graph<V, Edge<V>>, V> ShortestPathFinder<G, V, Edge<V>> createPathFinder() {
        /*
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
        */
        return new DijkstraShortestPathFinder<>();
    }

    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        List<Integer> res = new ArrayList<>();
        int m = energies.length;
        int n = energies[0].length;
        // use a hashmap to store the primitive and node connection
        HashMap<String, int[]> map = new HashMap<>();
        // insert all the cell into the map
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int[] temp = new int[]{i, j};
                map.put(Arrays.toString(temp), temp);
            }
        }
        // not forget to add the dummy node for start and end
        int[] start = new int[]{-1, -1};
        map.put(Arrays.toString(start), start);
        int[] end = new int[]{m, n};
        map.put(Arrays.toString(end), end);

        // make a collection for inserting the edge
        List<Edge<Object>> edges = new ArrayList<>();
        // insert start dummy node to first column
        for (int j = 0; j < n; j++) {
            edges.add(
                edge(start,
                    map.get(Arrays.toString(new int[]{0, j})),
                    energies[0][j]
                )
            );
        }
        // insert each node to its down
        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < n; j++) {
                // add its down
                edges.add(
                    edge(
                        map.get(Arrays.toString(new int[]{i, j})),
                        map.get(Arrays.toString(new int[]{i + 1, j})),
                        energies[i + 1][j]
                    )
                );
                // if it's not j = 0, add its down-left
                if (j != 0) {
                    edges.add(
                        edge(
                            map.get(Arrays.toString(new int[]{i, j})),
                            map.get(Arrays.toString(new int[]{i + 1, j - 1})),
                            energies[i + 1][j - 1]
                        )
                    );
                }
                // if it's not j = n - 1, add its down-right
                if (j != n - 1) {
                    edges.add(
                        edge(
                            map.get(Arrays.toString(new int[]{i, j})),
                            map.get(Arrays.toString(new int[]{i + 1, j + 1})),
                            energies[i + 1][j + 1]
                        )
                    );
                }
            }
        }
        // insert edge from the last column to end dummy node
        for (int j = 0; j < n; j++) {
            edges.add(
                edge(
                    map.get(Arrays.toString(new int[]{m - 1, j})),
                    end,
                    0.0
                )
            );
        }
        // make a graph
        Graph<int[], Edge<int[]>> g = directedGraph(edges);
        // using the previous function to find the shortest path
        ShortestPath<int[], Edge<int[]>> p = this.pathFinder.findShortestPath(g, start, end);
        // get answer and put it into the list
        List<Edge<int[]>> e = p.edges();

        for (int i = 0; i < m; i++) {
            res.add(e.get(i).to()[1]);
        }

        return res;
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        List<Integer> res = new ArrayList<>();
        int m = energies.length;
        int n = energies[0].length;
        // use a hashmap to store the primitive and node connection
        HashMap<String, int[]> map = new HashMap<>();
        // insert all the cell into the map
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int[] temp = new int[]{i, j};
                map.put(Arrays.toString(temp), temp);
            }
        }
        // not forget to add the dummy node for start and end
        int[] start = new int[]{-1, -1};
        map.put(Arrays.toString(start), start);
        int[] end = new int[]{m, n};
        map.put(Arrays.toString(end), end);

        // make a collection for inserting the edge
        List<Edge<Object>> edges = new ArrayList<>();
        // insert start dummy node to first column
        for (int i = 0; i < m; i++) {
            edges.add(
                edge(start,
                    map.get(Arrays.toString(new int[]{i, 0})),
                    energies[i][0]
                )
            );
        }
        // insert each node to its right
        for (int j = 0; j < n - 1; j++) {
            for (int i = 0; i < m; i++) {
                // add its right
                edges.add(
                    edge(
                        map.get(Arrays.toString(new int[]{i, j})),
                        map.get(Arrays.toString(new int[]{i, j + 1})),
                        energies[i][j + 1]
                    )
                );
                // if it's not i = 0, add its up-right
                if (i != 0) {
                    edges.add(
                        edge(
                            map.get(Arrays.toString(new int[]{i, j})),
                            map.get(Arrays.toString(new int[]{i - 1, j + 1})),
                            energies[i - 1][j + 1]
                        )
                    );
                }
                // if it's not i = m - 1, add its down-right
                if (i != m - 1) {
                    edges.add(
                        edge(
                            map.get(Arrays.toString(new int[]{i, j})),
                            map.get(Arrays.toString(new int[]{i + 1, j + 1})),
                            energies[i + 1][j + 1]
                        )
                    );
                }
            }
        }
        // insert edge from the last column to end dummy node
        for (int i = 0; i < m; i++) {
            edges.add(
                edge(
                    map.get(Arrays.toString(new int[]{i, n - 1})),
                    end,
                    0.0
                )
            );
        }
        // make a graph
        Graph<int[], Edge<int[]>> g = directedGraph(edges);
        // using the previous function to find the shortest path
        ShortestPath<int[], Edge<int[]>> p = this.pathFinder.findShortestPath(g, start, end);
        // get answer and put it into the list
        List<Edge<int[]>> e = p.edges();
        System.out.println(n);
        for (int j = 0; j < n; j++) {
            res.add(e.get(j).to()[0]);
        }

        return res;
    }


}
