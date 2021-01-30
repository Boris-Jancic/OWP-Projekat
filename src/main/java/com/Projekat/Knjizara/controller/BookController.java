package com.Projekat.Knjizara.controller;

import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.BoughtBook;
import com.Projekat.Knjizara.models.Receipt;
import com.Projekat.Knjizara.models.User;
import com.Projekat.Knjizara.models.enums.ECover;
import com.Projekat.Knjizara.models.enums.ELetter;
import com.Projekat.Knjizara.models.enums.EType;
import com.Projekat.Knjizara.service.BookService;
import com.Projekat.Knjizara.service.BoughtBookService;
import com.Projekat.Knjizara.service.ReceiptService;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.thymeleaf.util.StringUtils.isEmpty;
import static org.thymeleaf.util.StringUtils.randomAlphanumeric;

@Controller
@RequestMapping(value = "/Knjige")
public class BookController {

    public static final String SHOPPING_CART_KEY = "shoppingCart";

    @Autowired
    private BookService bookService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private BoughtBookService boughtBookService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private HttpSession session;
    
    private String bURL;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @PostConstruct
    public void init() {
        bURL = servletContext.getContextPath() + "/";
    }

    @GetMapping
    public ModelAndView Index(HttpServletRequest request, HttpServletResponse response) throws IOException {

        User user = (User) request.getSession().getAttribute(UserController.USER_KEY);

        if (user == null) {
            response.sendRedirect(bURL);
            return null;
        }

        List<Book> books = bookService.findAll();
        ModelAndView result = new ModelAndView("bookPage");

        if (user.isAdmin()){
            result.addObject("books", books);
        } else {
            List<Book> clientBooks = new ArrayList<>();

            for (Book b : books) {
                if (b.getRemaining() > 0 || b.isActive())
                    clientBooks.add(b);
            }
            result.addObject("books", clientBooks);
        }

        result.addObject("user", user);
        return result;
    }


    @GetMapping(value = "/Detalji")
    public ModelAndView details(@RequestParam String isbn, HttpServletResponse response) throws IOException {

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
    public ModelAndView create(HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        ModelAndView result = new ModelAndView("addBook");
        result.addObject("user", loggedUser);
        return result;
    }

    private String getISBNString() {
        String numChars = "1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        Random rnd = new Random();
        while (stringBuilder.length() < 13) {
            int index = (int) (rnd.nextFloat() * numChars.length());
            stringBuilder.append(numChars.charAt(index));
        }
        String saltStr = stringBuilder.toString();
        return saltStr;
    }

    @PostMapping(value = "/Dodaj")
    public ModelAndView create(@Valid Book book, BindingResult bindingResult, String cover, String letter,
                       HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        book.setIsbn(getISBNString());
        book.setTypeOfCover(ECover.valueOf(cover));
        book.setLetter(ELetter.valueOf(letter));
        book.setRemaining(0);
        book.setActive(true);

        if (bindingResult.hasErrors()) {
            ModelAndView error = new ModelAndView("addBook");
            System.out.println(book);
            error.addObject("user", loggedUser);
            return error;
        }

        bookService.save(book);
        response.sendRedirect(bURL + "Knjige");
        return null;
    }

    @GetMapping(value = "/Izmeni")
    public ModelAndView edit(@RequestParam String isbn, HttpServletResponse response) throws IOException {

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
    public ModelAndView edit(@Valid Book book, BindingResult bindingResult, String cover, String letter, HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null || !loggedUser.getUserType().equals(EType.ADMIN)) {
            response.sendRedirect(bURL);
            return null;
        }

        book.setName(book.getName());
        book.setPublisher(book.getPublisher());
        book.setAuthor(book.getAuthor());
        book.setYearOfRelease(book.getYearOfRelease());
        book.setDescription(book.getDescription());
        book.setPicture(book.getPicture());
        book.setNumOfPages(book.getNumOfPages());
        book.setTypeOfCover(ECover.valueOf(cover));
        book.setLetter(ELetter.valueOf(letter));
        book.setPrice(book.getPrice());
        book.setLanguage(book.getLanguage());
        book.setRemaining(book.getRemaining());
        book.setRating(book.getRating());

        if (bindingResult.hasErrors()) {
            ModelAndView error = new ModelAndView("editBook");
            System.out.println(book);
            error.addObject("user", loggedUser);
            error.addObject("book", book);
            return error;
        }

        bookService.update(book);
        response.sendRedirect(bURL + "Knjige");
        return null;
    }

    @GetMapping(value = "/Pretraga")
    public ModelAndView create(@RequestParam(required = false) String isbn, @RequestParam(required = false) String name,
                               @RequestParam(required = false) String genre, @RequestParam(required = false) float minPrice,
                               @RequestParam(required = false) float maxPrice, @RequestParam(required = false) String author,
                               @RequestParam(required = false) String language,
                               HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        ModelAndView result = new ModelAndView("searchPage");;
        if (isEmpty(isbn) && isEmpty(name) &&  isEmpty(author) && minPrice == 0 && maxPrice == 0 && isEmpty(language)) {
            result.addObject("books", bookService.findAll());
        } else {
            result.addObject("books", bookService.find(isbn, name, minPrice, maxPrice, author, language)); //TODO : Dodaj zanr
        }

        result.addObject("user", loggedUser);

        return result;
    }

    @GetMapping(value = "/Naruci")
    public void edit(@RequestParam String isbn, int wantedCopies,
                     HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null || !loggedUser.getUserType().equals(EType.ADMIN)) {
            response.sendRedirect(bURL);
            return;
        }

        Book wantedBook = bookService.findOne(isbn);
        wantedBook.setRemaining(wantedBook.getRemaining() + wantedCopies);
        bookService.update(wantedBook);

        response.sendRedirect(bURL + "Knjige");
    }

    @GetMapping(value = "/TabelaKnjiga")
    public ModelAndView bookTable(HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        List<Book> books = bookService.findAll();

        ModelAndView result = new ModelAndView("bookTable");
        result.addObject("books", books);
        result.addObject("user", loggedUser);

        return result;
    }

}