package com.Projekat.Knjizara.dao.impl;

import com.Projekat.Knjizara.dao.ReceiptDao;
import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.BoughtBook;
import com.Projekat.Knjizara.models.Receipt;
import com.Projekat.Knjizara.models.User;
import com.Projekat.Knjizara.service.BoughtBookService;
import com.Projekat.Knjizara.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ReceiptDaoImpl implements ReceiptDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BoughtBookService boughtBookService;

    @Autowired
    private UserService userService;


    private class ReceiptRowMapper implements RowMapper<Receipt> {

        @Override
        public Receipt mapRow(ResultSet rs, int i) throws SQLException {
            int index = 1;

            String id = rs.getString(index++);
            String dateOfPurchase = rs.getString(index++);
            User client = userService.findOne(rs.getString(index++));
            List<BoughtBook> boughtBooks = boughtBookService.findBoughtBooksOnReceipt(id);
            int numOfCopies = rs.getInt(index++);
            float price = rs.getFloat(index++);

            Receipt receipt = new Receipt();

            receipt.setId(id);
            receipt.setDateOfPurchase(dateOfPurchase);
            receipt.setClient(client);
            receipt.setBoughtBooks(boughtBooks);
            receipt.setNumberOfBooksBought(numOfCopies);
            receipt.setFinalPrice(price);

            return receipt;
        }
    }


    @Override
    public Receipt findOne(String id) {
        try {
            String sql = "SELECT * FROM receipts WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, new ReceiptRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Receipt> findByUser(String username) {
        String sql = "SELECT * FROM receipts WHERE name = ?";
        return jdbcTemplate.query(sql, new ReceiptRowMapper(), username);
    }

    @Override
    public List<Receipt> findAll() {
        String sql = "SELECT * FROM receipts";
        return jdbcTemplate.query(sql, new ReceiptRowMapper());
    }

    @Override
    public void save(Receipt receipt) {
        String sql = "INSERT INTO receipts (id, dateOfPurchase, name, numOfBooks, price) " +
                "  VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, receipt.getId(), receipt.getDateOfPurchase(), receipt.getClient().getUserName(),
                receipt.getNumberOfBooksBought(), receipt.getFinalPrice());
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM receipts WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
