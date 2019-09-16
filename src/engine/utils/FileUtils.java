package engine.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
    public static String loadAsString(String path) {
        StringBuilder result = new StringBuilder();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(Class.class.getResourceAsStream(path));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Couldn't find the file at " + path);
        }

        return result.toString();
    }
}
