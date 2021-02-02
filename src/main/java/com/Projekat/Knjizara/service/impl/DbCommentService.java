package com.Projekat.Knjizara.service.impl;

import com.Projekat.Knjizara.dao.CommentDao;
import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.Comment;
import com.Projekat.Knjizara.service.BookService;
import com.Projekat.Knjizara.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbCommentService implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private BookService bookService;

    @Override
    public Comment findComment(String id) { return commentDao.findComment(id); }

    @Override
    public List<Comment> findWaitingComments() { return commentDao.findWaitingComments(); }

    @Override
    public List<Comment> findBookComments(String isbn) { return commentDao.findBookComments(isbn); }

    @Override
    public void saveComment(Comment comment) { commentDao.saveComment(comment); }

    @Override
    public void approveComment(String id) { commentDao.approveComment(id); }

    @Override
    public void dissapproveComment(String id) { commentDao.dissapproveComment(id); }

    @Override
    public float getAvgRating(String isbn) {
        List<Comment> comments = findBookComments(isbn);
        int i = 0;
        float sum = 0;

        for (Comment c : comments){
            sum += c.getRating();
            i++;
        }

        return  sum / (float) i;
    }
}
