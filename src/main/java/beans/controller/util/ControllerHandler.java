package beans.controller.util;

import beans.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
import static java.util.Objects.isNull;

public class ControllerHandler {

    public static final String DEFAULT_EMAIL = "dmitriy.vbabichev@gmail.com";
    public static final String DEFAULT_NAME = "Dmytro Babichev";
    public static final LocalDate DEFAULT_BIRTHDAY = LocalDate.of(1992, 4, 29);
    public static final String DEFAULT_EVENT_NAME = "PARTY";

    public static Event createEvent(String eventName, String auditoriumName, LocalDateTime date) {
        Event event = new Event();
        event.setAuditorium(createAuditoriumByName(auditoriumName));
        event.setBasePrice(123);
        event.setDateTime(date);
        event.setName(isNull(eventName) ? DEFAULT_EVENT_NAME : eventName);
        event.setRate(Rate.MID);
        return event;
    }

    private static Auditorium createAuditoriumByName(String auditoriumName) {
        Auditorium auditorium = new Auditorium();
        auditorium.setId(1);
        auditorium.setName(auditoriumName);
        auditorium.setSeatsNumber(12);
        auditorium.setVipSeats("1,2,3,4,5,7");
        return auditorium;
    }

    public static List<Ticket> createTicketList(int size, LocalDateTime dateTime, String eventName, String auditoriumName) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            tickets.add(createTicket(dateTime, eventName, auditoriumName, i));
        }
        return tickets;
    }

    public static Ticket createTicket(LocalDateTime dateTime, String eventName, String auditoriumName, int id) {
        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setSeats(valueOf(id));
        ticket.setPrice((double) (20 + id));
        ticket.setUser(createUserById((long) id));
        ticket.setDateTime(isNull(dateTime) ? LocalDateTime.now() : dateTime);
        ticket.setEvent(createEvent(eventName, auditoriumName, dateTime));
        return ticket;
    }

    public static User createUserById(Long userId) {
        return new User(isNull(userId) ? 1 : userId, DEFAULT_EMAIL, DEFAULT_NAME,
                DEFAULT_BIRTHDAY);
    }

    public static User createUserByEmail(String email) {
        return new User(1, isNull(email) ? DEFAULT_EMAIL : email,
                DEFAULT_NAME, DEFAULT_BIRTHDAY);
    }

    public static User createUserByName(String name) {
        return new User(1, DEFAULT_EMAIL, isNull(name) ? DEFAULT_NAME : name,
                DEFAULT_BIRTHDAY);
    }
}
