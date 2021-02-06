package com.Projekat.Knjizara.controller;

import com.Projekat.Knjizara.models.*;
import com.Projekat.Knjizara.service.BookService;
import com.Projekat.Knjizara.service.BoughtBookService;
import com.Projekat.Knjizara.service.ReceiptService;
import com.Projekat.Knjizara.service.UserService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.thymeleaf.util.StringUtils.randomAlphanumeric;

@Controller
@RequestMapping(value = "/Kupovina")
public class TransactionController {

    public static final String SHOPPING_CART_KEY = "shoppingCart";

    @Autowired
    private UserService userService;

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
        boolean sale = false;

        for (BoughtBook bB : boughtBooks) {
            Discount specificDiscount = bookService.checkIfDiscountSpecific(bB.getBook().getIsbn());
            if (specificDiscount == null) {
                finalPrice += bB.getPrice();
            } else {
                float discount = (float) ((float) specificDiscount.getDiscount() * 0.01);
                float discountBook = bB.getPrice() - (bB.getPrice() * discount);
                finalPrice += discountBook;
                sale = true;
            }
        }

        Discount allDiscount = bookService.checkIfDiscountAll();
        if (allDiscount != null) {
            float discount = (float) ((float) allDiscount.getDiscount() * 0.01);
            finalPrice -= (finalPrice * discount);
            sale = true;
        }

        ModelAndView result = new ModelAndView("cartPage");
        result.addObject("cartBooks", boughtBooks);
        result.addObject("user", loggedUser);
        result.addObject("sale", sale);
        result.addObject("finalPrice", finalPrice);
        return result;
    }

    @PostMapping(value = "/DodajUKorpu")
    public void addToCart(@RequestParam String isbn, int wantedCopies,
                                  HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return;
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

        response.sendRedirect(bURL + "Kupovina/Korpa");
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
    public void buy(int points, float finalPrice, HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateOfPurchase = dtf.format(now);
        String receiptID = randomAlphanumeric(9);

        ArrayList<BoughtBook> boughtBooks = (ArrayList<BoughtBook>) session.getAttribute(SHOPPING_CART_KEY);

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
        }

        if (points > 0) {
            loggedUser.setPoints(loggedUser.getPoints() - points);
            float discount = (float) ((float) points * 0.05);
            finalPrice = (finalPrice - (finalPrice * discount));
        }

        Receipt receipt = new Receipt();
        receipt.setId(receiptID);
        receipt.setBoughtBooks(boughtBooks);
        receipt.setClient(loggedUser);
        receipt.setDateOfPurchase(dateOfPurchase);
        receipt.setNumberOfBooksBought(numOfCopies);
        receipt.setFinalPrice(finalPrice);

        int aquiredPoints = (int) finalPrice / 999;

        if (aquiredPoints + loggedUser.getPoints() > 10) {
            loggedUser.setPoints(10);
        } else if (aquiredPoints > 0) {
            loggedUser.setPoints(loggedUser.getPoints() + aquiredPoints);
        }

        boughtBooks.clear();
        userService.update(loggedUser);
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

    @GetMapping(value = "/ListaZelja")
    public ModelAndView addToWishList(HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        List<WishListItem> wishListBooks = bookService.userWishList(loggedUser.getUserName());

        ModelAndView result = new ModelAndView("wishListPage");
        result.addObject("user", loggedUser);
        result.addObject("wishListBooks", wishListBooks);
        return result;
    }

    @PostMapping(value = "/DodajUListuZelja")
    public void addToWishList(String username, String isbn, HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return;
        }

        WishListItem wishListItem = new WishListItem();

        wishListItem.setUser(userService.findOne(username));
        wishListItem.setBook(bookService.findOne(isbn));
        bookService.addToWishList(wishListItem);

        response.sendRedirect(bURL + "Kupovina/ListaZelja");
    }

    @PostMapping(value = "/IzbaciIzListeZelja")
    public void removeToWishList(String username, String isbn, HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return;
        }

        WishListItem wishListItem = new WishListItem();

        wishListItem.setUser(userService.findOne(username));
        wishListItem.setBook(bookService.findOne(isbn));
        bookService.removeFromWishList(wishListItem);

        response.sendRedirect(bURL + "Kupovina/ListaZelja");
    }


    @PostMapping(value = "/LoyaltyReq")
    public void sendLoyaltyCardReq(HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return;
        }

        userService.updateLoyaltyCardReq("WAITING", loggedUser.getUserName());

        response.sendRedirect(bURL + "Knjige");
    }

    @GetMapping(value = "/DodajAkciju")
    public ModelAndView addAction(String isbn, HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return null;
        }

        ModelAndView result = new ModelAndView("addDiscount");
        result.addObject("isbn", isbn);
        result.addObject("user", loggedUser);
        return result;
    }

    @PostMapping(value = "/ObjaviAkciju")
    public void addAction(@Valid Discount discount, BindingResult bindingResult, HttpServletResponse response) throws IOException {

        User loggedUser = (User) session.getAttribute(UserController.USER_KEY);
        if (loggedUser == null) {
            response.sendRedirect(bURL);
            return;
        }

        discount.setId(randomAlphanumeric(9));
        bookService.addDiscount(discount);

        response.sendRedirect(bURL + "Knjige");
    }
}
