package org.skvdb.service.dao;

import org.skvdb.common.dto.User;
import org.skvdb.common.exception.TableAlreadyExistsException;
import org.skvdb.common.exception.TableNotFoundException;
import org.skvdb.configuration.settings.SecuritySettings;
import org.skvdb.common.exception.UserAlreadyExistsException;
import org.skvdb.common.exception.UserNotFoundException;
import org.skvdb.storage.v2.BaseTable;
import org.skvdb.storage.v2.StructedStorage;
import org.skvdb.storage.v2.StructedTable;
import org.skvdb.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class UserDao {
    private final StructedStorage storage;

    private StructedTable<User> users;

    public static final String TABLE_NAME = "users";

    @Autowired
    public UserDao(StructedStorage storage, SecuritySettings securitySettings) {
        this.storage = storage;
        try {
            users = getUserTable();
        } catch (TableNotFoundException e) {
            users = createUserTable();
            setDefaultSuperuser(securitySettings);
        }
    }

    private StructedTable<User> getUserTable() throws TableNotFoundException {
        return storage.findTableByName(TABLE_NAME, User.class);
    }

    private StructedTable<User> createUserTable() {
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



    public List<User> getUsers() {
        Iterator<Map.Entry<String, User>> iterator = users.getIterator();
        List<User> userList = new ArrayList<>();
        while (iterator.hasNext()) {
            User user = iterator.next().getValue();
            userList.add(user);
        }
        return userList;
    }

    public void deleteUser(String username) throws UserNotFoundException {
        if (users.containsKey(username) && !users.get(username).isProtected()) {
            users.delete(username);
            return;
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
            return;
        }
        throw new UserNotFoundException();
    }
}
