package org.skvdb.storage.api;

import java.util.List;
import java.util.Set;

public interface User {
    void createUser(String username, String password, boolean isSuperuser);

    void deleteUser(String username);

    void changePassword(String username, String newPassword);

    void grantAuthority(String username, Authority authority);

    void revokeAuthority(String username, Authority authority);

    List<String> getAllUsers();

    Set<Authority> getAuthorities(String username);


}
