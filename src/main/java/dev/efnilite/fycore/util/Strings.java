package dev.efnilite.fycore.util;

import java.util.List;

public class Strings {

    /**
     * Gets the closest matching string
     *
     * @param   source
     *          The source string
     *
     * @param   strings
     *          Strings which will be compared to this string
     *
     * @return the closest matching string from parameter strings
     */
    public static String getClosestMatching(String source, List<String> strings) {
        int min = Integer.MAX_VALUE;
        String closest = "";

        for (String string : strings) {
            int distance = getLevenshteinDistance(source, string);
            if (distance < min) {
                closest = string;
            }
        }
        return closest;
    }

    /**
     * Gets the levenshtein distance between two strings
     * Source: https://www.stephenenright.com/java-levenshtein-distance
     *
     * @param   source
     *          The source string
     *
     * @param   other
     *          The other string
     *
     * @return the distance required
     */
    public static int getLevenshteinDistance(String source, String other) {
        int sourceLength = source.length();
        int otherLength = other.length();

        int[][] minDistanceMatrix = new int[sourceLength + 1][otherLength + 1];  // init the minimum distance matrix and add one to account for default values
        minDistanceMatrix[0][0] = 0;

        for (int row = 0; row < sourceLength; row++) { // enter default edit values for the source string (in rows)
            minDistanceMatrix[row][0] = row;
        }

        for (int col = 0; col < otherLength; col++) { // enter default edit values for the other string (in cols)
            minDistanceMatrix[0][col] = col;
        }

        for (int row = 0; row < sourceLength; row++) {
            for (int col = 0; col < otherLength; col++) { // go through every value and get the min value
                minDistanceMatrix[row][col] = getMinLevenshteinCost(source, other, minDistanceMatrix, row, col);
            }
        }

        return minDistanceMatrix[sourceLength][otherLength]; // get the last value
    }

    private static int getMinLevenshteinCost(String source, String other, int[][] minDistanceMatrix, int row, int col) {
        int insertion = minDistanceMatrix[row][col - 1] + 1;
        int deletion = minDistanceMatrix[row - 1][col] + 1;
        int substition = minDistanceMatrix[row - 1][col - 1];

        if (source.charAt(row - 1) != other.charAt(col - 1)) { // if the letters are the same skip adding a cost
            substition += 1;
        }

        return Numbers.min(insertion, deletion, substition);
    }
}