package com.Projekat.Knjizara.dao.impl;

import com.Projekat.Knjizara.dao.BookDao;
import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.enums.ECover;
import com.Projekat.Knjizara.models.enums.ELetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookDaoImpl implements BookDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int i) throws SQLException {
            int index = 1;
            String name = rs.getString(index++);
            String isbn = rs.getString(index++);
            String publisher = rs.getString(index++);
            String authors = rs.getString(index++);
            Date released = rs.getDate(index++);
            String description = rs.getString(index++);
            String picture = rs.getString(index++);
            int pages = rs.getInt(index++);
            String cover = rs.getString(index++);
            String letter = rs.getString(index++);
            String language = rs.getString(index++);
            int remaining = rs.getInt(index++);
            float rating = rs.getInt(index++);
            boolean active = rs.getBoolean(index++);

            Book book = new Book();

            book.setName(name);
            book.setIsbn(isbn);
            book.setPublisher(publisher);
            book.setAuthors(authors);
            book.setYearOfRelease(released);
            book.setDescription(description);
            book.setPicture(picture);
            book.setNumOfPages(pages);
            book.setTypeOfCover(ECover.valueOf(cover));
            book.setLetter(ELetter.valueOf(letter));
            book.setLanguage(language);
            book.setRating(rating);
            book.setRemaining(remaining);
            book.setActive(active);

            return book;
        }
    }

    @Override
    public Book findOne(String isbn) {
        try {
            String sql = "SELECT * FROM books WHERE isbn = ?";
            return jdbcTemplate.queryForObject(sql, new BookRowMapper(), isbn);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        return jdbcTemplate.query(sql, new BookRowMapper());
    }

    @Override
    public List<Book> find(String name, String author, String language) {// TODO: dodaj pretragu po zanru
        ArrayList<Object> argumentList = new ArrayList<Object>();

        String sql = "SELECT * FROM books";

        StringBuffer whereSql = new StringBuffer(" WHERE ");
        boolean argumentExist = false;

        if(name != null) {
            name = "%" + name + "%";
            if(argumentExist)                whereSql.append(" AND ");
            whereSql.append("name LIKE ?");
            argumentExist = true;
            argumentList.add(name);
        }

        if(author != null) {
            author = "%" + author + "%";
            if(argumentExist)
                whereSql.append(" AND ");
            whereSql.append("author LIKE ?");
            argumentExist = true;
            argumentList.add(author);
        }

        if(language != null) {
            language = "%" + language + "%";
            if(argumentExist)
                whereSql.append(" AND ");
            whereSql.append("language LIKE ?");
            argumentList.add(language);
        }


        if(argumentExist)
            sql += whereSql.toString()+" ORDER BY name";
        else
            sql += " ORDER BY name";
        System.out.println(sql);

        return jdbcTemplate.query(sql, argumentList.toArray(), new BookRowMapper());
    }

    @Override
    public void save(Book book) {
        String sql = "INSERT INTO books (name, isbn, publisher, authors, released, description, picture, pages, cover, letter, language, remaining, rating, active) " +
                     "  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, book.getName(), book.getIsbn(), book.getPublisher(), book.getAuthors(),
                                    book.getYearOfRelease(), book.getDescription(), book.getPicture(),
                                        book.getNumOfPages(), book.getTypeOfCover(), book.getLetter(),
                                            book.getLanguage(), book.getRemaining(), book.getRating(), book.isActive());
    }

    @Override
    public void update(Book book) {
            String sql = "UPDATE books SET name = ?, isbn = ?, publisher = ?, authors = ?, released = ?, description = ?," +
                         "                  picture = ?, pages = ?, cover = ?, letter = ?, language = ?, remaining = ?," +
                         "                  rating = ?, active = ?";
        jdbcTemplate.update(sql, book.getName(), book.getIsbn(), book.getPublisher(), book.getAuthors(),
                book.getYearOfRelease(), book.getDescription(), book.getPicture(),
                book.getNumOfPages(), book.getTypeOfCover(), book.getLetter(),
                book.getLanguage(), book.getRemaining(), book.getRating(), book.isActive());
    }

    @Override
    public void delete(String isbn) {
        String sql = "DELETE FROM books WHERE isbn = ?";
        jdbcTemplate.update(sql, isbn);
    }

}
