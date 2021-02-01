package com.Projekat.Knjizara.controller;

import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.BoughtBook;
import com.Projekat.Knjizara.models.Receipt;
import com.Projekat.Knjizara.models.User;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.thymeleaf.util.StringUtils.randomAlphanumeric;

@Controller
@RequestMapping(value = "/Kupovina")
public class TransactionController {

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


    @GetMapping(value = "/Korpa")
    public ModelAndView viewCart(HttpServletResponse response) throws IOException {
        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        if (session.getAttribute(SHOPPING_CART_KEY) == null) {
            ArrayList<BoughtBook> createBoughtBooks = new ArrayList<>();
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
                                  HttpServletResponse response) throws IOException {

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

    @PostMapping(value = "/IzbaciIzKorpe")
    public void removeFromCart(@RequestParam String id,
                               HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return;
        }

        ArrayList<BoughtBook> boughtBooks = (ArrayList<BoughtBook>) session.getAttribute(SHOPPING_CART_KEY);

        boughtBooks.removeIf(removedBook -> removedBook.getId().equals(id));

        response.sendRedirect(bURL + "Kupovina/Korpa");
    }

    @GetMapping(value = "/IstorijaKupovine")
    public ModelAndView history(HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        List<Receipt> receipts = receiptService.findByUser(loggedUser.getUserName());

        ModelAndView result = new ModelAndView("receiptPage");
        result.addObject("user", loggedUser);
        result.addObject("receipts", receipts);
        return result;
    }

    @PostMapping(value = "/Kupi")
    public void buy(HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return;
        }

        String receiptID = randomAlphanumeric(9);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateOfPurchase = dtf.format(now);

        ArrayList<BoughtBook> boughtBooks = (ArrayList<BoughtBook>) session.getAttribute(SHOPPING_CART_KEY);

        float finalPrice = 0;
        int numOfCopies = 0;

        for (BoughtBook bB : boughtBooks) {
            int remainingBooks = bB.getBook().getRemaining() - bB.getNumOfCopies();

            if (remainingBooks < 0) {
                bB.getBook().setRemaining(0);
                bB.setNumOfCopies(bB.getBook().getRemaining());
            } else {
                bB.getBook().setRemaining(remainingBooks);
            }

            bB.setReceiptID(receiptID);
            bookService.update(bB.getBook());
            boughtBookService.save(bB);

            numOfCopies += bB.getNumOfCopies();
            finalPrice += bB.getPrice();
        }

        Receipt receipt = new Receipt();
        receipt.setId(receiptID);
        receipt.setBoughtBooks(boughtBooks);
        receipt.setClient(loggedUser);
        receipt.setDateOfPurchase(dateOfPurchase);
        receipt.setNumberOfBooksBought(numOfCopies);
        receipt.setFinalPrice(finalPrice);

        boughtBooks.clear();

        receiptService.save(receipt);

        response.sendRedirect(bURL + "Kupovina/IstorijaKupovine");
    }

    @GetMapping(value = "/DetaljiKupovine")
    public ModelAndView history(String receiptID, HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);

        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        List<BoughtBook> boughtBooks = boughtBookService.findBoughtBooksOnReceipt(receiptID);

        int finalPrice = 0;
        for (BoughtBook bB : boughtBooks) {
            finalPrice += bB.getPrice();
        }

        ModelAndView result = new ModelAndView("specificReceipt");
        result.addObject("user", loggedUser);
        result.addObject("receiptBooks", boughtBooks);
        result.addObject("finalPrice", finalPrice);
        return result;
    }
}
