package com.Projekat.Knjizara.controller;

import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.Comment;
import com.Projekat.Knjizara.models.User;
import com.Projekat.Knjizara.service.BookService;
import com.Projekat.Knjizara.service.CommentService;
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
import java.util.List;

@Controller
@RequestMapping(value = "/Komentari")
public class CommentController {
    @Autowired
    private BookService bookService;

    @Autowired
    private CommentService commentService;

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

        List<Comment> comments = commentService.findWaitingComments();
        
        ModelAndView result = new ModelAndView("commentPage");
        result.addObject("comments", comments);
        result.addObject("user", user);
        return result;
    }

    @PostMapping(value = "/Odobri")
    public void approveComment(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) throws IOException {

        User user = (User) request.getSession().getAttribute(UserController.USER_KEY);

        if (user == null) {
            response.sendRedirect(bURL);
            return;
        }

        Comment comment = commentService.findComment(id);
        commentService.approveComment(id);

        Book book = bookService.findOne(comment.getBook().getIsbn());
        System.out.println("Stara ocena knjige : " + book.getRating());
        book.setRating(commentService.getAvgRating(book.getIsbn()));
        System.out.println("Nova ocena knjige : " + book.getRating());

        bookService.update(book);

        response.sendRedirect(bURL + "Komentari/");
    }

    @PostMapping(value = "/Odbij")
    public void disapproveComment(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) throws IOException {

        User user = (User) request.getSession().getAttribute(UserController.USER_KEY);

        if (user == null) {
            response.sendRedirect(bURL);
            return;
        }
        commentService.dissapproveComment(id);
        response.sendRedirect(bURL + "Komentari/");
    }
}
