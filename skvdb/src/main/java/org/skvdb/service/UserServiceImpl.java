package org.skvdb.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.common.service.UserService;
import org.skvdb.common.exception.UserAlreadyExistsException;
import org.skvdb.common.exception.UserNotFoundException;
import org.skvdb.common.security.Authority;
import org.skvdb.common.security.AuthorityType;
import org.skvdb.service.dao.TableDao;
import org.skvdb.service.dao.UserDao;
import org.skvdb.common.dto.User;
import org.skvdb.storage.v2.BaseTable;
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

    private TableDao tableDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, TableDao tableDao) {
        this.userDao = userDao;
        this.tableDao = tableDao;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getUsers();
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
    public void deleteUser(String username) throws UserNotFoundException {
        userDao.deleteUser(username);
    }

    @Override
    public void grantAuthority(String username, Authority authority) throws UserNotFoundException {
        User user = userDao.getUser(username);
        user.authorities().add(authority);
        userDao.updateUser(user);
    }

    @Override
    public void revokeAuthority(String username, Authority authority) throws UserNotFoundException {
        User user = userDao.getUser(username);
        user.authorities().remove(authority);
        userDao.updateUser(user);
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

    public List<BaseTable> getAllTables(String username) throws UserNotFoundException {
        List<BaseTable> result = new ArrayList<>();
        for (BaseTable table : tableDao.getTables()) {
            if (hasAnyAuthority(username, table.getTableMetaData().getName())) {
                result.add(table);
            }
        }
        return result;
    }

    public User getUser(String username) throws UserNotFoundException {
        return userDao.getUser(username);
    }
}
