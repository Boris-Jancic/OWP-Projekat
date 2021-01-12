package com.Projekat.Knjizara.controller;

import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.User;
import com.Projekat.Knjizara.models.enums.ECover;
import com.Projekat.Knjizara.models.enums.ELetter;
import com.Projekat.Knjizara.models.enums.EType;
import com.Projekat.Knjizara.service.BookService;
import com.Projekat.Knjizara.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/Knjige")
public class BookController {

    @Autowired
    private BookService bookService;
    private UserService userService;


    @Autowired
    private ServletContext servletContext;
    private String bURL;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @PostConstruct
    public void init() {
        bURL = servletContext.getContextPath() + "/";
    }

    @GetMapping
    public ModelAndView Index(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

        User user = (User) request.getSession().getAttribute(UserController.USER_KEY);

        if(user == null) {
            response.sendRedirect(bURL);
            return null;
        }

        List<Book> books = bookService.findAll();

        ModelAndView rezultat = new ModelAndView("bookPage");
        rezultat.addObject("books", books);
        rezultat.addObject("user", user);

        return rezultat;
    }


    @GetMapping(value = "/Detalji")
    public ModelAndView details(@RequestParam String isbn, HttpServletResponse response, HttpSession session) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        Book book = bookService.findOne(isbn);

        ModelAndView result = new ModelAndView("specificBook");
        result.addObject("book", book);
        result.addObject("user", loggedUser);
        return result;
    }

    @GetMapping(value = "/Dodaj")
    public ModelAndView create(HttpServletResponse response, HttpSession session) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        ModelAndView result = new ModelAndView("addBook");
        result.addObject("user", loggedUser);
        return result;
    }

    @PostMapping(value = "/Dodaj")
    public void create(@RequestParam String name, @RequestParam String isbn, @RequestParam String authors,
                       @RequestParam String publisher, @RequestParam Date released, @RequestParam String description,
                       @RequestParam String picture, @RequestParam int pages, @RequestParam String coverType,
                       @RequestParam String language, @RequestParam float price, @RequestParam String letterType,
                       @RequestParam int remaining,
                       HttpServletResponse response, HttpSession session) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return;
        }

        Book book = new Book();

        book.setName(name);
        book.setIsbn(isbn);
        book.setPublisher(publisher);
        book.setAuthors(authors);
        book.setYearOfRelease(released);
        book.setDescription(description);
        book.setPicture(picture);
        book.setNumOfPages(pages);
        book.setTypeOfCover(ECover.valueOf(coverType));
        book.setLetter(ELetter.valueOf(letterType));
        book.setPrice(price);
        book.setLanguage(language);
        book.setRemaining(remaining);

        bookService.save(book);
        response.sendRedirect(bURL + "Knjige");
    }

    @GetMapping(value = "/Izmeni")
    public ModelAndView edit(@RequestParam String isbn, HttpServletResponse response, HttpSession session) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null || !loggedUser.getUserType().equals(EType.ADMIN)) {
            response.sendRedirect(bURL);
            return null;
        }

        Book book = bookService.findOne(isbn);

        ModelAndView result = new ModelAndView("editBook");
        result.addObject("user", loggedUser);
        result.addObject("book", book);
        return result;
    }

    @PostMapping(value = "/Izmeni")
    public void edit(@RequestParam String name, @RequestParam String isbn, @RequestParam String authors,
                       @RequestParam String publisher, @RequestParam Date released, @RequestParam String description,
                       @RequestParam String picture, @RequestParam int pages, @RequestParam String coverType,
                       @RequestParam String language, @RequestParam float price, @RequestParam String letterType,
                       @RequestParam int remaining,
                       HttpServletResponse response, HttpSession session) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null || !loggedUser.getUserType().equals(EType.ADMIN)) {
            response.sendRedirect(bURL);
            return;
        }

        Book book = new Book();

        book.setName(name);
        book.setIsbn(isbn);
        book.setPublisher(publisher);
        book.setAuthors(authors);
        book.setYearOfRelease(released);
        book.setDescription(description);
        book.setPicture(picture);
        book.setNumOfPages(pages);
        book.setTypeOfCover(ECover.valueOf(coverType));
        book.setLetter(ELetter.valueOf(letterType));
        book.setPrice(price);
        book.setLanguage(language);
        book.setRemaining(remaining);

        bookService.update(book);
        response.sendRedirect(bURL + "Knjige");
    }

    @GetMapping(value = "/Pretraga")
    public ModelAndView create(@RequestParam(required = false) String name, @RequestParam(required = false) String genre,
                               @RequestParam(required = false) float minPrice, @RequestParam(required = false) float maxPrice,
                               @RequestParam(required = false) String author, @RequestParam(required = false) String language,
                               HttpServletResponse response, HttpSession session) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null || !loggedUser.getUserType().equals(EType.ADMIN)) {
            response.sendRedirect(bURL);
            return null;
        }

        List<Book> books = bookService.find(name, minPrice, maxPrice, author, language); //TODO : Dodaj zanr

        ModelAndView result = new ModelAndView("searchPage");

        result.addObject("books", books);
        result.addObject("user", loggedUser);

        return result;
    }
}
