package com.Projekat.Knjizara.controller;

import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.Receipt;
import com.Projekat.Knjizara.models.User;
import com.Projekat.Knjizara.models.enums.EType;
import com.Projekat.Knjizara.service.ReceiptService;
import com.Projekat.Knjizara.service.UserService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping(value="/Korisnici")
public class UserController {

    public static final String USER_KEY = "loggedUser";

    @Autowired
    private UserService userService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private HttpSession session;

    private String bURL;

    @PostConstruct
    public void init() {
        bURL = servletContext.getContextPath() + "/";
    }


    @GetMapping
    public ModelAndView index(HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        List<User> users = userService.findAll();

        ModelAndView result = new ModelAndView("userPage");
        result.addObject("user", loggedUser);
        result.addObject("users", users);

        return result;
    }

    @GetMapping(value = "/Registracija")
    public ModelAndView create() {
        ModelAndView result = new ModelAndView("register");
        return result;
    }


    @PostMapping(value = "/Registracija")
    public ModelAndView create(@Valid User client, BindingResult bindingResult, HttpServletResponse response) throws IOException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateOfRegistration = dtf.format(now);

        client.setUserType(EType.CLIENT);
        client.setActive(true);
        client.setDateOfRegistration(dateOfRegistration);

        if (bindingResult.hasErrors()) {
            ModelAndView error = new ModelAndView("register");
            error.addObject("client", client);
            return error;
        }

        userService.save(client);
        response.sendRedirect(bURL);
        return null;
    }

    @PostMapping(value = "/Login")
    public ModelAndView postLogin(@RequestParam(required = false) String username, @RequestParam(required = false) String password,
                                  HttpServletResponse response) throws IOException {
        try {
            // validacija
            User loggedUser = userService.checkLogin(username, password);

            if (loggedUser == null) {
                System.out.println("Dati korisnik nije pronadjen");
                throw new Exception("Neispravno korisničko ime ili lozinka!");
            }

            // prijava
            System.out.println("Ulogovan je : " + loggedUser.getUserName());
            session.setAttribute(UserController.USER_KEY, loggedUser);

            response.sendRedirect(bURL + "Knjige");
            return null;
        } catch (Exception ex) {
            // ispis greške
            String message = ex.getMessage();
            if (message.isEmpty()) {
                message = "Neuspešna prijava!";
            }

            // prosleđivanje
            ModelAndView result = new ModelAndView("failedLogin");
            result.addObject("message", message);

            return result;
        }
    }

    @GetMapping(value="/Logout")
    public void logout(HttpServletResponse response) throws IOException {
        session.invalidate();

        System.out.println("Korisnik odjavljen");

        response.sendRedirect(bURL);
    }

    @GetMapping(value = "/Detalji")
    public ModelAndView details(@RequestParam String username, HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        User user = userService.findOne(username);

        List<Receipt> receipts = receiptService.findByUser(username);

        ModelAndView result = new ModelAndView("specificUser");
        result.addObject("user", loggedUser);
        result.addObject("wantedUser", user);
        result.addObject("receipts", receipts);
        return result;
    }
}