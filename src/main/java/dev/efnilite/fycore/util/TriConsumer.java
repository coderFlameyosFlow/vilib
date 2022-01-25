package dev.efnilite.fycore.util;

public interface TriConsumer<X, Y, Z> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param   x
     *          The first input argument
     * @param   y
     *          The second input argument
     * @param   z
     *          The third input argument
     */
    void accept(X x, Y y, Z z);

}