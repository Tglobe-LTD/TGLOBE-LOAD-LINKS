package com.tglobe.loadlinks.model;

import javax.persistence.*; //
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String customerName;

    private String loadType;

    @Positive(message = "Weight must be greater than zero")
    private Double weight;

    @NotBlank(message = "Pickup location is required")
    private String pickup;
    private Double pickupLatitude;
    private Double pickupLongitude;

    @NotBlank(message = "Dropoff location is required")
    private String dropoff;
    private Double dropoffLatitude;
    private Double dropoffLongitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver assignedDriver;

    private String status = "PENDING";
    
    private LocalDateTime createdAt;

    public Booking() {}

    public Booking(Customer customer, String loadType, Double weight, String pickup, String dropoff) {
        this.customer = customer;
        this.customerName = (customer != null) ? customer.getName() : null;
        this.loadType = loadType;
        this.weight = weight;
        this.pickup = pickup;
        this.dropoff = dropoff;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { 
        this.customer = customer;
        if(customer != null) this.customerName = customer.getName();
    }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getLoadType() { return loadType; }
    public void setLoadType(String loadType) { this.loadType = loadType; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getPickup() { return pickup; }
    public void setPickup(String pickup) { this.pickup = pickup; }

    public String getDropoff() { return dropoff; }
    public void setDropoff(String dropoff) { this.dropoff = dropoff; }

    public Driver getAssignedDriver() { return assignedDriver; }
    public void setAssignedDriver(Driver assignedDriver) { this.assignedDriver = assignedDriver; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getPickupLatitude() { return pickupLatitude; }
    public void setPickupLatitude(Double pickupLatitude) { this.pickupLatitude = pickupLatitude; }

    public Double getPickupLongitude() { return pickupLongitude; }
    public void setPickupLongitude(Double pickupLongitude) { this.pickupLongitude = pickupLongitude; }

    public Double getDropoffLatitude() { return dropoffLatitude; }
    public void setDropoffLatitude(Double dropoffLatitude) { this.dropoffLatitude = dropoffLatitude; }

    public Double getDropoffLongitude() { return dropoffLongitude; }
    public void setDropoffLongitude(Double dropoffLongitude) { this.dropoffLongitude = dropoffLongitude; }

    @PrePersist
    public void prePersist() {
        if (this.status == null) this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }
}