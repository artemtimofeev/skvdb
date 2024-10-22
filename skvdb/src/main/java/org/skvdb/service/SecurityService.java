package org.skvdb.service;

import org.skvdb.common.exception.UserNotFoundException;
import org.skvdb.service.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    @Autowired
    private UserDao userDao;

    public String getPasswordHash(String username) throws UserNotFoundException {
        return userDao.getUser(username).passwordHash();
    }
}
