package dev.efnilite.fycore.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class Serializer {

    protected static Gson gson = new GsonBuilder().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();

}