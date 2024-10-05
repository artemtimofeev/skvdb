package org.skvdb.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.exception.TableAlreadyExistsException;
import org.skvdb.exception.TableNotFoundException;
import org.skvdb.exception.UserDoesNotExistsException;
import org.skvdb.storage.api.Storage;
import org.skvdb.storage.api.Table;
import org.skvdb.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    private Table<Map<String, String>> authenticationData;

    private static final Logger logger = LogManager.getLogger();

    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "password";

    @Autowired
    public AuthenticationService(Storage storage) {
        authenticationData = getAuthenticationTable(storage);
    }

    private Table<Map<String, String>> getAuthenticationTable(Storage storage) {
        try {
            return storage.findTableMapByName(AuthenticationDataTable.TABLE_NAME);
        } catch (TableNotFoundException e) {
            return createAuthenticationTable(storage);
        }
    }

    private Table<Map<String, String>> createAuthenticationTable(Storage storage) {
        try {
            Table<Map<String, String>> table = storage.createMapTable(AuthenticationDataTable.TABLE_NAME);
            setDefaultAdminCredentials(table);
            return table;
        } catch (TableAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDefaultAdminCredentials(Table<Map<String, String>> authenticationData) {
        authenticationData.set(DEFAULT_ADMIN_USERNAME, new HashMap<>());
        Map<String, String> mp = authenticationData.get(DEFAULT_ADMIN_USERNAME).value();
        mp.put(AuthenticationDataTable.PASSWORD_HASH, HashUtil.getEncodedHash(DEFAULT_ADMIN_PASSWORD));
    }

    public String getPasswordHash(String username) throws UserDoesNotExistsException {
        if (authenticationData.containsKey(username)) {
            return authenticationData.get(username).value().get(AuthenticationDataTable.PASSWORD_HASH);
        }
        throw new UserDoesNotExistsException();
    }

}
