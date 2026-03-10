package com.tglobe.loadlinks.service;

import com.tglobe.loadlinks.model.Booking;
import com.tglobe.loadlinks.repository.BookingRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository repository;

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    public Booking createBooking(Booking booking) {
        // Business Rule: Automatic categorization
        if (booking.getWeight() > 5000) {
            booking.setLoadType("HEAVY_DUTY");
        }
        return repository.save(booking);
    }

    public List<Booking> getAll() { return repository.findAll(); }

    public Optional<Booking> getById(Long id) { return repository.findById(id); }

    public Booking updateBooking(Long id, Booking details) {
        Booking booking = repository.findById(id).orElseThrow();
        booking.setPickup(details.getPickup());
        booking.setDropoff(details.getDropoff());
        booking.setStatus(details.getStatus());
        return repository.save(booking);
    }

    public void deleteBooking(Long id) { repository.deleteById(id); }
}
