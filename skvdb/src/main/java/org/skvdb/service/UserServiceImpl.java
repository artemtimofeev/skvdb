package org.skvdb.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.common.service.UserService;
import org.skvdb.common.exception.UserAlreadyExistsException;
import org.skvdb.common.exception.UserNotFoundException;
import org.skvdb.common.security.Authority;
import org.skvdb.common.security.AuthorityType;
import org.skvdb.common.storage.Table;
import org.skvdb.service.dao.UserDao;
import org.skvdb.common.dto.User;
import org.skvdb.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger();

    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>();
    }

    @Override
    public void createUser(String username, String password, boolean isSuperuser) throws UserAlreadyExistsException {
        userDao.createUser(new User(
                username,
                HashUtil.getEncodedHash(password),
                isSuperuser,
                false,
                new HashSet<>()
                ));
    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void grantAuthority(String username, Authority authority) throws UserNotFoundException {
        User user = userDao.getUser(username);
        user.authorities().add(authority);
    }

    @Override
    public void revokeAuthority(String username, Authority authority) throws UserNotFoundException {
        User user = userDao.getUser(username);
        user.authorities().remove(authority);
    }

    @Override
    public void grantSuperuserAuthority(String username) throws UserNotFoundException {
        User user = userDao.getUser(username);
        userDao.updateUser(new User(user.username(), user.passwordHash(), true, user.isProtected(), user.authorities()));
    }

    @Override
    public void revokeSuperuserAuthority(String username) throws UserNotFoundException {
        User user = userDao.getUser(username);
        userDao.updateUser(new User(user.username(), user.passwordHash(), false, user.isProtected(), user.authorities()));
    }

    @Override
    public boolean isSuperuser(String username) throws UserNotFoundException {
        return userDao.getUser(username).isSuperuser();
    }

    @Override
    public boolean hasAuthority(String username, Authority authority) throws UserNotFoundException {
        User user = userDao.getUser(username);
        return user.authorities().contains(authority);
    }

    @Override
    public boolean hasAnyAuthority(String username, String tableName) throws UserNotFoundException {
        User user = userDao.getUser(username);
        return user.authorities().contains(new Authority(AuthorityType.READ, tableName))
                || user.authorities().contains(new Authority(AuthorityType.WRITE, tableName))
                || user.authorities().contains(new Authority(AuthorityType.OWNER, tableName));
    }

    public List<Table<?>> getAllTables(String username) throws UserNotFoundException {
        return null;
    }

}
