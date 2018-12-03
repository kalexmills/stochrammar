/**
 * Copyright (c) 2018, NiftySoft LLC.
 *
 * This file is part of Stochrammar.
 *
 * Stochrammar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Stochrammar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Stochrammar.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.kalexmills.stochrammar.runner;

import com.github.kalexmills.stochrammar.CFToken;
import com.github.kalexmills.stochrammar.StochasticGrammar;

import java.util.*;

/**
 * TreeRunner provides an algorithm for running a StochasticGrammar. GrammarTokens generated by the grammar are
 * stored in a tree. The act() methods are applied to a blankEntity object via either a depth-first or a breadth-first
 * traversal of the resulting tree.
 *
 * @param <T>
 */
public class TreeRunner<T> extends GrammarRunner<T> {
    // TODO: This could be much more performant if it didn't construct an actual tree at all.

    public enum TraversalType {
        DEPTH_FIRST,
        BREADTH_FIRST
    }

    private TreeNode root;
    private TraversalType traversalType = TraversalType.DEPTH_FIRST;

    public TreeRunner(StochasticGrammar<T> grammar) {
        super(grammar);
    }

    @Override
    public T run(Random rand) {
        generateTree(rand);
        return generateEntity();
    }

    public void setTraversalType(TraversalType traversalType) {
        this.traversalType = traversalType;
    }

    private void generateTree(Random rand) {
        Stack<TreeNode> frontier = new Stack<>();

        root = new TreeNode(grammar.generateRootToken());
        frontier.push(root);
        while(!frontier.isEmpty()) {
            TreeNode<T> node = frontier.pop();

            CFToken[] tokens = node.token.replace(rand);
            if (tokens != null) {

                node.addChildren(tokens);
                for (int i = 0; i < node.nChildren; ++i) {
                    frontier.push(node.children[i]);
                }
            }
        }
    }

    private T generateEntity() {
        TraversalSequence sequence;
        switch (traversalType) {
            default:
            case DEPTH_FIRST:
                sequence = new TraversalStack();
                break;
            case BREADTH_FIRST:
                sequence = new TraversalQueue();
                break;
        }
        T entity = grammar.blankEntity();
        sequence.add(root);
        while(!sequence.isEmpty()) {
            TreeNode<T> curr = sequence.next();

            for (int i = 0; i < curr.nChildren; ++i) {
                sequence.add(curr.children[i]);
            }
            entity = curr.token.act(entity);
        }
        return entity;
    }

    /**
     * TraversalList provides an interface for sequential data structures used for tree traversal.
     */
    private interface TraversalSequence {
        void add(TreeNode node);
        TreeNode next();
        boolean isEmpty();
    }

    private static class TraversalStack implements TraversalSequence {

        private Stack<TreeNode> stack;
        public TraversalStack() { this.stack = new Stack<>(); }

        @Override
        public void add(TreeNode node) { stack.push(node); }

        @Override
        public TreeNode next() { return stack.pop(); }

        @Override
        public boolean isEmpty() {
            return stack.isEmpty();
        }
    }

    private static class TraversalQueue implements TraversalSequence {
        private Queue<TreeNode> queue;
        public TraversalQueue() { this.queue = new LinkedList<>(); }

        @Override
        public void add(TreeNode node) { queue.add(node); }

        @Override
        public TreeNode next() { return queue.remove(); }

        @Override
        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    /**
     * TreeNode wraps CFToken, providing an n-ary tree structure on top of it.
     * @param <T>
     */
    private static class TreeNode<T> {
        // The default number of slots to allocate a new TreeNode.
        private static final int DEFAULT_ARITY = 4;
        private TreeNode[] children;
        private int nChildren = 0;
        CFToken<T> token;

        public TreeNode(CFToken<T> token) {
            this(token, DEFAULT_ARITY);
        }

        public TreeNode(CFToken<T> token, int arity) {
            this.token = token;
            this.children = new TreeNode[4];
        }

        public void addChildren(CFToken<T>... tokens) {
            // Double size of children array if needed.
            if (nChildren + tokens.length < children.length) {
                int newLen = Math.max(children.length*2, nChildren + tokens.length);
                children = Arrays.copyOf(children, newLen);
            }
            // Insert TreeNode into children array.
            for (int i = 0; i < tokens.length; ++i) {
                children[nChildren + i] = new TreeNode(tokens[i]);
            }
            // Update dynamic array length.
            nChildren += tokens.length;
        }
    }
}