package dev.efnilite.vilib.reflection;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unchecked")
public class Reflection {

    /**
     * Creates a new instance of a class using a constructor without arguments
     *
     * @param   cls
     *          The class
     *
     * @param   <T>
     *          The return value
     *
     * @return a new instance of the class
     */
    public static <T> T newInstance(Class<?> cls) {
        try {
            return (T) cls.getDeclaredConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <T> T newInstance(Class<?> cls, Object... args) {
        try {
            Class<?>[] classes = new Class<?>[args.length];
            int index = 0;
            for (Object arg : args) {
                classes[index] = arg.getClass();
                index++;
            }

            return (T) cls.getDeclaredConstructor(classes).newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
