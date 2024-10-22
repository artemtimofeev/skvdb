package org.skvdb.common.service;

import org.skvdb.common.dto.User;
import org.skvdb.common.exception.UserAlreadyExistsException;
import org.skvdb.common.exception.UserNotFoundException;
import org.skvdb.common.security.Authority;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    /**
     *
     * @param username
     * @param password
     * @param isSuperuser
     * @throws UserAlreadyExistsException
     */
    void createUser(String username, String password, boolean isSuperuser) throws UserAlreadyExistsException;

    /**
     *
     * @param username
     * @throws UserNotFoundException
     */
    void deleteUser(String username) throws UserNotFoundException;

    /**
     *
     * @param username
     * @param authority
     * @throws UserNotFoundException
     */
    void grantAuthority(String username, Authority authority) throws UserNotFoundException;

    /**
     *
     * @param username
     * @param authority
     * @throws UserNotFoundException
     */
    void revokeAuthority(String username, Authority authority) throws UserNotFoundException;

    /**
     *
     * @param username
     * @throws UserNotFoundException
     */
    void grantSuperuserAuthority(String username) throws UserNotFoundException;

    /**
     *
     * @param username
     * @throws UserNotFoundException
     */
    void revokeSuperuserAuthority(String username) throws UserNotFoundException;

    /**
     *
     * @param username
     * @return
     * @throws UserNotFoundException
     */
    boolean isSuperuser(String username) throws UserNotFoundException;

    /**
     *
     * @param username
     * @param authority
     * @return
     * @throws UserNotFoundException
     */
    boolean hasAuthority(String username, Authority authority) throws UserNotFoundException;

    /**
     *
     * @param username
     * @param tableName
     * @return
     * @throws UserNotFoundException
     */
    boolean hasAnyAuthority(String username, String tableName) throws UserNotFoundException;
}
