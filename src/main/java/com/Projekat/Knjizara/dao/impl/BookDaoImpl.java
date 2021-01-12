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
            String language = rs.getString(index++);;
            float price = rs.getFloat(index++);
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
            book.setPrice(price);
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
    public List<Book> find(String name, float minPrice, float maxPrice, String author, String language) {// TODO: dodaj pretragu po zanru
        ArrayList<Object> argumentList = new ArrayList<Object>();

        String sql = "SELECT * FROM books b WHERE ";

        boolean argumentExist = false;

        if(!name.equals("")) {
            name = "%" + name + "%";
            sql += "b.name LIKE ?";
            argumentExist = true;
            argumentList.add(name);
        }
        if(!author.equals("")) {
            author = "%" + author + "%";
            if(argumentExist)
                sql +=" AND ";
            sql += "b.author LIKE ?";
            argumentExist = true;
            argumentList.add(author);
        }
        if(!language.equals("")) {
            language = "%" + language + "%";
            if(argumentExist)
                sql +=" AND ";
            sql += "b.language LIKE ?";
            argumentExist = true;
            argumentList.add(language);
        }
        if(minPrice != 0) {
            if(argumentExist)
                sql +=" AND ";
            sql += "b.price >= ?";
            argumentExist = true;
            argumentList.add(minPrice);
        }
        if(maxPrice != 0) {
            if(argumentExist)
                sql +=" AND ";
            sql += "b.price <= ?";
            argumentList.add(maxPrice);
        }

        return jdbcTemplate.query(sql, argumentList.toArray(),new BookRowMapper());
    }

    @Override
    public void save(Book book) {
        String sql = "INSERT INTO books (name, isbn, publisher, author, released, description, picture, pages, cover, letter, language, price, remaining, rating, active) " +
                     "  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, book.getName(), book.getIsbn(), book.getPublisher(), book.getAuthors(), book.getYearOfRelease(),
                    book.getDescription(), book.getPicture(), book.getNumOfPages(), book.getTypeOfCover().toString(),
                    book.getLetter().toString(), book.getLanguage(), book.getPrice(), book.getRemaining(), book.getRating(),
                    book.isActive());
    }

    @Override
    public void update(Book book) {
        String sql = "UPDATE books SET name = ?, isbn = ?, publisher = ?, author = ?, released = ?, description = ?," +
                     "                  picture = ?, pages = ?, cover = ?, letter = ?, language = ?, price = ?," +
                     "                  remaining = ?, rating = ?, active = ?" +
                     "      WHERE isbn like ?";
        jdbcTemplate.update(sql, book.getName(), book.getIsbn(), book.getPublisher(), book.getAuthors(),
                    book.getYearOfRelease(), book.getDescription(), book.getPicture(), book.getNumOfPages(),
                    book.getTypeOfCover().toString(), book.getLetter().toString(), book.getLanguage(), book.getPrice(),
                    book.getRemaining(), book.getRating(), book.isActive(), book.getIsbn());
    }

    @Override
    public void delete(String isbn) {
        String sql = "DELETE FROM books WHERE isbn = ?";
        jdbcTemplate.update(sql, isbn);
    }

}
