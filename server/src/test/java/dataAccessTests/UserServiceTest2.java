package dataAccessTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;

import static org.junit.jupiter.api.Assertions.*;


public class UserServiceTest2 {

    static UserDAO userDAO;

    static {
        try {
            userDAO = new SQLUserDAO();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void clear(){
        userDAO.clear();
    }

    @Test
    void addUser() throws Exception{
        assertDoesNotThrow(() -> {
            var user1 = new UserData("user1", "pass1", "email1");
            var user2 = new UserData("user2", "pass2", "email2");
            var user3 = new UserData("user3", "pass3", "email3");
            userDAO.addUser(user1);
            userDAO.addUser(user2);
            userDAO.addUser(user3);

            assertNotNull(userDAO.getUser(user1.username()));
            assertEquals(userDAO.getUser(user2.username()), user2);
            assertNotNull(userDAO.getUser(user1.username()));
        });
    }

    @Test
    void addUserFail() throws DataAccessException{
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            var user1 = new UserData("user1", null, "email1");
            userDAO.addUser(user1);
        });
        assertTrue(exception.getMessage().contains("bad request"));
    }

    @Test
    void verifyUserTest() throws Exception {
        var user1 = new UserData("user1", "pass1", "email1");
        userDAO.addUser(user1);
        assertEquals(userDAO.getUser(user1.username()), user1) ;
    }

    @Test
    void verifyUserTestFail() throws DataAccessException {
        var user2 = new UserData("user2", "pass2", "email2");
        assertNotEquals(userDAO.getUser(user2.username()), "user2");
    }
    @Test
    void clearTest(){
        assertDoesNotThrow(() -> {
            userDAO.clear();
        });
        assertTrue(userDAO.getUserList() == null);
    }















}
