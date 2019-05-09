package e.group.thesismanager.service;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AdminServiceIT {

    @Autowired
    AdminService adminService;

    private static final String FN_1 = "fn1";
    private static final String LN_1 = "ln1";
    private static final String UN_1 = "un1";
    private static final String PW_1 = "pw1";
    private static final List<User> USER_LIST = new LinkedList<>();

    @Transactional
    @Test
    public void adminServiceTest_AddAndDelete() {

        User u1 = new User(FN_1, LN_1, UN_1, PW_1, new HashSet<>(Arrays.asList(Role.STUDENT)));
        u1 = adminService.saveUser(u1);
        Long id = u1.getId();
        assertEquals(u1, adminService.findUserById(id));

        adminService.deleteUserById(id);
        assertFalse(adminService.findAllUsers().contains(u1));
    }

    @Test
    public void adminServiceTest_Exception() {

        assertThrows(NotFoundException.class, () -> adminService.findUserById(5506L));
    }

    @Transactional
    @Test
    void adminServiceTest_AssignRole() {

        User u1 = new User(FN_1, LN_1, UN_1, PW_1, new HashSet<>(Arrays.asList(Role.STUDENT)));
        u1 = adminService.saveUser(u1);
        u1 = adminService.assignRoleToUserById(u1.getId(), Role.ADMIN);

        assertEquals(2, u1.getRoles().size());
        assertTrue(u1.getRoles().contains(Role.ADMIN));
        assertTrue(adminService.findUsersByRole(Role.ADMIN).
                stream().filter(user -> user.getLastName().equals(LN_1))
                .count() != 0 );
    }
}