package com.Projekat.Knjizara.dao.impl;

import com.Projekat.Knjizara.dao.UserDao;
import com.Projekat.Knjizara.models.Book;
import com.Projekat.Knjizara.models.enums.ECover;
import com.Projekat.Knjizara.models.enums.ELetter;
import com.Projekat.Knjizara.models.User;
import com.Projekat.Knjizara.models.enums.EStatus;
import com.Projekat.Knjizara.models.enums.EType;
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
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            int index = 1;
            String username = rs.getString(index++);
            String password = rs.getString(index++);
            String email = rs.getString(index++);
            String name = rs.getString(index++);
            String lastName = rs.getString(index++);
            Date dateOfBirth = rs.getDate(index++);
            String address = rs.getString(index++);
            String phone = rs.getString(index++);
            String dateOfRegistration = rs.getString(index++);
            String type = rs.getString(index++);
            boolean active = rs.getBoolean(index++);
            String loyaltyCard = rs.getString(index++);
            int points = rs.getInt(index++);

            User user = new User();

            user.setUserName(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setName(name);
            user.setLastName(lastName);
            user.setDateOfBirth(dateOfBirth);
            user.setAddress(address);
            user.setPhone(phone);
            user.setDateOfRegistration(dateOfRegistration);
            user.setUserType(EType.valueOf(type));
            user.setActive(active);
            user.setLoyaltyCard(EStatus.valueOf(loyaltyCard));
            user.setPoints(points);

            return user;
        }
    }

    @Override
    public User findOne(String username) {
        try {
            String sql = "SELECT * FROM users WHERE username = ?";
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public User checkLogin(String username, String password) {
        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), username, password);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public List<User> loyaltyCardReq() {
        String sql = "SELECT * FROM users WHERE status LIKE 'WAITING'";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public void updateLoyaltyCardReq(String request, String username) {
        String sql = "UPDATE users SET status = ? WHERE username like ?";
        jdbcTemplate.update(sql, request, username);
    }

    @Override
    public List<User> find(String username) {

        ArrayList<Object> argumentList = new ArrayList<Object>();

        String sql = "SELECT * FROM users";

        StringBuffer whereSql = new StringBuffer(" WHERE ");

        if(username != null) {
            username = "%" + username + "%";
            whereSql.append(" AND ");
            whereSql.append("username LIKE ?");
            argumentList.add(username);
        }

        return jdbcTemplate.query(sql, argumentList.toArray(), new UserRowMapper());
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (username, password, email, name, lastName, dateOfBirth, address, phone," +
                                        " dateOfRegistration, userType, active, status, points) " +
                "  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserName(), user.getPassword(), user.getEmail(), user.getName(),
                user.getLastName(), user.getDateOfBirth(), user.getAddress(), user.getPhone(), user.getDateOfRegistration(),
                user.getUserType().toString(), user.isActive(), "NOTAPPROVED", 0);
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, name = ?, lastName = ?, dateOfBirth = ?, address = ?, phone = ?," +
                " dateOfRegistration = ?, userType = ?, active = ?, points = ? WHERE username like ?";
        jdbcTemplate.update(sql, user.getUserName(), user.getPassword(), user.getEmail(), user.getName(), user.getLastName(),
                user.getDateOfBirth(), user.getAddress(), user.getPhone(), user.getDateOfRegistration(),
                user.getUserType().toString(), user.isActive(), user.getPoints(), user.getUserName());
    }

    @Override
    public void block(String username, boolean blocked) {
        String sql = "UPDATE users SET active = ? WHERE username like ?";
        jdbcTemplate.update(sql, blocked, username);
    }
}
