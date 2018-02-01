package com.biobelt.biobeltapi;

import java.util.UUID;

/**
 * @author Simon WOLKIEWIEZ
 * @since 20/06/2017
 * @version 1.0.0
 */

public abstract class Utils {

    public static boolean isValidUUID(String uuid) {
        return uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
    }

    public static String getJsonMessage(boolean etat, String message, UUID id) {
        return "{\n" +
                    "\t\"etat\": " + etat + ",\n" +
                    "\t\"message\": \"" + message + "\",\n" +
                    "\t\"id\": \"" + id + "\"\n" +
                "}";
    }

    public static String getJsonMessage(boolean etat, String message) {
        return "{\n" +
                "\t\"etat\": " + etat + ",\n" +
                "\t\"message\": \"" + message + "\"\n" +
                "}";
    }

}
