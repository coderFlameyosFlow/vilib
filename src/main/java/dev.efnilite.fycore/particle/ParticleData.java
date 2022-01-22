package dev.efnilite.fycore.particle;

import org.bukkit.Particle;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper for data used in {@link Particles}
 *
 * @author Efnilite
 */
public class ParticleData<Data> {

    private int size;
    private double speed;
    private double offsetX;
    private double offsetY;
    private double offsetZ;
    private Particle type;
    private @Nullable Data data;

    public ParticleData(Particle type, @Nullable Data data, int size) {
        this(type, data, size, 0, 0, 0, 0);
    }

    public ParticleData(Particle type, @Nullable Data data, int size, double speed) {
        this(type, data, size, speed, 0, 0, 0);
    }

    public ParticleData(Particle type, @Nullable Data data, int size, double offsetX, double offsetY, double offsetZ) {
        this(type, data, size, 0, offsetX, offsetY, offsetZ);
    }

    /**
     * Main constructor for Particle Data.
     *
     * @param   type
     *          The type of particle
     *
     * @param   data
     *          The possible data associated with this particle (colour, etc.)
     *
     * @param   size
     *          The size of the particle
     *
     * @param   speed
     *          The speed of the particle
     *
     * @param   offsetX
     *          The max x offset in moving
     *
     * @param   offsetY
     *          The max y offset in moving
     *
     * @param   offsetZ
     *          The max z offset in moving
     */
    public ParticleData(Particle type, @Nullable Data data, int size, double speed, double offsetX, double offsetY, double offsetZ) {
        this.data = data;
        this.type = type;
        this.size = size;
        this.speed = speed;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public ParticleData<Data> size(int size) {
        this.size = size;
        return this;
    }

    public ParticleData<Data> speed(double speed) {
        this.speed = speed;
        return this;
    }

    public ParticleData<Data> offsetX(double offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public ParticleData<Data> offsetY(double offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public ParticleData<Data> offsetZ(double offsetZ) {
        this.offsetZ = offsetZ;
        return this;
    }

    public ParticleData<Data> data(@Nullable Data data) {
        this.data = data;
        return this;
    }

    public ParticleData<Data> type(Particle type) {
        this.type = type;
        return this;
    }

    public int getSize() {
        return size;
    }

    public double getSpeed() {
        return speed;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getOffsetZ() {
        return offsetZ;
    }

    @Nullable
    public Data getData() {
        return data;
    }

    public Particle getType() {
        return type;
    }
}