package mapify.mapify.Controllers;

import mapify.mapify.Models.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CsvParserController {
    private HashMap<String, Integer> headersIndexes = new HashMap<>();
    private final ArrayList<String> headersCheckers = new ArrayList<>(List.of("firstname", "lastname", "address"));

    public boolean checkForFileHeadersAndFormat(File csvFile) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(csvFile))){
            String line;
            boolean headersProcessed = false;
            String[] headers = null;
            while ((line = fileReader.readLine()) != null) {
                String[] values = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
                if (!headersProcessed) {
                    headers = values;

                    if (!csvHeaderProcessing(line)) {
                        return false;
                    }
                    headersProcessed = true;
                }
                else {
                    if (!checkValuesLength(headers, values))
                        return false;
                }
            }
            return true;
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public List<User> getCSVData(File csvFile) {

        List<User> usersList = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(csvFile))){
            String line;
            boolean headersProcessed = false;
            String[] headers = null;
            while ((line = fileReader.readLine()) != null) {
                String[] values = line.split(",");
                if (!headersProcessed) {
                    headers = values;
                    headersProcessed = true;
                } else {
                    String firstName = values[headersIndexes.get("firstname")];
                    String lastName = values[headersIndexes.get("lastname")];
                    String address = values[headersIndexes.get("address")].replaceAll("\"", "");
                    User userItem = new User(firstName,lastName,address);
                    if (headersIndexes.containsKey("phonenumber")){
                        String phoneNumber = values[headersIndexes.get("phonenumber")];
                        userItem.setPhoneNumber(phoneNumber);
                    }
                    usersList.add(userItem);
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return usersList;
    }

    private boolean checkValuesLength(String[] headers, String[] values ) {
        return headers.length == values.length;
    }

    private boolean csvHeaderProcessing(String headersLine) {
        if (headersLine != null) {
            String [] headers = headersLine.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
            List<String> formattedHeaders = processHeadersTypographie(headers);
            for (String headersChecker : headersCheckers) {
                if (indexOfHeader(headersChecker, formattedHeaders) == -1 )
                    return false;
            }
            for (String header : formattedHeaders) {
                headersIndexes.put(header, indexOfHeader(header, formattedHeaders));
            }
            return true;
        }
        return false;
    }
    private List<String> processHeadersTypographie(String [] headers) {
        List<String> formattedHeaders = new ArrayList<>();
        for (String header : headers) {
            String formattedHeader = header.replace(" ", "").toLowerCase();
            formattedHeaders.add(formattedHeader);
        }
        return formattedHeaders;
    }

    private static int indexOfHeader(String header, List<String> headers) {
        return headers.indexOf(header);
    }
    public HashMap<String, Integer> getHeadersIndexes() {
        return headersIndexes;
    }
}
