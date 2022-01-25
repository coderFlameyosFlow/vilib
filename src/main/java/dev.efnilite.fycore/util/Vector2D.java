package dev.efnilite.fycore.util;

import org.bukkit.util.Vector;

/**
 * 2D vector.
 */
public class Vector2D {

    public int x;
    public int y;

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Converts this Vector2D instance to a default Bukkit one
     *
     * @return this vector but usable in Bukkit applications
     */
    public Vector toBukkitVector() {
        return new Vector(x, y, 0);
    }

    /**
     * Gets a Vector2D from a Bukkit vector
     *
     * @param   vector
     *          The Bukkit vector to be converted
     *
     * @return a new Vector2D
     */
    public static Vector2D fromBukkit(Vector vector) {
        return new Vector2D(vector.getBlockX(), vector.getBlockY());
    }

    @Override
    public Vector2D clone() {
        return new Vector2D(x, y);
    }

    public Vector2D add(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector2D multiply(double modifier) {
        this.x *= modifier;
        this.y *= modifier;
        return this;
    }

    public Vector2D subtract(Vector2D vector) {
        this.x -= vector.x;
        this.y -= vector.y;
        return this;
    }

    public double distanceTo(Vector2D other) {
        double x2 = Math.pow(other.x - x, 2);
        double y2 = Math.pow(other.y - y, 2);
        return Math.sqrt(x2 + y2);
    }

    public Vector2D setX(int x) {
        this.x = x;
        return this;
    }

    public Vector2D setY(int y) {
        this.y = y;
        return this;
    }

    public void rotate(int deg) {
        double rad =  Math.toRadians(deg);
        double rotatedX = (Math.cos(rad) * x) - (Math.sin(rad) * y); // uses rotation matrix
        double rotatedY = (Math.sin(rad) * x) + (Math.cos(rad) * y);

        this.x = (int) rotatedX;
        this.y = (int) rotatedY;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}