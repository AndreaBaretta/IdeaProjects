package com.company;

import org.omg.CORBA.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        final File csv = new File("/home/andrea/Desktop/frequency_response.csv");
        try {
            if (csv.exists()) {
                //noinspection ResultOfMethodCallIgnored
                csv.delete();
            }
            //noinspection ResultOfMethodCallIgnored
            csv.createNewFile();
            FileWriter csvWriter = new FileWriter(csv);
            csvWriter.append(String.format("%f", (double)267678237));
            csvWriter.append(",");
            csvWriter.append(String.format("%f", (double)2346432));
            csvWriter.append("\n");
            csvWriter.append(String.format("%f", (double)267678237));
            csvWriter.append(",");
            csvWriter.append(String.format("%f", (double)2346432));

            csvWriter.flush();
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
