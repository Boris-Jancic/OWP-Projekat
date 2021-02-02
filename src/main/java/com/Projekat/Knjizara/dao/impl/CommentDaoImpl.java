package com.Projekat.Knjizara.dao.impl;

import com.Projekat.Knjizara.dao.CommentDao;
import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.BoughtBook;
import com.Projekat.Knjizara.models.Comment;
import com.Projekat.Knjizara.models.User;
import com.Projekat.Knjizara.models.enums.EStatus;
import com.Projekat.Knjizara.service.BookService;
import com.Projekat.Knjizara.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public class CommentDaoImpl implements CommentDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    private class CommentRowMapper implements RowMapper<Comment> {

        @Override
        public Comment mapRow(ResultSet rs, int i) throws SQLException {
            int index = 1;

            String id = rs.getString(index++);
            User author = userService.findOne(rs.getString(index++));
            String text = rs.getString(index++);
            Book book = bookService.findOne(rs.getString(index++));
            float grade = rs.getFloat(index++);
            String dateOfComment = rs.getString(index++);
            String status = rs.getString(index++);

            Comment comment = new Comment();

            comment.setId(id);
            comment.setAuthor(author);
            comment.setText(text);
            comment.setBook(book);
            comment.setRating(grade);
            comment.setDateOfComment(dateOfComment);
            comment.setStatus(EStatus.valueOf(status));

            return comment;
        }
    }

    @Override
    public Comment findComment(String id) {
        try {
            String sql = "SELECT * FROM comments WHERE id like ?";
            return jdbcTemplate.queryForObject(sql, new CommentRowMapper(), id);
        } catch (
        EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Comment> findWaitingComments() {
        String sql = "SELECT * FROM comments WHERE status like 'WAITING'";
        return jdbcTemplate.query(sql, new CommentRowMapper());
    }
    @Override
    public List<Comment> findBookComments(String isbn) {
        String sql = "SELECT * FROM comments WHERE book like ? AND status like 'APPROVED'";
        return jdbcTemplate.query(sql, new CommentRowMapper(), isbn);
    }

    @Override
    public void saveComment(Comment comment) {
        String sql = "INSERT INTO comments (id, author, text, book, grade, dateOfComment, status) VALUES(?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, comment.getId(), comment.getAuthor().getUserName(), comment.getText(),
                comment.getBook().getIsbn(), comment.getRating(), comment.getDateOfComment(), comment.getStatus().toString());
    }

    @Override
    public void approveComment(String id) {
        String sql = "UPDATE comments SET status = 'APPROVED' WHERE id LIKE ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void dissapproveComment(String id) {
        String sql = "UPDATE comments SET status = 'NOTAPPROVED' WHERE id LIKE ?";
        jdbcTemplate.update(sql, id);
    }
}
