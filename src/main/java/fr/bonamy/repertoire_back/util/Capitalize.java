package fr.bonamy.repertoire_back.util;

public class Capitalize {

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
