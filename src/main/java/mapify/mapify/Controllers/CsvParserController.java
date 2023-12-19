package mapify.mapify.Controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CsvParserController {
    private File csvFile = null;
    private HashMap<String, Integer> headersIndexes = new HashMap<>();
    private final ArrayList<String> headersCheckers = new ArrayList<>(List.of("first name", "last name", "address"));

    public boolean checkForFileHeaders() {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(csvFile))){
            String headersLine = fileReader.readLine();
            if (headersLine != null) {
                String [] headers = headersLine.split(",");
                for (String headersChecker : headersCheckers) {
                    if (indexOfHeader(headersChecker, headers) == -1 )
                        return false;
                }
                for (String header : headers) {
                    headersIndexes.put(header, indexOfHeader(header, headers));
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    private static int indexOfHeader(String header, String [] headers) {
        for (int i = 0; i < headers.length; i++) {
            if (header.equals(headers[i]) || header.equalsIgnoreCase(headers[i]))
                return i;
        }
        return -1;
    }
    private HashMap<String, Integer> getHeadersIndexes() {
        return headersIndexes;
    }
    public void setCsvFile(File file) {
        if (csvFile != null) {
            csvFile = file;
        }
    }
}
