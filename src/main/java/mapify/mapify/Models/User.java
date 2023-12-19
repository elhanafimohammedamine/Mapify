package mapify.mapify.Models;

import mapify.mapify.Controllers.Controller;

public class User {
    private String firstName;
    private String lastName;

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    private String address;
    private String phoneNumber;
    private Controller.Location addressLocation;
    private double distanceToDeviceLocationWithFoot;
    private double distanceToDeviceLocationWithCar;
    private double distanceToDeviceLocation;

    User() {
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

    public double getDistanceToDeviceLocationWithFoot() {
        return distanceToDeviceLocationWithFoot;
    }

    public void setDistanceToDeviceLocationWithFoot(double distanceToDeviceLocationWithFoot) {
        this.distanceToDeviceLocationWithFoot = distanceToDeviceLocationWithFoot;
    }

    public double getDistanceToDeviceLocationWithCar() {
        return distanceToDeviceLocationWithCar;
    }

    public void setDistanceToDeviceLocationWithCar(double distanceToDeviceLocationWithCar) {
        this.distanceToDeviceLocationWithCar = distanceToDeviceLocationWithCar;
    }

    public double getDistanceToDeviceLocation() {
        return distanceToDeviceLocation;
    }

    public void setDistanceToDeviceLocation(double distanceToDeviceLocation) {
        this.distanceToDeviceLocation = distanceToDeviceLocation;
    }

}
