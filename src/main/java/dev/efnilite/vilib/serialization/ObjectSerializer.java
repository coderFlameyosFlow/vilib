package dev.efnilite.vilib.serialization;

import dev.efnilite.vilib.ViMain;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Class to serialize objects with in Base 64.
 */
public class ObjectSerializer {

    public static <T> String serialize64(T item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream output = new BukkitObjectOutputStream(outputStream);

            output.writeObject(item);

            output.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Throwable throwable) {
            ViMain.logging().stack("There was an error while trying to convert an object to base 64!", throwable);
            return "";
        }
    }

    public static <T> @Nullable T deserialize64(String string) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(string));
            BukkitObjectInputStream input = new BukkitObjectInputStream(inputStream);

            input.close();
            return (T) input.readObject();
        } catch (Throwable throwable) {
            ViMain.logging().stack("Error while trying to convert an object from base 64!",
                    "delete the inventories folder and restart the server", throwable);
            return null;
        }
    }

}
