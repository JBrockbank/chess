package dataAccessTests;

import dataAccess.*;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;


import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTests2 {

    AuthDAO authDAO = new SQLAuthDAO();
    UserDAO userDAO = new SQLUserDAO();

    public AuthServiceTests2() throws Exception {
    }

    @BeforeEach
    void clear(){
        authDAO.clear();
        userDAO.clear();
    }

    @Test
    void newTokenTest() throws Exception {
        AuthData authData = authDAO.createAuth("user");
        AuthData authData2 = authDAO.getAuth(authData.authToken());
        assertEquals(authData.authToken(), authData2.authToken());
    }

    @Test
    void newTokenTestFail() throws Exception {
        AuthData authData = authDAO.createAuth("user");
        String token = authData.authToken();
        AuthData authData2 = authDAO.createAuth("user2");
        String token2 = authData2.authToken();
        assertNotEquals(token, token2);
    }

    @Test
    void getAuthDataTest() throws Exception {
        AuthData authData = authDAO.createAuth("user");
        AuthData authData2 = authDAO.getAuth(authData.authToken());
        assertEquals(authData, authData2);
    }

    @Test
    void getAuthDataTestFail() throws Exception {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            authDAO.getAuth("not an auth token");
        });
        assertTrue(exception.getMessage().contains("unauthorized"));
    }

    @Test
    void deleteTest() throws Exception {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            AuthData authData = authDAO.createAuth("user");
            String token = authData.authToken();
            authDAO.deleteAuth(token);
            authDAO.getAuth(token);
        });
        assertTrue(exception.getMessage().contains("unauthorized"));
    }

    @Test
    void deleteTestFail() throws Exception {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            authDAO.deleteAuth("not an auth token");
        });
        assertTrue(exception.getMessage().contains("unauthorized"));
    }

    @Test
    void clearTest() throws Exception {
        assertDoesNotThrow(() -> {
            authDAO.clear();
        });
    }
}
