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
