package com.Projekat.Knjizara.dao.impl;

import com.Projekat.Knjizara.dao.BookDao;
import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.Discount;
import com.Projekat.Knjizara.models.User;
import com.Projekat.Knjizara.models.WishListItem;
import com.Projekat.Knjizara.models.enums.ECover;
import com.Projekat.Knjizara.models.enums.ELetter;
import com.Projekat.Knjizara.service.BookService;
import com.Projekat.Knjizara.service.UserService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.thymeleaf.util.StringUtils.randomAlphanumeric;

@Repository
public class BookDaoImpl implements BookDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int i) throws SQLException {
            int index = 1;
            String name = rs.getString(index++);
            String isbn = rs.getString(index++);
            String publisher = rs.getString(index++);
            String author = rs.getString(index++);
            Date released = rs.getDate(index++);
            String description = rs.getString(index++);
            String picture = rs.getString(index++);
            int pages = rs.getInt(index++);
            String cover = rs.getString(index++);
            String letter = rs.getString(index++);
            String language = rs.getString(index++);
            float price = rs.getFloat(index++);
            int remaining = rs.getInt(index++);
            float rating = rs.getInt(index++);
            boolean active = rs.getBoolean(index++);

            Book book = new Book();

            book.setName(name);
            book.setIsbn(isbn);
            book.setPublisher(publisher);
            book.setAuthor(author);
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
    private class DiscountRowMapper implements RowMapper<Discount> {

        @Override
        public Discount mapRow(ResultSet rs, int i) throws SQLException {
            int index = 1;
            String id = rs.getString(index++);
            int discount = rs.getInt(index++);
            Date start = rs.getDate(index++);
            Date end = rs.getDate(index++);
            String isbn = "";
            if (rs.next())
                isbn = rs.getString(index++);

            Discount rowDiscount = new Discount();
            rowDiscount.setId(id);
            rowDiscount.setDiscount(discount);
            rowDiscount.setStart(start);
            rowDiscount.setEnd(end);
            rowDiscount.setBook(isbn);

            return rowDiscount;
        }
    }

    private class WishListRowMapper implements RowMapper<WishListItem> {

        @Override
        public WishListItem mapRow(ResultSet rs, int i) throws SQLException {
            int index = 1;
            User user = userService.findOne(rs.getString(index++));
            Book book = bookService.findOne(rs.getString(index++));

            WishListItem wishListItem = new WishListItem();

            wishListItem.setUser(user);
            wishListItem.setBook(book);

            return wishListItem;
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
    public List<Book> find(String isbn, String name, float minPrice, float maxPrice, String author, String language) {// TODO: dodaj pretragu po zanru
        val map = new MapSqlParameterSource();

        //language=MySQL
        String sql = "SELECT * FROM books b WHERE ";

        boolean argumentExist = false;

        if(!isbn.equals("")) {
            isbn = "%" + isbn + "%";
            sql += "b.isbn LIKE :isbn";
            argumentExist = true;
            map.addValue("isbn", isbn);
        }
        if(!name.equals("")) {
            name = "%" + name + "%";
            sql += "b.name LIKE :name";
            if(argumentExist)
                sql +=" AND ";
            argumentExist = true;
            map.addValue("name", name);
        }
        if(!author.equals("")) {
            author = "%" + author + "%";
            if(argumentExist)
                sql +=" AND ";
            sql += "b.author LIKE :author";
            argumentExist = true;
            map.addValue("author", author);
        }
        if(!language.equals("")) {
            language = "%" + language + "%";
            if(argumentExist)
                sql +=" AND ";
            sql += "b.language LIKE :language";
            argumentExist = true;
            map.addValue("language", language);
        }
        if(minPrice != 0) {
            if(argumentExist)
                sql +=" AND ";
            sql += "b.price >= :minprice";
            argumentExist = true;
            map.addValue("minprice", minPrice);
        }
        if(maxPrice != 0) {
            if(argumentExist)
                sql +=" AND ";
            sql += "b.price <= :maxprice";
            map.addValue("maxprice", maxPrice);
        }

        return namedParameterJdbcTemplate.query(sql, map, new BookRowMapper());
    }

    @Override
    public void save(Book book) {
        String sql = "INSERT INTO books (name, isbn, publisher, author, released, description, picture, pages, cover, letter, language, price, remaining, rating, active) " +
                     "  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, book.getName(), book.getIsbn(), book.getPublisher(), book.getAuthor(), book.getYearOfRelease(),
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
        jdbcTemplate.update(sql, book.getName(), book.getIsbn(), book.getPublisher(), book.getAuthor(),
                    book.getYearOfRelease(), book.getDescription(), book.getPicture(), book.getNumOfPages(),
                    book.getTypeOfCover().toString(), book.getLetter().toString(), book.getLanguage(), book.getPrice(),
                    book.getRemaining(), book.getRating(), book.isActive(), book.getIsbn());
    }

    @Override
    public void delete(String isbn) {
        String sql = "DELETE FROM books WHERE isbn = ?";
        jdbcTemplate.update(sql, isbn);
    }

    @Override
    public List<WishListItem> userWishList(String username) {
        String sql = "SELECT * FROM wishLists WHERE username = ?";
        return jdbcTemplate.query(sql, new WishListRowMapper(), username);
    }

    @Override
    public void addToWishList(WishListItem wishListItem) {
        String sql = "INSERT INTO wishLists (username, isbn) VALUES (?, ?)";
        jdbcTemplate.update(sql, wishListItem.getUser().getUserName(), wishListItem.getBook().getIsbn());
    }

    @Override
    public void removeFromWishList(WishListItem wishListItem) {
        String sql = "DELETE FROM wishLists WHERE username = ? AND isbn = ?";
        jdbcTemplate.update(sql, wishListItem.getUser().getUserName(), wishListItem.getBook().getIsbn());
    }

    @Override
    public void addDiscount(Discount discount) {
        String sql = "INSERT INTO discounts (id, discount, startDisc, endDisc, isbn) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, discount.getId(), discount.getDiscount(), discount.getStart(), discount.getEnd(),discount.getBook());
    }

    @Override
    public Discount checkIfDiscountAll() {
        String sql = "SELECT * FROM discounts WHERE CURDATE() between startDisc and endDisc and isbn like '0000000000000'";
        return jdbcTemplate.queryForObject(sql, new DiscountRowMapper());
    }

    @Override
    public Discount checkIfDiscountSpecific(String isbn) {
        String sql = "SELECT * FROM discounts WHERE CURDATE() between startDisc and endDisc and isbn like ?";
        return jdbcTemplate.queryForObject(sql, new DiscountRowMapper());
    }

}
