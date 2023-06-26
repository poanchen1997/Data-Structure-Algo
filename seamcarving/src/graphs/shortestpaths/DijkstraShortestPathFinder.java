package graphs.shortestpaths;

import priorityqueues.ExtrinsicMinPQ;
import priorityqueues.NaiveMinPQ;
import graphs.BaseEdge;
import graphs.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.List;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {
    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new NaiveMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Map<V, E> edgeTo = new HashMap<>(); // store the ancestor (child, edge to parent)
        Map<V, Double> distTo = new HashMap<>(); // store the distance
        Set<V> known = new HashSet<>(); // store the processed node
        // Using priority queue to make sure we get the smallest dist
        ExtrinsicMinPQ<V> pq = createMinPQ(); // using its weight for priority

        // make the start node dist to 0
        distTo.put(start, 0.0);
        pq.add(start, 0.0);

        // start the greedy algorithm
        while (!pq.isEmpty()) {
            V currNode = pq.removeMin();
            known.add(currNode);

            // add a break point here if we finish the end node
            if (Objects.equals(currNode, end)) {
                return edgeTo;
            }

            for (E edge : graph.outgoingEdgesFrom(currNode)) {
                // ignore the processed node
                if (!known.contains(edge.to())) {
                    Double oldDist = distTo.getOrDefault(edge.to(), Double.POSITIVE_INFINITY);
                    Double newDist = distTo.get(currNode) + edge.weight();
                    if (newDist < oldDist) {
                        distTo.put(edge.to(), newDist); // update the weight
                        edgeTo.put(edge.to(), edge); // update the parents
                        // pq.add(edge.to(), newDist); // revise this to prevent add repeatly
                        if (pq.contains(edge.to())) {
                            pq.changePriority(edge.to(), newDist);
                        } else {
                            pq.add(edge.to(), newDist);
                        }
                    }
                }
            }
        }
        return edgeTo;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        // case 1: start == end, the result would be just itself
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        List<E> edges = new ArrayList<>();
        E currEdge = spt.get(end);
        edges.add(currEdge);

        if (currEdge == null) {
            // case 2: cannot go back to start -> failure
            return new ShortestPath.Failure<>();
        }

        // going from end to start
        while (currEdge != null && currEdge.from() != start) {
            currEdge = spt.get(currEdge.from());
            if (currEdge != null) {
                edges.add(currEdge);
            }
        }

        // case 3: correctly find the start
        Collections.reverse(edges); // reverse from to make the order from start to end
        return new ShortestPath.Success<>(edges);
    }

}
