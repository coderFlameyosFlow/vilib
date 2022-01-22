package dev.efnilite.fycore.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class Serializer{

    protected static Gson gson = new GsonBuilder().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();

//    public String serialize64(Type object) throws IOException {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        BukkitObjectOutputStream output = new BukkitObjectOutputStream(outputStream);
//
//        output.writeObject(object);
//
//        output.close();
//        return Base64Coder.encodeLines(outputStream.toByteArray());
//    }
//
//    public Type deserialize64(String string) throws IOException, ClassNotFoundException {
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(string));
//        BukkitObjectInputStream input = new BukkitObjectInputStream(inputStream);
//
//        input.close();
//        return (Type) input.readObject();
//    }
}