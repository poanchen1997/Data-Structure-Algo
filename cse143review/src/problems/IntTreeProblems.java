package problems;

import datastructures.IntTree;
// Checkstyle will complain that this is an unused import until you use it in your code.
import datastructures.IntTree.IntTreeNode;

/**
 * See the spec on the website for tips and example behavior.
 *
 * Also note: you may want to use private helper methods to help you solve these problems.
 * YOU MUST MAKE THE PRIVATE HELPER METHODS STATIC, or else your code will not compile.
 * This happens for reasons that aren't the focus of this assignment and are mostly skimmed over in
 * 142 and 143. If you want to know more, you can ask on the discussion board or at office hours.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not call any methods on the `IntTree` objects
 * - do not construct new `IntTreeNode` objects (though you may have as many `IntTreeNode` variables
 *      as you like).
 * - do not construct any external data structures such as arrays, queues, lists, etc.
 * - do not mutate the `data` field of any node; instead, change the tree only by modifying
 *      links between nodes.
 */

public class IntTreeProblems {

    /**
     * Computes and returns the sum of the values multiplied by their depths in the given tree.
     * (The root node is treated as having depth 1.)
     */
    public static int depthSum(IntTree tree) {
        // throw new UnsupportedOperationException("Not implemented yet.");

        IntTreeNode root = tree.overallRoot;
        if (root == null) {
            return 0;
        }
        return helper(root, 1);
    }

    public static int helper(IntTreeNode root, int depth) {
        if (root.left == null && root.right == null) {
            return root.data * depth;
        } else if (root.right == null) {
            // left exist
            return root.data * depth + helper(root.left, depth + 1);
        } else if (root.left == null) {
            // right exist
            return root.data * depth + helper(root.right, depth + 1);
        } else {
            // both exist
            return root.data * depth + helper(root.left, depth + 1) + helper(root.right, depth + 1);
        }
    }
    /**
     * Removes all leaf nodes from the given tree.
     */
    public static void removeLeaves(IntTree tree) {
        // throw new UnsupportedOperationException("Not implemented yet.");
        IntTreeNode root = tree.overallRoot;
        if (root == null) {
            return;
        } else if (root.left == null && root.right == null) {
            tree.overallRoot = null;
        } else {
            helper2(root);
        }
    }

    public static void helper2(IntTreeNode root) {
        if (root.left != null) {
            if (root.left.left == null && root.left.right == null) {
                root.left = null;
            } else {
                helper2(root.left);
            }
        }
        if (root.right != null) {
            if (root.right.left == null && root.right.right == null) {
                root.right = null;
            } else {
                helper2(root.right);
            }
        }

    }

    /**
     * Removes from the given BST all values less than `min` or greater than `max`.
     * (The resulting tree is still a BST.)
     */
    public static void trim(IntTree tree, int min, int max) {
        // throw new UnsupportedOperationException("Not implemented yet.");
        IntTreeNode root = tree.overallRoot;

        tree.overallRoot = helper3(root, min, max);

    }

    public static IntTreeNode helper3(IntTreeNode root, int min, int max) {
        if (root == null) {
            return null;
        }

        root.left = helper3(root.left, min, max);
        root.right = helper3(root.right, min, max);

        if (root.data < min || root.data > max) {
            if (root.left == null) {
                return root.right;
            }
            if (root.right == null) {
                return root.left;
            }
        }
        return root;


    }
}
