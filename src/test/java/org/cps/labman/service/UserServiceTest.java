package org.cps.labman.service;

import org.cps.labman.dto.UserDto;
import org.cps.labman.exception.DataNotFoundException;
import org.cps.labman.exception.UserIdMismatchException;
import org.cps.labman.persistence.enums.UserRole;
import org.cps.labman.persistence.model.Role;
import org.cps.labman.persistence.model.User;
import org.cps.labman.persistence.repo.RoleRepository;
import org.cps.labman.persistence.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder, roleRepository);
        Mockito.when(roleRepository.save(any())).thenReturn(Mockito.mock(Role.class));
    }

    @ParameterizedTest
    @MethodSource("userDtoProvider")
    void testCreateUser(UserDto userDto) {
        User user = new User();
        user.setId(1L);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAge(userDto.getAge());
        user.setGender(userDto.getGender());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEmail(userDto.getEmail());
        user.setPassword("hashedPassword");
        user.setRoles(List.of(createRoleIfNotExist(userDto)));

        Mockito.when(passwordEncoder.encode(userDto.getPassword())).thenReturn("hashedPassword");
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(userDto);

        Assertions.assertEquals(user.getId(), result.getId());
        Assertions.assertEquals(user.getFirstName(), result.getFirstName());
        Assertions.assertEquals(user.getLastName(), result.getLastName());
        Assertions.assertEquals(user.getAge(), result.getAge());
        Assertions.assertEquals(user.getGender(), result.getGender());
        Assertions.assertEquals(user.getPhoneNumber(), result.getPhoneNumber());
        Assertions.assertEquals(user.getEmail(), result.getEmail());
        Assertions.assertEquals(user.getPassword(), result.getPassword());
        Assertions.assertEquals(user.getRoles(), result.getRoles());

        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    private static Stream<Arguments> userDtoProvider() {
        return Stream.of(
                Arguments.of(createUserDto("John", "Doe", 30, "Male", 1234567890L, "john@example.com", "password")),
                Arguments.of(createUserDto("Jane", "Smith", 25, "Female", 9876543210L, "jane@example.com", "password"))
                // Add more test cases as needed
        );
    }

    private static UserDto createUserDto(String firstName, String lastName, int age, String gender, Long phoneNumber, String email, String password) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setAge(age);
        userDto.setGender(gender);
        userDto.setPhoneNumber(phoneNumber);
        userDto.setEmail(email);
        userDto.setPassword(password);
        return userDto;
    }

    @Test
    void testFindAll() {
        Iterable<User> users = Mockito.mock(Iterable.class);
        Mockito.when(userRepository.findAll()).thenReturn(users);

        Iterable<User> result = userService.findAll();

        Assertions.assertEquals(users, result);
    }

    @Test
    void testFindUser() throws DataNotFoundException {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setFirstName("John");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findUser(userId);

        Assertions.assertEquals(user, result);
    }

    @Test
    void testFindUserNotFound() {
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            userService.findUser(userId);
        });
    }

    @Test
    void testFindUserByEmail() throws DataNotFoundException {
        String email = "john@example.com";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userService.findUserByEmail(email);

        Assertions.assertEquals(user, result);
    }

    @Test
    void testDeleteUser() throws DataNotFoundException {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setFirstName("John");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUserNotFound() {
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            userService.deleteUser(userId);
        });
    }

    @Test
    void testUpdateUserMismatchedId() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setFirstName("John");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User updatedUser = new User();
        updatedUser.setId(2L);
        updatedUser.setFirstName("John Updated");

        Assertions.assertThrows(UserIdMismatchException.class, () -> {
            userService.updateUser(updatedUser, userId);
        });

        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }

    @Test
    void testUpdateUserNotFound() {
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setFirstName("John Updated");

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            userService.updateUser(updatedUser, userId);
        });

        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }

    private Role createRoleIfNotExist(UserDto userDto) {
        UserRole userRole;
        if (userDto.getEmail().startsWith("admin")) {
            userRole = UserRole.ROLE_ADMIN;
        } else if (userDto.getEmail().startsWith("lt")) {
            userRole = UserRole.ROLE_LT;
        } else {
            userRole = UserRole.ROLE_USER;
        }
        Role role = new Role();
        role.setName(userRole.name());
        return role;
    }
}
