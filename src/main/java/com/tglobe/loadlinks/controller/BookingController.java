package com.tglobe.loadlinks.controller;

import com.tglobe.loadlinks.model.Booking;
import com.tglobe.loadlinks.service.BookingService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) { this.service = service; }

    @PostMapping
    public Booking create(@RequestBody Booking booking) { return service.createBooking(booking); }

    @GetMapping
    public List<Booking> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Optional<Booking> getOne(@PathVariable Long id) { return service.getById(id); }

    @PutMapping("/{id}")
    public Booking update(@PathVariable Long id, @RequestBody Booking booking) { 
        return service.updateBooking(id, booking); 
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.deleteBooking(id); }
}
