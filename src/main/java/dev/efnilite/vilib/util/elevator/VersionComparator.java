package dev.efnilite.vilib.util.elevator;

/**
 * Class containing comparators for versions which may be used in plugins.
 */
public enum VersionComparator {

    /**
     * Tests whether version strings are equal.
     */
    EQUAL {

        @Override
        public boolean isLatest(String latest, String current) {
            return latest.equalsIgnoreCase(current);
        }
    },

    /**
     * Compares versions with a semantic syntax. Does not support letters, just numbers.
     */
    FROM_SEMANTIC {

        @Override
        boolean isLatest(String latest, String current) {
            String[] latestVs = strip(latest).split("\\.");
            String[] currentVs = strip(current).split("\\.");

            for (int i = 0; i < latestVs.length; i++) {
                int l = Integer.parseInt(latestVs[i]);
                int c = Integer.parseInt(currentVs[i]);

                if (l != c) {
                    return false;
                }
            }

            return true;
        }
    };

    abstract boolean isLatest(String latest, String current);

    // strips a string
    protected String strip(String string) {
        return string.toLowerCase().replace("v", "");
    }
}