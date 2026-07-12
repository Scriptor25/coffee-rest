package io.scriptor.loader

import java.util.*

interface ITree<T> : Iterable<T> {

    val children: MutableIterator<ITree<T>>

    val value: T?

    fun hasValue(): Boolean

    override fun iterator(): MutableIterator<T> {
        return TreeIterator(this)
    }

    class TreeIterator<T>(root: ITree<T>) : MutableIterator<T> {
        private val stack: Deque<MutableIterator<ITree<T>>> = ArrayDeque()
        private var next: T? = null

        init {
            this.stack.push(mutableListOf(root).iterator())
            advance()
        }

        override fun hasNext(): Boolean {
            return next != null
        }

        override fun next(): T {
            val result = next ?: throw NoSuchElementException()
            advance()
            return result
        }

        private fun advance() {
            next = null

            while (!stack.isEmpty()) {
                val top = stack.peek()

                if (top.hasNext()) {
                    val current = top.next()
                    stack.push(current.children)

                    if (current.hasValue()) {
                        next = current.value
                        return
                    }
                } else {
                    stack.pop()
                }
            }
        }

        override fun remove() {
            throw UnsupportedOperationException()
        }
    }
}
