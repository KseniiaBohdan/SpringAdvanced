package beans.daos;

import beans.models.Event;
import beans.models.Ticket;
import beans.models.User;

import java.util.List;

import static java.util.Objects.isNull;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/4/2016
 * Time: 10:21 AM
 */
public interface BookingDAO {

    Ticket create(User user, Ticket ticket);

    void delete(User user, Ticket ticket);

    List<Ticket> getTickets(Event event);

    List<Ticket> getTickets(User user);

    long countTickets(User user);

    List<Ticket> getAllTickets();

    static void validateUser(User user) {
        if (isNull(user)) {
            throw new NullPointerException("User is [null]");
        }
        if (isNull(user.getEmail())) {
            throw new NullPointerException("User email is [null]");
        }
    }

    static void validateTicket(Ticket ticket) {
        if (isNull(ticket)) {
            throw new NullPointerException("Ticket is [null]");
        }
    }
}
