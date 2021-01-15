package com.Projekat.Knjizara.dao.impl;

import com.Projekat.Knjizara.dao.BoughtBookDao;
import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.BoughtBook;
import com.Projekat.Knjizara.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BoughtBookDaoImpl implements BoughtBookDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BookService bookService;

    private class BoughtBookRowMapper implements RowMapper<BoughtBook> {

        @Override
        public BoughtBook mapRow(ResultSet rs, int i) throws SQLException {
            int index = 1;

            String id = rs.getString(index++);
            String username = rs.getString(index++);
            Book book = bookService.findOne(rs.getString(index++));
            int numOfCopies = rs.getInt(index++);
            float price = rs.getFloat(index++);
            String receiptID = rs.getString(index++);

            BoughtBook boughtBook = new BoughtBook();

            boughtBook.setId(id);
            boughtBook.setUsername(username);
            boughtBook.setBook(book);
            boughtBook.setNumOfCopies(numOfCopies);
            boughtBook.setPrice(price);
            boughtBook.setId(receiptID);

            return boughtBook;
        }
    }

    @Override
    public BoughtBook findOne(String id) {
        try {
            String sql = "SELECT * FROM boughtBooks WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, new BoughtBookRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<BoughtBook> findBoughtBooksOnReceipt(String id) {
        String sql = "SELECT * FROM boughtbooks " +
                        "WHERE idReceipt = ?" +
                        "GROUP BY id, username, book, numOfCopies, price, idReceipt";
        return jdbcTemplate.query(sql, new BoughtBookRowMapper(), id);
    }

    @Override
    public List<BoughtBook> findAll() {
        String sql = "SELECT * FROM boughtBooks";
        return jdbcTemplate.query(sql, new BoughtBookRowMapper());
    }

    @Override
    public void save(BoughtBook boughtBook) {
        String sql = "INSERT INTO boughtBooks (id, username, book, numOfCopies, price, idReceipt) " +
                "  VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, boughtBook.getId(), boughtBook.getUsername(), boughtBook.getBook().getIsbn(),
                boughtBook.getNumOfCopies(), boughtBook.getPrice(), boughtBook.getReceiptID());
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM boughtBooks WHERE id = ?";
        jdbcTemplate.update(sql, id);

    }

}
