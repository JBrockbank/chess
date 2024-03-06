package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;
import static service.UserService.*;


import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static service.GameService.*;

public class AuthServiceTests {

    AuthService authService = new AuthService();
    UserService userService = new UserService();

    public AuthServiceTests() throws DataAccessException {
    }

    @BeforeEach
    void clear(){
        authService.clear();
        userService.clear();
    }

    @Test
    void newTokenTest() throws Exception {
        AuthData authData = authService.newToken("user");
        AuthData authData2 = authService.getAuthData(authData.authToken());
        assertEquals(authData.authToken(), authData2.authToken());
    }

    @Test
    void newTokenTestFail() throws Exception {
        AuthData authData = authService.newToken("user");
        String token = authData.authToken();
        AuthData authData2 = authService.newToken("user2");
        String token2 = authData2.authToken();
        assertNotEquals(token, token2);
    }

    @Test
    void getAuthDataTest() throws Exception {
        AuthData authData = authService.newToken("user");
        AuthData authData2 = authService.getAuthData(authData.authToken());
        assertEquals(authData, authData2);
    }

    @Test
    void getAuthDataTestFail() throws Exception {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            authService.getAuthData("not an auth token");
        });
        assertTrue(exception.getMessage().contains("unauthorized"));
    }

    @Test
    void deleteTest() throws Exception {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            AuthData authData = authService.newToken("user");
            String token = authData.authToken();
            authService.delete(token);
            authService.getAuthData(token);
        });
        assertTrue(exception.getMessage().contains("unauthorized"));
    }

    @Test
    void deleteTestFail() throws Exception {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            authService.delete("not an auth token");
        });
        assertTrue(exception.getMessage().contains("unauthorized"));
    }

    @Test
    void clearTest() throws Exception {
        assertDoesNotThrow(() -> {
            authService.clear();
        });
    }
}
