package e.group.thesismanager.service;

import e.group.thesismanager.exception.NotFoundException;
import e.group.thesismanager.model.Role;
import e.group.thesismanager.model.User;
import e.group.thesismanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    private static final String FN_1 = "fn1";
    private static final String LN_1 = "ln1";
    private static final List<User> USER_LIST = new LinkedList<>();

    AdminService adminService;


    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        adminService = new AdminServiceImpl(userRepository);

        User u1 = new User(FN_1, LN_1, new HashSet<>(Arrays.asList(Role.STUDENT)));
        User u2 = new User(FN_1, LN_1, new HashSet<>(Arrays.asList(Role.STUDENT)));

        USER_LIST.clear();
        USER_LIST.add(u1);
        USER_LIST.add(u2);
    }

    @Test
    void findAllUsers() {

        when(userRepository.findAll()).thenReturn(USER_LIST);
        assertEquals(2, adminService.findAllUsers().size());
        assertArrayEquals(USER_LIST.toArray(), adminService.findAllUsers().toArray());

        when(userRepository.findAll()).thenReturn(null);
        assertNull(adminService.findAllUsers());
    }

    @Test
    void findUsersByLastName() {

        when(userRepository.findUsersByLastName(anyString())).thenReturn(Arrays.asList(USER_LIST.get(0)));
        assertArrayEquals(new User[]{USER_LIST.get(0)}, adminService.findUsersByLastName(LN_1).toArray());

        when(userRepository.findUsersByLastName(anyString())).thenReturn(null);
        assertNull(adminService.findUsersByLastName(LN_1));
    }

    @Test
    void findUsersByRole() {

        when(userRepository.findAll()).thenReturn(USER_LIST);
        assertArrayEquals(USER_LIST.toArray(), adminService.findUsersByRole(Role.STUDENT).toArray());
        assertEquals(0, adminService.findUsersByRole(Role.COORDINATOR).size());

        USER_LIST.get(0).getRoles().add(Role.COORDINATOR);
        assertArrayEquals(new User[] {USER_LIST.get(0)}, adminService.findUsersByRole(Role.COORDINATOR).toArray());

        assertEquals(0, adminService.findUsersByRole(Role.SUPERVISOR).size());

    }

    @Test
    void findUsersById() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(USER_LIST.get(0)));
        assertEquals(USER_LIST.get(0), adminService.findUserById(2L));

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> adminService.findUserById(2L));
    }

    @Test
    void saveUser() {

        String lastName = "last_name";

        User u1 = new User(FN_1, lastName, new HashSet<>(Arrays.asList(Role.STUDENT, Role.OPPONENT)));
        when(userRepository.save(u1)).thenReturn(u1);
        assertEquals(u1, adminService.saveUser(u1));
    }

    @Test
    void deleteUserById() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(USER_LIST.get(0)));
        adminService.deleteUserById(1L);
        verify(userRepository, times(1)).deleteById(anyLong());

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> adminService.deleteUserById(1L));
    }

    @Test
    void assignRoleToUserById() {

        User user = USER_LIST.get(0);
        int size = user.getRoles().size();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        user = adminService.assignRoleToUserById(1L, Role.ADMIN);
        assertEquals(size+1, user.getRoles().size());
        assertTrue(user.getRoles().contains(Role.ADMIN));
    }
}