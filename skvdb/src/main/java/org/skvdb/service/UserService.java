package org.skvdb.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.exception.UserAlreadyExistsException;
import org.skvdb.exception.UserNotFoundException;
import org.skvdb.security.Authority;
import org.skvdb.service.dao.UserDao;
import org.skvdb.service.dto.User;
import org.skvdb.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger();

    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getPasswordHash(String username) throws UserNotFoundException {
        return userDao.getUser(username).passwordHash();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>();
    }

    public void createUser(String username, String password, boolean isSuperuser) throws UserAlreadyExistsException {
        userDao.createUser(new User(
                username,
                HashUtil.getEncodedHash(password),
                isSuperuser,
                new HashSet<>()
                ));
    }

    public void deleteUser(String username) {

    }

    public void grantAuthority(String username, Authority authority) throws UserNotFoundException {
        User user = userDao.getUser(username);
        user.authorities().add(authority);
    }

    public void revokeAuthority(String username, Authority authority) throws UserNotFoundException {
        User user = userDao.getUser(username);
        user.authorities().remove(authority);
    }

    public void grantSuperuserAuthority(String username) throws UserNotFoundException {
        User user = userDao.getUser(username);
        userDao.updateUser(new User(user.username(), user.passwordHash(), true, user.authorities()));
    }

    public void revokeSuperuserAuthority(String username) throws UserNotFoundException {
        User user = userDao.getUser(username);
        userDao.updateUser(new User(user.username(), user.passwordHash(), false, user.authorities()));
    }

    public boolean isSuperuser(String username) throws UserNotFoundException {
        return userDao.getUser(username).isSuperuser();
    }

    public boolean hasAuthority(String username, Authority authority) throws UserNotFoundException {
        User user = userDao.getUser(username);
        return user.authorities().contains(authority);
    }

}
