package beans.controller;

import beans.models.Ticket;
import beans.services.BookingService;
import beans.services.EventService;
import beans.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static beans.controller.util.ControllerHandler.*;
import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private EventService eventService;

    @GetMapping("/ticket/price/get")
    public String getTicketPrice(@RequestParam String eventName,
                                 @RequestParam String auditoriumName,
                                 @RequestParam Long date,
                                 @RequestParam Integer[] seats,
                                 @RequestParam Long userId,
                                 @ModelAttribute("model") ModelMap model) {
        model.addAttribute("price", 123.5);
//        LocalDateTime localDateTime = ofInstant(ofEpochMilli(date), systemDefault());
//        model.addAttribute("price", bookingService.getTicketPrice(eventName, auditoriumName,
//                localDateTime, asList(seats), userService.getById(userId)));
        return "ticket-price";

    }

    @GetMapping("/ticket/get")
    public String getTicketsForEvent(
            @RequestParam String eventName,
            @RequestParam String auditoriumName,
            @RequestParam Long date,
            @ModelAttribute("model") ModelMap model) {
        LocalDateTime localDateTime = ofInstant(ofEpochMilli(date), systemDefault());
        model.addAttribute("tickets", createTicketList(10, localDateTime, eventName, auditoriumName));
        //model.addAttribute("tickets",bookingService.getTicketsForEvent(eventName, auditoriumName, localDateTime));
        return "ticket-for-event";
    }

    @GetMapping(path = "/ticket/get", headers = "accept=application/pdf")
    public String getTicketsForEvent(
            @RequestParam String eventName,
            @RequestParam String auditoriumName,
            @RequestParam Long date,
            Model model) {
        LocalDateTime localDateTime = ofInstant(ofEpochMilli(date), systemDefault());
        List<Ticket> ticketList = createTicketList(10, localDateTime, eventName, auditoriumName);
        model.addAttribute("tickets", ticketList);
        //model.addAttribute("tickets",bookingService.getTicketsForEvent(eventName, auditoriumName, localDateTime));
        return "ticket-for-event";
    }

    @PostMapping("ticket/book")
    public String bookTicket(
            @RequestBody Ticket ticket,
            @RequestParam Long userId,
            @ModelAttribute ModelMap model) {
        Ticket bookedTicket = createTicket(ticket.getDateTime(), ticket.getEvent().getName(), ticket.getEvent().getAuditorium().getName(), 1);
        bookedTicket.setUser(createUserById(userId));
        model.addAttribute("ticket", bookedTicket);
        //model.addAttribute("ticket",bookingService.bookTicket(userService.getById(userId), ticket));
        return "ticket-book";
    }

}
