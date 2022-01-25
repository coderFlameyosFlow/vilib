package dev.efnilite.fycore.util.vector;

import org.bukkit.util.Vector;

/**
 * 3D vector.
 */
public class Vector3D {

    public int x;
    public int y;
    public int z;

    public Vector3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Converts this Vector3D instance to a default Bukkit one
     *
     * @return this vector but usable in Bukkit applications
     */
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
    public static Vector3D fromBukkit(Vector vector) {
        return new Vector3D(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    @Override
    public Vector3D clone() {
        return new Vector3D(x, y, z);
    }

    public Vector3D add(Vector3D other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }

    public Vector3D multiply(double modifier) {
        this.x *= modifier;
        this.y *= modifier;
        this.z *= modifier;
        return this;
    }

    public Vector3D subtract(Vector3D vector) {
        this.x -= vector.x;
        this.y -= vector.y;
        this.z -= vector.z;
        return this;
    }

    public double distanceTo(Vector3D other) {
        double x2 = Math.pow(other.x - x, 2);
        double y2 = Math.pow(other.y - y, 2);
        double z2 = Math.pow(other.z - z, 2);
        return Math.sqrt(x2 + y2 + z2);
    }

    public Vector3D setX(int x) {
        this.x = x;
        return this;
    }

    public Vector3D setY(int y) {
        this.y = y;
        return this;
    }

    public Vector3D setZ(int z) {
        this.z = z;
        return this;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }
}