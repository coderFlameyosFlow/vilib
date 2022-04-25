package dev.efnilite.vilib.vector;

import org.bukkit.util.Vector;

import java.io.Serializable;

/**
 * A 2D vector.
 */
public class Vector2D implements Serializable {

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

    /**
     * Adds another vector to this one by adding all coordinates one by one.
     *
     * @param   other
     *          The other vector.
     *
     * @return the instance of this class with updated coordinates
     */
    public Vector2D add(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    /**
     * Multiplies all coordinates by the same modifier.
     *
     * @param   modifier
     *          The modifier which all coordinates will be multiplied by.
     *
     * @return the instance of this class with updated coordinates
     */
    public Vector2D multiply(double modifier) {
        this.x *= modifier;
        this.y *= modifier;
        return this;
    }

    /**
     * Subtracts another vector from this one by subtracting all coordinates one by one.
     *
     * @param   other
     *          The other vector.
     *
     * @return the instance of this class with updated coordinates
     */
    public Vector2D subtract(Vector2D other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    /**
     * Gets the distance to another vector.
     *
     * @param   other
     *          The other vector.
     *
     * @return the distance between the two vectors. Always positive.
     */
    public double distanceTo(Vector2D other) {
        double x2 = Math.pow(other.x - x, 2);
        double y2 = Math.pow(other.y - y, 2);
        return Math.sqrt(Math.abs(x2 + y2));
    }

    public Vector2D setX(int x) {
        this.x = x;
        return this;
    }

    public Vector2D setY(int y) {
        this.y = y;
        return this;
    }

    /**
     * Rotates this vector a specific amount of degrees.
     * Clockwise is negative and counterclockwise is positive.
     *
     * @param   deg
     *          The angle in degrees.
     */
    public void rotate(int deg) {
        double rad =  Math.toRadians(deg * -1);
        double rotatedX = (Math.cos(rad) * x) - (Math.sin(rad) * y); // uses rotation matrix
        double rotatedY = (Math.sin(rad) * x) + (Math.cos(rad) * y);

        this.x = (int) Math.round(rotatedX);
        this.y = (int) Math.round(rotatedY);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}