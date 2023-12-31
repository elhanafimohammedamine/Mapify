package mapify.mapify.Controllers;

import mapify.mapify.Models.User;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

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
            Pattern pattern = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            while ((line = fileReader.readLine()) != null) {
                String[] values = pattern.split(line);
                if (!headersProcessed) {
                    headers = values;
                    headersProcessed = true;
                } else {
                    String firstName = values[headersIndexes.get("firstname")];
                    String lastName = values[headersIndexes.get("lastname")];
                    String address = values[headersIndexes.get("address")].replaceAll("\"", "").replaceAll("'"," ");
                    User userItem = new User(firstName,lastName,address);
                    if (headersIndexes.containsKey("phonenumber")){
                        String phoneNumber = values[headersIndexes.get("phonenumber")];
                        userItem.setPhoneNumber(phoneNumber);
                    }
                    else {
                        userItem.setPhoneNumber("No phone number provided");
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
    private List<String> updateCSVFileWithLocation(File csvFile, List<User> users) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(csvFile))){
            String line;
            while ((line = fileReader.readLine()) != null) {
                lines.add(line);
            }
            String newHeader = lines.get(0) + ",Address Latitude,Address Longitude";
            lines.set(0,newHeader);
            for (int i = 1; i < lines.size(); i++) {
                String userAddressLat = users.get(i -1).getAddressLocation() != null ?
                        String.valueOf(users.get(i -1).getAddressLocation().latitude()) : "None";
                String userAddressLng = users.get(i -1).getAddressLocation() != null ?
                        String.valueOf(users.get(i -1).getAddressLocation().longitude()) : "None";
                String updatedLine = lines.get(i) + "," + userAddressLat + "," + userAddressLng;
                lines.set(i, updatedLine);
                if (i == users.size())
                    break;
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return lines;
    }

    public void copyCsvFileIntoFileToSave(File sourceFile, File fileToSave, List<User> users) {
        List<String> lines = this.updateCSVFileWithLocation(sourceFile,users);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int indexOfHeader(String header, List<String> headers) {
        return headers.indexOf(header);
    }
    public HashMap<String, Integer> getHeadersIndexes() {
        return headersIndexes;
    }
}
