package mapify.mapify.Models;

import mapify.mapify.Controllers.Controller;

public class User {
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private Controller.Location addressLocation;
    private String  durationToDeviceLocationWithFoot;
    private String  durationToDeviceLocationWithCar;
    private int distanceToDeviceLocation;

    public User() {
    }
    public User(String firstName, String lastName, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Controller.Location getAddressLocation() {
        return addressLocation;
    }

    public void setAddressLocation(Controller.Location addressLocation) {
        this.addressLocation = addressLocation;
    }

    public String getDurationToDeviceLocationWithFoot() {
        return durationToDeviceLocationWithFoot;
    }

    public void setDurationToDeviceLocationWithFoot(String durationToDeviceLocationWithFoot) {
        this.durationToDeviceLocationWithFoot = durationToDeviceLocationWithFoot;
    }

    public String getDurationToDeviceLocationWithCar() {
        return durationToDeviceLocationWithCar;
    }

    public void setDurationToDeviceLocationWithCar(String  durationToDeviceLocationWithCar) {
        this.durationToDeviceLocationWithCar = durationToDeviceLocationWithCar;
    }

    public int getDistanceToDeviceLocation() {
        return distanceToDeviceLocation;
    }

    public void setDistanceToDeviceLocation(int distanceToDeviceLocation) {
        this.distanceToDeviceLocation = distanceToDeviceLocation;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", addressLocation=" + addressLocation +
                ", durationToDeviceLocationWithFoot=" + durationToDeviceLocationWithFoot +
                ", durationToDeviceLocationWithCar=" + durationToDeviceLocationWithCar +
                ", distanceToDeviceLocation=" + distanceToDeviceLocation +
                '}';
    }

}
