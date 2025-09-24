//∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗
//∗ @file: Parser.java
//∗ @description: This program is used to parse values from a Car csv file and output to a txt file
//∗ @author: Eric Gao
//∗ @date: September 22, 2025
//∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗
import java.io.*;
import java.util.Locale;

public class Parser {

    private final BST<Car> mybst = new BST<>();
    private static final String OUT = "./output.txt";

    public Parser(String filename) throws FileNotFoundException { process(new File(filename)); }

    // Read commands and dispatch
    public void process(File input) throws FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            String raw;
            while ((raw = br.readLine()) != null) {
                String line = raw.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.replaceAll("\\s+", " ").split(" ", 2);
                String cmd = parts[0].toLowerCase(Locale.ROOT);
                String arg = (parts.length > 1) ? parts[1].trim() : "";

                switch (cmd) {
                    case "load" -> { loadCsv(arg); /* no output per spec */ }
                    case "insert" -> {
                        Car a = Car.fromCsvRow(arg);
                        if (a != null) mybst.insert(a);
                        write( "insert " + arg );
                    }
                    case "search" -> {
                        Car key = Car.fromCsvRow(arg);
                        boolean found = (key != null) && mybst.search(key);
                        write( (found ? "found " : "not found ") + arg );
                    }
                    case "remove" -> {
                        Car key = Car.fromCsvRow(arg);
                        if (key != null) mybst.remove(key);
                        write( "remove " + arg );
                    }
                    case "print" -> {
                        StringBuilder sb = new StringBuilder();
                        for (Car a : mybst) sb.append(a).append(System.lineSeparator());
                        write(sb.toString().isEmpty() ? "" : sb.toString().trim());
                    }
                    default -> write("Invalid Command");
                }
            }
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    private void write(String s){
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(OUT, true)))) {
            pw.println(s);
        } catch (IOException e) {
            System.err.println("write error: " + e.getMessage());
        }
    }

    private void loadCsv(String csvPath) {
        if (csvPath == null || csvPath.isEmpty()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String row; int ln = 0;
            while ((row = br.readLine()) != null) {
                ln++;
                if (ln == 1 && row.toLowerCase(Locale.ROOT).startsWith("name,")) continue; // header
                Car a = Car.fromCsvRow(row);
                if (a != null) mybst.insert(a); // duplicates ignored
            }
        } catch (IOException e) {
            write("Failed to load " + csvPath + ": " + e.getMessage());
        }
    }
}

