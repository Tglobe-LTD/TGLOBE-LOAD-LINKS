package com.tglobe.loadlinks.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer extends User {
    
    private String companyName;
    private String address;
    private String taxId;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();
    
    private Integer totalBookings = 0;
    private String preferredLoadType;

    public Customer() {
        super();
        this.setUserType("CUSTOMER");
    }

    // Getters and Setters
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public Integer getTotalBookings() { return totalBookings; }
    public void setTotalBookings(Integer totalBookings) { this.totalBookings = totalBookings; }

    public String getPreferredLoadType() { return preferredLoadType; }
    public void setPreferredLoadType(String preferredLoadType) { this.preferredLoadType = preferredLoadType; }
    
    // Helper method to add booking
    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setCustomer(this);
        totalBookings = bookings.size();
    }
}