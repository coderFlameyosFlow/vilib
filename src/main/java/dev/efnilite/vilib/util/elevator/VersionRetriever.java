package dev.efnilite.vilib.util.elevator;

import dev.efnilite.vilib.ViMain;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

public enum VersionRetriever {

    PLUGIN_YML {
        @Override
        public String getLatestVersion(String url) {
            try {
                InputStream stream = new URL(url).openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream)); // buffered to improve performance

                return reader.lines()
                        .filter(s -> s.startsWith("version:")) // only get the version
                        .collect(Collectors.toList())
                        .get(0)
                        .replace("version:", "")
                        .replaceAll("['\" ]", "")
                        .trim();
            } catch (Throwable throwable) {
                ViMain.logging().error("Error while trying to get latest version.");
                return "";
            }
        }
    };

    public abstract String getLatestVersion(String url);

}