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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.thymeleaf.util.StringUtils.randomAlphanumeric;

@Controller
@RequestMapping(value = "/Knjige")
public class BookController {

    @Autowired
    public static final String SHOPPING_CART_KEY = "shoppingCart";

    @Autowired
    private BookService bookService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private BoughtBookService boughtBookService;

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

        if (user == null) {
            response.sendRedirect(bURL);
            return null;
        }

        List<Book> books = bookService.findAll();

        ModelAndView result = new ModelAndView("bookPage");
        result.addObject("books", books);
        result.addObject("user", user);

        return result;
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

        bookService.update(book);
        response.sendRedirect(bURL + "Knjige");
    }

    @GetMapping(value = "/Pretraga")
    public ModelAndView create(@RequestParam(required = false) String name, @RequestParam(required = false) String genre,
                               @RequestParam(required = false) float minPrice, @RequestParam(required = false) float maxPrice,
                               @RequestParam(required = false) String author, @RequestParam(required = false) String language,
                               HttpServletResponse response, HttpSession session) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        List<Book> books = bookService.find(name, minPrice, maxPrice, author, language); //TODO : Dodaj zanr

        ModelAndView result = new ModelAndView("searchPage");
        result.addObject("books", books);
        result.addObject("user", loggedUser);

        return result;
    }

    @GetMapping(value = "/Naruci")
    public void edit(@RequestParam String isbn, int wantedCopies,
                     HttpServletResponse response, HttpSession session) throws IOException {

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

    @GetMapping(value = "/Korpa")
    public ModelAndView viewCart(HttpServletResponse response, HttpSession session) throws IOException {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        if (session.getAttribute(SHOPPING_CART_KEY) == null) {
            ArrayList<BoughtBook> createBoughtBooks = new ArrayList<BoughtBook>();
            session.setAttribute(SHOPPING_CART_KEY, createBoughtBooks);
        }

        ArrayList<BoughtBook> boughtBooks = (ArrayList<BoughtBook>) session.getAttribute(SHOPPING_CART_KEY);

        int finalPrice = 0;
        for (BoughtBook bB : boughtBooks) {
            finalPrice += bB.getPrice();
        }

        ModelAndView result = new ModelAndView("cartPage");
        result.addObject("cartBooks", boughtBooks);
        result.addObject("user", loggedUser);
        result.addObject("finalPrice", finalPrice);
        return result;
    }

    @GetMapping(value = "/DodajUKorpu")
    public ModelAndView addToCart(@RequestParam String isbn, int wantedCopies,
                     HttpServletResponse response, HttpSession session) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        if (session.getAttribute(SHOPPING_CART_KEY) == null) {
            ArrayList<BoughtBook> createBoughtBooks = new ArrayList<BoughtBook>();
            session.setAttribute(SHOPPING_CART_KEY, createBoughtBooks);
        }

        Book wantedBook = bookService.findOne(isbn);
        BoughtBook boughtBook = new BoughtBook();

        boughtBook.setId(randomAlphanumeric(9));
        boughtBook.setUsername(loggedUser.getUserName());
        boughtBook.setBook(wantedBook);
        boughtBook.setNumOfCopies(wantedCopies);
        boughtBook.setPrice(wantedCopies * wantedBook.getPrice());

        ArrayList<BoughtBook> boughtBooks = (ArrayList<BoughtBook>) session.getAttribute(SHOPPING_CART_KEY);
        boughtBooks.add(boughtBook);

        int finalPrice = 0;
        for (BoughtBook bB : boughtBooks) {
            finalPrice += bB.getPrice();
        }

        ModelAndView result = new ModelAndView("cartPage");
        result.addObject("cartBooks", boughtBooks);
        result.addObject("user", loggedUser);
        result.addObject("finalPrice", finalPrice);
        return result;
    }

    @PostMapping (value = "/IzbaciIzKorpe")
    public void removeFromCart(@RequestParam String id,
                               HttpServletResponse response, HttpSession session) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return;
        }

        ArrayList<BoughtBook> boughtBooks = (ArrayList<BoughtBook>) session.getAttribute(SHOPPING_CART_KEY);

        boughtBooks.removeIf(removedBook -> removedBook.getId().equals(id));

        response.sendRedirect(bURL + "Knjige/Korpa");
    }

    @GetMapping(value = "/TabelaKnjiga")
    public ModelAndView bookTable(HttpServletResponse response, HttpSession session) throws IOException {

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

    @GetMapping(value = "/IstorijaKupovine")
    public ModelAndView history(HttpServletResponse response, HttpSession session) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        List<Receipt> receipts = receiptService.findByUser(loggedUser.getUserName());

        for (Receipt r : receipts){
            System.out.println(r);
        }
        ModelAndView result = new ModelAndView("receiptPage");
        result.addObject("user", loggedUser);
        result.addObject("receipts", receipts);
        return result;
    }

    @PostMapping(value = "/Kupi")
    public void buy(HttpServletResponse response, HttpSession session) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return;
        }

        String receiptID = randomAlphanumeric(9);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateOfRegistration = dtf.format(now);

        ArrayList<BoughtBook> boughtBooks = (ArrayList<BoughtBook>) session.getAttribute(SHOPPING_CART_KEY);
        Receipt receipt = new Receipt();

        float finalPrice = 0;
        int numOfCopies = 0;

        receipt.setBoughtBooks(boughtBooks);

        for (BoughtBook bB : boughtBooks) {
            bB.getBook().setRemaining(bB.getBook().getRemaining() - bB.getNumOfCopies());
            bB.setReceiptID(receiptID);
            bookService.update(bB.getBook());
            boughtBookService.save(bB);

            numOfCopies += bB.getNumOfCopies();
            finalPrice += bB.getPrice();
        }
        boughtBooks.clear();


        receipt.setId(receiptID);
        receipt.setClient(loggedUser);
        receipt.setDateOfPurchase(dateOfRegistration);
        receipt.setNumberOfBooksBought(numOfCopies);
        receipt.setFinalPrice(finalPrice);


        receiptService.save(receipt);

        response.sendRedirect(bURL + "Knjige/IstorijaKupovine");
    }

}