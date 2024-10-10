package org.skvdb.service.dao;

import org.skvdb.configuration.settings.SecuritySettings;
import org.skvdb.exception.TableAlreadyExistsException;
import org.skvdb.exception.TableNotFoundException;
import org.skvdb.exception.UserAlreadyExistsException;
import org.skvdb.exception.UserNotFoundException;
import org.skvdb.service.dto.User;
import org.skvdb.storage.api.Storage;
import org.skvdb.storage.api.Table;
import org.skvdb.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;


@Component
public class UserDao {
    private Storage storage;

    private Table<User> users;

    public static final String TABLE_NAME = "users";

    @Autowired
    public UserDao(Storage storage, SecuritySettings securitySettings) {
        this.storage = storage;
        try {
            users = getUserTable();
        } catch (TableNotFoundException e) {
            users = createUserTable();
            setDefaultSuperuser(securitySettings);
        }
    }

    private Table<User> getUserTable() throws TableNotFoundException {
        return storage.findTableByName(TABLE_NAME, User.class);
    }

    private Table<User> createUserTable() {
        try {
            return storage.createTable(TABLE_NAME, User.class);
        } catch (TableAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDefaultSuperuser(SecuritySettings securitySettings) {
        User defaultSuperuser = new User(
                securitySettings.defaultAdminUsername(),
                HashUtil.getEncodedHash(securitySettings.defaultAdminPassword()),
                true,
                new HashSet<>()
                );
        users.set(securitySettings.defaultAdminUsername(), defaultSuperuser);
    }

    public User getUser(String username) throws UserNotFoundException {
        if (users.containsKey(username)) {
            return users.get(username);
        }
        throw new UserNotFoundException();
    }

    public void createUser(User user) throws UserAlreadyExistsException {
        if (users.containsKey(user.username())) {
            throw new UserAlreadyExistsException();
        }
        users.set(user.username(), user);
    }

    public void updateUser(User user) throws UserNotFoundException {
        if (users.containsKey(user.username())) {
            users.set(user.username(), user);
        }
        throw new UserNotFoundException();
    }
}
