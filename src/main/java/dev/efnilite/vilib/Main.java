package dev.efnilite.vilib;

import dev.efnilite.vilib.vector.Vector2D;

public class Main {

    public static void main(String[] args) {
        Vector2D vector2D = new Vector2D(1, 1);
        vector2D.rotate(90);
        System.out.println(vector2D);
    }

}
