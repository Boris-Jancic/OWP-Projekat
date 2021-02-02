package com.Projekat.Knjizara.dao;

import com.Projekat.Knjizara.models.BoughtBook;
import com.Projekat.Knjizara.models.Comment;

import java.util.List;

public interface CommentDao {
    public Comment findComment(String id);

    public List<Comment> findWaitingComments();

    public List<Comment> findBookComments(String isbn);

    public void saveComment(Comment comment);

    public void approveComment(String id);

    public void dissapproveComment(String id);
}
