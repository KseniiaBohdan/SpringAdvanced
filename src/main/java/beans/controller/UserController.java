package beans.controller;

import beans.models.User;
import beans.services.UserService;
import beans.services.json.JsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static beans.controller.util.ControllerHandler.*;
import static java.util.Objects.nonNull;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    private JsonHandler<User> jsonHandler = new JsonHandler<>(User.class);

    @PostMapping("/register")
    public boolean registerUser(@RequestBody User user) {
        return nonNull(userService.register(user));
    }

    @PostMapping("/register/fromFile")
    public void registerUser(@RequestParam MultipartFile file) throws IOException {
        userService.register(jsonHandler.getObjectFromFile(file));
    }

    @PostMapping("/remove")
    public void removeUser(@RequestBody User user) {
        userService.remove(user);
    }

    @GetMapping("/get/{id}")
    public String getUserById(
            @PathVariable Long userId,
            @ModelAttribute ModelMap model) {
        //model.addAttribute("user",userService.getById(userId));
        model.addAttribute("user", createUserById(userId));
        return "user-get-by-id";
    }

    @GetMapping("/get/{email}")
    public String getUserByEmail(
            @PathVariable String email,
            @ModelAttribute ModelMap model) {
        // model.addAttribute("user",userService.getUserByEmail(email));
        model.addAttribute("user", createUserByEmail(email));
        return "user-get-by-email";
    }

    @GetMapping("/get/{name}")
    public String getUserByName(
            @PathVariable String name,
            @ModelAttribute ModelMap model) {
//        model.addAttribute("users",userService.getUsersByName(name));
        model.addAttribute("user", createUserByName(name));
        return "user-get-by-name";
    }

    @GetMapping("/ticket/get")
    public String getTicketByUser(
            @RequestParam Long userId,
            @ModelAttribute("model") ModelMap model) {
        //       throw new UnsupportedOperationException();
        model.addAttribute("tickets", createTicketList(10, null, null, null));
        return "user-get-tickets";
    }

    @GetMapping(
            path = "/ticket/get",
            headers = "accept=application/pdf")
    public String getTicketByUser(
            @RequestParam Long userId,
            Model model) {
//        throw new UnsupportedOperationException();
        model.addAttribute("tickets", createTicketList(10, null, null, null));
        return "user-get-tickets";
    }

    @GetMapping("/file/upload")
    public String getUploadFilePage() {
        return "upload-file";
    }

}
