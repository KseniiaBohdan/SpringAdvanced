package beans.services.impl;

import beans.daos.BookingDAO;
import beans.models.*;
import beans.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/3/2016
 * Time: 11:33 AM
 */
@Service("bookingServiceImpl")
@PropertySource({"classpath:strategies/booking.properties"})
@Transactional
public class BookingServiceImpl implements BookingService {

    private final EventService eventService;
    private final AuditoriumService auditoriumService;
    private final UserService userService;
    private final BookingDAO bookingDAO;
    private final DiscountService discountService;
    final int minSeatNumber;
    final double vipSeatPriceMultiplier;
    final double highRatedPriceMultiplier;
    final double defaultRateMultiplier;

    @Autowired
    public BookingServiceImpl(@Qualifier("eventServiceImpl") EventService eventService,
                              @Qualifier("auditoriumServiceImpl") AuditoriumService auditoriumService,
                              @Qualifier("userServiceImpl") UserService userService,
                              @Qualifier("discountServiceImpl") DiscountService discountService,
                              @Qualifier("bookingDAO") BookingDAO bookingDAO,
                              @Value("${min.seat.number}") int minSeatNumber,
                              @Value("${vip.seat.price.multiplier}") double vipSeatPriceMultiplier,
                              @Value("${high.rate.price.multiplier}") double highRatedPriceMultiplier,
                              @Value("${def.rate.price.multiplier}") double defaultRateMultiplier) {
        this.eventService = eventService;
        this.auditoriumService = auditoriumService;
        this.userService = userService;
        this.bookingDAO = bookingDAO;
        this.discountService = discountService;
        this.minSeatNumber = minSeatNumber;
        this.vipSeatPriceMultiplier = vipSeatPriceMultiplier;
        this.highRatedPriceMultiplier = highRatedPriceMultiplier;
        this.defaultRateMultiplier = defaultRateMultiplier;
    }

    @Override
    public double getTicketPrice(String eventName, String auditoriumName, LocalDateTime dateTime, List<Integer> seats,
                                 User user) {
        if (isNull(eventName) || isNull(seats) || isNull(user) || seats.contains(null)) {
            throw new NullPointerException("Event/Seats/User/Seats_contain name is [null]");
        }

        final Auditorium auditorium = auditoriumService.getByName(auditoriumName);

        final Event event = eventService.getEvent(eventName, auditorium, dateTime);
        if (isNull(event)) {
            throw new IllegalStateException(
                    "There is no event with name: [" + eventName + "] in auditorium: [" + auditorium + "] on date: ["
                            + dateTime + "]");
        }

        final double baseSeatPrice = event.getBasePrice();
        final double rateMultiplier = event.getRate() == Rate.HIGH ? highRatedPriceMultiplier : defaultRateMultiplier;
        final double seatPrice = baseSeatPrice * rateMultiplier;
        final double vipSeatPrice = vipSeatPriceMultiplier * seatPrice;
        final double discount = discountService.getDiscount(user, event);

        validateSeats(seats, auditorium);

        final List<Integer> auditoriumVipSeats = auditorium.getVipSeatsList();
        final List<Integer> vipSeats = auditoriumVipSeats.stream().filter(seats::contains).collect(
                Collectors.toList());
        final List<Integer> simpleSeats = seats.stream().filter(seat -> !vipSeats.contains(seat)).collect(
                Collectors.toList());
        final double simpleSeatsPrice = simpleSeats.size() * seatPrice;
        final double vipSeatsPrice = vipSeats.size() * vipSeatPrice;
        final double totalPrice = simpleSeatsPrice + vipSeatsPrice;

        return (1.0 - discount) * totalPrice;
    }

    private void validateSeats(List<Integer> seats, Auditorium auditorium) {
        final int seatsNumber = auditorium.getSeatsNumber();
        final Optional<Integer> incorrectSeat = seats.stream().filter(
                seat -> seat < minSeatNumber || seat > seatsNumber).findFirst();
        incorrectSeat.ifPresent(seat -> {
            throw new IllegalArgumentException(
                    String.format("Seat: [%s] is incorrect. Auditorium: [%s] has [%s] seats", seat, auditorium.getName(),
                            seatsNumber));
        });
    }

    @Override
    public Ticket bookTicket(User user, Ticket ticket) {
        if (isNull(user)) {
            throw new NullPointerException("User is [null]");
        }
        User foundUser = userService.getById(user.getId());
        if (isNull(foundUser)) {
            throw new IllegalStateException("User: [" + user + "] is not registered");
        }

        List<Ticket> bookedTickets = bookingDAO.getTickets(ticket.getEvent());
        boolean seatsAreAlreadyBooked = bookedTickets
                .stream()
                .anyMatch(bookedTicket -> ticket.getSeatsList().stream().anyMatch(bookedTicket.getSeatsList()::contains));

        if (!seatsAreAlreadyBooked)
            bookingDAO.create(user, ticket);
        else
            throw new IllegalStateException("Unable to book ticket: [" + ticket + "]. Seats are already booked.");

        return ticket;
    }

    @Override
    public List<Ticket> getTicketsForEvent(String event, String auditoriumName, LocalDateTime date) {
        final Auditorium auditorium = auditoriumService.getByName(auditoriumName);
        final Event foundEvent = eventService.getEvent(event, auditorium, date);
        return bookingDAO.getTickets(foundEvent);
    }
}
