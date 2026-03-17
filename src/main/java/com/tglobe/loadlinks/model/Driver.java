package com.tglobe.loadlinks.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "drivers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Driver extends User {
    
    @NotBlank(message = "License number is required")
    @Column(unique = true)
    private String licenseNumber;
    
    private String status; // AVAILABLE, ON_TRIP, OFF_DUTY, ON_LEAVE
    
    private Double rating; // 1-5 star rating
    
    private Integer totalTrips;
    
    private String vehicleAssigned; // License plate of assigned vehicle
    
    // Location fields for proximity matching
    private Double currentLatitude;
    private Double currentLongitude;
    private String currentLocation;
    
    private String emergencyContact;
    
    @Column(columnDefinition = "TEXT")
    private String notes;

    // Default constructor
    public Driver() {
        super();
        this.setUserType("DRIVER");
        this.rating = 5.0;
        this.totalTrips = 0;
        this.status = "AVAILABLE";
    }

    // Constructor with required fields
    public Driver(String email, String password, String firstName, String lastName, 
                  String licenseNumber) {
        super(email, password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.licenseNumber = licenseNumber;
        this.setUserType("DRIVER");
        this.rating = 5.0;
        this.totalTrips = 0;
        this.status = "AVAILABLE";
    }

    // ===== GETTERS AND SETTERS =====
    
    public String getLicenseNumber() { 
        return licenseNumber; 
    }
    
    public void setLicenseNumber(String licenseNumber) { 
        this.licenseNumber = licenseNumber; 
    }

    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }

    public Double getRating() { 
        return rating; 
    }
    
    public void setRating(Double rating) { 
        this.rating = rating; 
    }

    public Integer getTotalTrips() { 
        return totalTrips; 
    }
    
    public void setTotalTrips(Integer totalTrips) { 
        this.totalTrips = totalTrips; 
    }

    public String getVehicleAssigned() { 
        return vehicleAssigned; 
    }
    
    public void setVehicleAssigned(String vehicleAssigned) { 
        this.vehicleAssigned = vehicleAssigned; 
    }

    public Double getCurrentLatitude() { 
        return currentLatitude; 
    }
    
    public void setCurrentLatitude(Double currentLatitude) { 
        this.currentLatitude = currentLatitude; 
    }

    public Double getCurrentLongitude() { 
        return currentLongitude; 
    }
    
    public void setCurrentLongitude(Double currentLongitude) { 
        this.currentLongitude = currentLongitude; 
    }

    public String getCurrentLocation() { 
        return currentLocation; 
    }
    
    public void setCurrentLocation(String currentLocation) { 
        this.currentLocation = currentLocation; 
    }

    public String getEmergencyContact() { 
        return emergencyContact; 
    }
    
    public void setEmergencyContact(String emergencyContact) { 
        this.emergencyContact = emergencyContact; 
    }

    public String getNotes() { 
        return notes; 
    }
    
    public void setNotes(String notes) { 
        this.notes = notes; 
    }

    // ===== HELPER METHODS =====
    
    @Override
    public String toString() {
        return "Driver{" +
                "id=" + getId() +
                ", name='" + getFirstName() + " " + getLastName() + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", status='" + status + '\'' +
                ", rating=" + rating +
                ", totalTrips=" + totalTrips +
                '}';
    }
}