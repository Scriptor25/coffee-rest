package io.scriptor.loader;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface ITree<T> extends Iterable<T> {

    Iterator<ITree<T>> children();

    T value();

    boolean hasValue();

    @Override
    default @NotNull Iterator<T> iterator() {
        return new TreeIterator<>(this);
    }

    final class TreeIterator<T> implements Iterator<T> {

        private final Deque<Iterator<ITree<T>>> stack = new ArrayDeque<>();
        private T next;

        public TreeIterator(final @NotNull ITree<T> root) {
            this.stack.push(Collections.singletonList(root).iterator());
            advance();
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            final var result = next;
            advance();
            return result;
        }

        private void advance() {
            next = null;

            while (!stack.isEmpty()) {
                final var top = stack.peek();

                if (top.hasNext()) {
                    final var current = top.next();
                    stack.push(current.children());

                    if (current.hasValue()) {
                        next = current.value();
                        return;
                    }
                } else {
                    stack.pop();
                }
            }
        }
    }
}
