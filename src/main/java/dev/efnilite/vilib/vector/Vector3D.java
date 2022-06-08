package dev.efnilite.vilib.vector;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * 3D vector.
 */
public class Vector3D implements Serializable {

    public double x;
    public double y;
    public double z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Converts this Vector3D instance to a default Bukkit one
     *
     * @return this vector but usable in Bukkit applications
     */
    @NotNull
    public Vector toBukkitVector() {
        return new Vector(x, y, z);
    }

    /**
     * Gets a Vector3D from a Bukkit vector
     *
     * @param   vector
     *          The Bukkit vector to be converted
     *
     * @return a new Vector3D
     */
    @NotNull
    public static Vector3D fromBukkit(@NotNull Vector vector) {
        return new Vector3D(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    /**
     * Adds another vector to this one by adding all coordinates one by one.
     *
     * @param   other
     *          The other vector.
     *
     * @return the instance of this class with updated coordinates
     */
    @NotNull
    public Vector3D add(Vector3D other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }

    /**
     * Adds values to the current coordinates.
     *
     * @param   x
     *          The x value.
     *
     * @param   y
     *          The y value.
     *
     * @param   z
     *          The z value.
     *
     * @return the instance of this class with updated coordinates
     */
    @NotNull
    public Vector3D add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
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
    @NotNull
    public Vector3D multiply(double modifier) {
        this.x *= modifier;
        this.y *= modifier;
        this.z *= modifier;
        return this;
    }

    /**
     * Multiplies all coordinates by a different modifier, unique to every coordinate.
     *
     * @param   xMod
     *          The modifier of the x-coordinate.
     *
     * @param   yMod
     *          The modifier of the y-coordinate.
     *
     * @param   zMod
     *          The modifier of the z-coordinate.
     *
     * @return the instance of this class with updated coordinates
     */
    @NotNull
    public Vector3D multiply(double xMod, double yMod, double zMod) {
        this.x *= xMod;
        this.y *= yMod;
        this.z *= zMod;
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
    @NotNull
    public Vector3D subtract(Vector3D other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
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
    public double distanceTo(Vector3D other) {
        double x2 = Math.pow(other.x - x, 2);
        double y2 = Math.pow(other.y - y, 2);
        double z2 = Math.pow(other.z - z, 2);
        return Math.sqrt(Math.abs(x2 + y2 + z2));
    }

    /**
     * Rotates this vector a specific amount of degrees around the x-axis.
     * Clockwise is negative and counterclockwise is positive.
     *
     * @param   deg
     *          The angle in degrees.
     *
     * @return the rotated vector
     */
    @NotNull
    public Vector3D rotateX(int deg) {
        double rad =  Math.toRadians(deg * -1); // * -1 is required to follow clockwise rotation = negative and other way around, not sure why
        double rotatedY = (Math.cos(rad) * y) - (Math.sin(rad) * z);
        double rotatedZ = (Math.sin(rad) * y) + (Math.cos(rad) * z);

        this.y = rotatedY;
        this.z = rotatedZ;
        return this;
    }

    /**
     * Rotates this vector a specific amount of degrees around the y-axis.
     * Clockwise is negative and counterclockwise is positive.
     *
     * @param   deg
     *          The angle in degrees.
     *
     * @return the rotated vector
     */
    @NotNull
    public Vector3D rotateY(int deg) {
        double rad =  Math.toRadians(deg * -1); // * -1 is required to follow clockwise rotation = negative and other way around, not sure why
        double rotatedX = (Math.cos(rad) * x) - (Math.sin(rad) * z);
        double rotatedZ = (Math.sin(rad) * x) + (Math.cos(rad) * z);

        this.x = rotatedX;
        this.z = rotatedZ;
        return this;
    }

    /**
     * Rotates this vector a specific amount of degrees around the z-axis.
     * Clockwise is negative and counterclockwise is positive.
     *
     * @param   deg
     *          The angle in degrees.
     *
     * @return the rotated vector
     */
    @NotNull
    public Vector3D rotateZ(int deg) {
        double rad =  Math.toRadians(deg * -1); // * -1 is required to follow clockwise rotation = negative and other way around, not sure why
        double rotatedX = (Math.cos(rad) * x) - (Math.sin(rad) * y);
        double rotatedY = (Math.sin(rad) * x) + (Math.cos(rad) * y);

        this.x = rotatedX;
        this.y = rotatedY;
        return this;
    }

    /**
     * Gets the length of this vector
     *
     * @return the length of this vector
     */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Normalizes this vector instance.
     * All values will be divided by the length.
     *
     * @return the instance of this vector, but normalized
     */
    public Vector3D normalize() {
        double length = length();
        this.x /= length;
        this.y /= length;
        this.z /= length;
        return this;
    }

    /**
     * Turns this Vector3D instance into a Bukkit Location.
     *
     * @param   world
     *          The world in which this location is
     *
     * @return the Location instance with the same coordinates
     */
    @NotNull
    public Location toLocation(@NotNull World world) {
        return new Location(world, x, y, z);
    }

    @NotNull
    public Vector3D setX(int x) {
        this.x = x;
        return this;
    }

    @NotNull
    public Vector3D setY(int y) {
        this.y = y;
        return this;
    }

    @NotNull
    public Vector3D setZ(int z) {
        this.z = z;
        return this;
    }

    @Override
    public Vector3D clone() {
        return new Vector3D(x, y, z);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }
}