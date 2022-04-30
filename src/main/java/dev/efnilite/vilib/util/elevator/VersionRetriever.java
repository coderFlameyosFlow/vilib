package dev.efnilite.vilib.util.elevator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.efnilite.vilib.ViMain;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
    },

    GITHUB {

        /**
         * Gets the latest version using the GitHub REST API.
         *
         * @param   repo
         *          The repo name including the author, e.g. Efnilite/vilib
         *
         * @return the latest version
         */
        @Override
        public String getLatestVersion(String repo) {
            try {
                String url = "https://api.github.com/repos/" + repo + "/releases/latest";

                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestProperty("accept", "application/json");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();
                String tag = object.get("tag_name").getAsString();
//                String downloadUrl = object.get("browser_download_url").getAsString(); // todo add

                reader.close();
                return tag;
            } catch (Throwable throwable) {
                ViMain.logging().error("Error while trying to get the latest version.");
            }

            return null;
        }
    };

    public abstract String getLatestVersion(String url);

}
