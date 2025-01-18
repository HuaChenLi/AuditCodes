package src.Lib;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Logging {
    String filename;
    FileWriter myWriter;
    PrintStream ps;
    FileOutputStream fileOutputStream;
    public Logging(String filename) {
        this.filename = filename;
        try {
            this.fileOutputStream = new FileOutputStream(filename, true);
            this.ps = new PrintStream(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void writeLog(String errorMessage) {
        try {
            this.myWriter = new FileWriter(filename, true);
            String currentTimeStamp;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date now = new Date();
            currentTimeStamp = sdf.format(now);
            myWriter.write(currentTimeStamp + ": " + errorMessage + "\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void writeLog(ArrayList<String> arrayList) {
        try {
            this.myWriter = new FileWriter(filename, true);
            String currentTimeStamp;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            for (String s : arrayList) {
                Date now = new Date();
                currentTimeStamp = sdf.format(now);
                myWriter.write(currentTimeStamp + ": " + s + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public PrintStream getPs() {
        return this.ps;
    }
}
