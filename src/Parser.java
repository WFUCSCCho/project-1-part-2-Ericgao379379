//∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗
//∗ @file: Parser.java
//∗ @description: This program is used to parse values from a Car csv file and output to a txt file
//∗ @author: Eric Gao
//∗ @date: September 22, 2025
//∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗
import java.util.ArrayList;
import java.io.*;
import java.util.List;


public class Parser {

    //Create a BST tree of Integer type
    private BST<Car> mybst = new BST<>();

    public Parser(String filename) throws FileNotFoundException {
        process(new File(filename));
    }

    // Implement the process method
    // Remove redundant spaces for each input command
    public void process(File input) throws FileNotFoundException {
        final String out = "./result.txt";
        try {
            new File(out).delete();
        }
        catch (Exception ignored) {}

        try (BufferedReader br = new BufferedReader(new FileReader(input))){
            String headerLine = br.readLine();
            if (headerLine == null) {
                writeToFile("Empty file", out);
                return;
              }

            if (!isHeader(headerLine)){
                handleLine(headerLine, out);
            }

            String Line;
            int rows = 0;
            while ((Line = br.readLine()) != null) {
                if (Line.isBlank()){
                    continue;
                }
                if (handleLine(Line, out)){
                    rows++;
                }

            }
            writeToFile("Loaded " + rows + " rows, BST size = " + mybst.size(), out);

            for (Car c: mybst){
                writeToFile(c.toString(), out);
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        //call operate_BST method;
    }

    public boolean handleLine(String line, String out){
        String s = line.trim();
        String lower = s.toLowerCase();

        if (lower.startsWith("insert ")){
            // insert line
            String actual = s.substring(7);
            operate_BST(new String[]{"insert",actual});
            return true;
        }
        else if (lower.startsWith("remove ")){
            // delete line
            String actual = s.substring(7);
            operate_BST(new String[]{"remove",actual});
            return true;
        }
        else if (lower.startsWith("search ")){
            // search
            String actual = s.substring(7);
            operate_BST(new String[]{"search",actual});
            return true;
        }
        else {
            // plain CSV row
            operate_BST(new String[]{"insert",s});
            return true;
        }

    }

    public boolean isHeader(String line){
        String l = line.toLowerCase().trim();
        return l.startsWith("name,mpg,cylinders,displacement,horsepower,weight,acceleration,model_year,origin");
    }


    // Implement the operate_BST method
    // Determine the incoming command and operate on the BST
    public void operate_BST(String[] command) {
        final String out = "./result.txt";
        if (command == null || command.length < 2) {
            writeToFile("Invalid command", out);
            return;
        }
        String verb = command[0].toLowerCase();
        String payload = command[1];

        switch (verb) {
            case "insert": {
                java.util.List<String> insertTokens = splitCsv(payload);
                Car insertCar = carFromTokens(insertTokens);
                if (insertCar == null) {break;}
                mybst.insert(insertCar);
                break;
            }

            case "remove": {
                java.util.List<String> removeTokens = splitCsv(payload);
                Car removeCar = carFromTokens(removeTokens);
                if (removeCar == null) {break; }
                mybst.remove(removeCar);
                break;
            }

            case "search": {
                java.util.List<String> searchTokens = splitCsv(payload);
                Car searchKey = null;

                if (searchTokens.size() >= 9) {
                    searchKey = carFromTokens(searchTokens); // full row works
                } else if (searchTokens.size() == 3) {
                    // compact key: name,model_year,mpg
                    String name = unquote(searchTokens.get(0).trim());
                    Integer year = parseInt(searchTokens.get(1));
                    Double mpg   = parseDouble(searchTokens.get(2));
                    if (year != null && mpg != null) {
                        searchKey = new Car(name, mpg, 0, 0.0, 0, 0, 0.0, year, "");
                    }
                }

                if (searchKey == null) {break; }
                boolean found = mybst.search(searchKey);
                writeToFile(found ? "[FOUND] " + searchKey : "[NOT FOUND] " + searchKey, out);
                break;
            }

            default: {
                writeToFile("Invalid Command", out);
                break;
            }
        }

    }

    // Implement the writeToFile method
    // Generate the result file
    public void writeToFile(String content, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(content);
            bw.newLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }




    private static String unquote(String s) {
        s = s.trim();
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\""))
            return s.substring(1, s.length() - 1);
        return s;
    }

    private static int parseInt(String s) { return Integer.parseInt(s.trim()); }


    private static double parseDouble(String s) { return Double.parseDouble(s.trim()); }

    private static List<String> splitCsv(String s) {
        List<String> out = new ArrayList<>(9);
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '"') {
                if (inQuotes && i + 1 < s.length() && s.charAt(i + 1) == '"') {
                    // escaped quote
                    cur.append('"'); i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                out.add(cur.toString().trim());
                cur.setLength(0);
            } else {
                cur.append(ch);
            }
        }
        out.add(cur.toString().trim());
        return out;
    }

    private static Car carFromTokens(List<String> t) {
        // Expect at least the 9 required CSV fields
        if (t == null || t.size() < 9) return null;

        // CSV order: name, mpg, cylinders, displacement, horsepower, weight, acceleration, model_year, origin
        String name   = unquote(t.get(0)).trim();
        String mpgS   = t.get(1).trim();
        String cylS   = t.get(2).trim();
        String dispS  = t.get(3).trim();
        String hpS    = t.get(4).trim();
        String wtS    = t.get(5).trim();
        String accS   = t.get(6).trim();
        String yrS    = t.get(7).trim();
        String origin = unquote(t.get(8)).trim();

        // Required fields (horsepower is optional): skip row if any required is blank/"?"
        if (name.isEmpty() || origin.isEmpty() ||
                mpgS.isEmpty() || cylS.isEmpty() || dispS.isEmpty() ||
                wtS.isEmpty()  || accS.isEmpty() || yrS.isEmpty() ||
                "?".equals(mpgS) || "?".equals(cylS) || "?".equals(dispS) ||
                "?".equals(wtS)  || "?".equals(accS) || "?".equals(yrS)) {
            return null;
        }

        try {
            double mpg          = Double.parseDouble(mpgS);
            int    cylinders    = Integer.parseInt(cylS);
            double displacement = Double.parseDouble(dispS);
            int    horsepower   = (hpS.isEmpty() || "?".equals(hpS)) ? -1 : Integer.parseInt(hpS);
            int    weight       = Integer.parseInt(wtS);
            double acceleration = Double.parseDouble(accS);
            int    modelYear    = Integer.parseInt(yrS);

            return new Car(name, mpg, cylinders, displacement, horsepower, weight, acceleration, modelYear, origin);
        } catch (NumberFormatException e) {
            // Any bad number -> skip this row
            return null;
        }
    }

}
