package dokter;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Doctor {
    private IntegerProperty doctorId;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty specialization;
    private StringProperty email;
    private StringProperty phoneNumber;
    private StringProperty address;
    private ObjectProperty<LocalDate> hireDate;

    public Doctor(int doctorId, String firstName, String lastName, String specialization, String email, String phoneNumber, String address, LocalDate hireDate) {
        this.doctorId = new SimpleIntegerProperty(doctorId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.specialization = new SimpleStringProperty(specialization);
        this.email = new SimpleStringProperty(email);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.address = new SimpleStringProperty(address);
        this.hireDate = new SimpleObjectProperty<>(hireDate);
    }

    public int getDoctorId() {
        return doctorId.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getSpecialization() {
        return specialization.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public String getAddress() {
        return address.get();
    }

    public LocalDate getHireDate() {
        return hireDate.get();
    }
}

