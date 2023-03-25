package com.axity.office.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.transaction.annotation.Transactional;

import com.axity.office.commons.dto.RoleDto;
import com.axity.office.commons.dto.UserDto;
import com.axity.office.commons.enums.ErrorCode;
import com.axity.office.commons.exception.BusinessException;
import com.axity.office.commons.request.PaginatedRequestDto;

/**
 * Class UserServiceTest
 * 
 * @author username@axity.com
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
class UserServiceTest {
  private static final Logger LOG = LoggerFactory.getLogger(UserServiceTest.class);

  @Autowired
  private UserService userService;

  /**
   * Method to validate the paginated search
   */
  @Test
  void shouldFindUsers() {
    // given
    var request = new PaginatedRequestDto();
    request.setLimit(5);
    request.setOffset(0);

    // when
    var users = this.userService.findUsers(request);

    // then
    LOG.info("Response: {}", users);

    assertNotNull(users);
    assertNotNull(users.getData());
    assertFalse(users.getData().isEmpty());
  }

  /**
   * Method to validate the search by id
   * 
   * @param userId
   */
  @ParameterizedTest
  @ValueSource(ints = { 1 })
  void shouldFindUserById(Integer userId) {
    // when
    var user = this.userService.find(userId);

    // then
    assertNotNull(user);
    LOG.info("Response: {}", user);
  }

  /**
   * Method to validate the search for a non-existent user
   */
  @Test
  void shouldNotFindUserById() {
    // when
    var user = this.userService.find(999999);

    // then
    assertNull(user);
  }

  /**
   * @param id
   * @return Instance of RoleDto
   */
  private RoleDto createRole(int id) {
    var role = new RoleDto();
    role.setId(id);
    return role;
  }

  /**
   * Test method for creating a user with a single role
   * {@link com.axity.office.service.impl.UserServiceImpl#create(com.axity.office.commons.dto.UserDto)}.
   */
  @Test
  void shouldCreateUserWithOneRole() {
    // given
    var list = new ArrayList<RoleDto>();
    list.add(createRole(1));

    var userDto = new UserDto();
    userDto.setUsername("JohnDoe");
    userDto.setEmail("john.doe@example.com");
    userDto.setName("John");
    userDto.setLastName("Doe");
    userDto.setRoles(list);

    // when
    var response = this.userService.create(userDto);

    // then
    assertNotNull(response);
    assertEquals("OK", response.getHeader().getMessage());
    assertNotNull(response.getBody());

    this.userService.delete(userDto.getId());
  }

  /**
   * Test method for creating a user with multiple roles
   * {@link com.axity.office.service.impl.UserServiceImpl#create(com.axity.office.commons.dto.UserDto)}.
   */
  @Test
  void shouldCreateUserWithManyRoles() {
    // given
    var list = new ArrayList<RoleDto>();
    list.add(createRole(1));
    list.add(createRole(2));
    list.add(createRole(3));

    var userDto = new UserDto();
    userDto.setUsername("JohnDoe");
    userDto.setEmail("john.doe@example.com");
    userDto.setName("John");
    userDto.setLastName("Doe");
    userDto.setRoles(list);

    // when
    var response = this.userService.create(userDto);

    // then
    assertNotNull(response);
    assertEquals("OK", response.getHeader().getMessage());
    assertNotNull(response.getBody());

    this.userService.delete(userDto.getId());
  }

  /**
   * Test method for update user.
   */
  @Test
  @Disabled("TODO: Update the test according to the entity")
  void shouldUpdateUser() {
    // Given
    var user = this.userService.find(1).getBody();
    // TODO: update according to the entity

    // When
    var response = this.userService.update(user);

    // Then
    assertNotNull(response);
    assertEquals(0, response.getHeader().getCode());
    assertTrue(response.getBody());
    user = this.userService.find(1).getBody();

    // Verify the value is updated.
  }

  /**
   * Test method for validating if a username already exists in the database
   * {@link com.axity.office.service.impl.UserServiceImpl#create(com.axity.office.commons.dto.UserDto)}.
   */
  @Test
  void testValidateUsernameAlreadyExists() {
    // Initial data
    var roles = new ArrayList<RoleDto>();
    roles.add(createRole(1));

    var user = new UserDto();
    user.setUsername("johndoe"); // Username already exists in db
    user.setEmail("johndoe@example.com");
    user.setName("John");
    user.setLastName("Doe");
    user.setRoles(roles);

    // Call
    var response = this.userService.create(user);

    // Validation
    assertNotNull(response);
    assertEquals(ErrorCode.USERNAME_ALREADY_EXISTS.getCode(), response.getHeader().getCode());
  }

  /**
   * 
   * Test method to validate if selected roles do not exist
   * {@link com.axity.office.service.impl.UserServiceImpl#create(com.axity.office.commons.dto.UserDto)}.
   */
  @Test
  void testValidateSelectedRolesDoNotExist() {
    // Initial data
    var roles = new ArrayList<RoleDto>();
    roles.add(createRole(99)); // Role selected does not exist in the database

    var userDto = new UserDto();

    userDto.setUsername("JohnDoe");
    userDto.setEmail("john.doe@example.com");
    userDto.setName("John");
    userDto.setLastName("Doe");
    userDto.setRoles(roles);

    // Method call
    var response = this.userService.create(userDto);

    // Validation
    assertNotNull(response);
    assertEquals(ErrorCode.ROLE_NOT_FOUND.getCode(), response.getHeader().getCode());

  }

  /**
   * Test method for validating if an email address already exists in the database
   * {@link com.axity.office.service.impl.UserServiceImpl#create(com.axity.office.commons.dto.UserDto)}.
   */
  @Test
  void testValidateEmailAlreadyExists() {
    // Initial data
    var roles = new ArrayList<RoleDto>();
    roles.add(createRole(1));

    var user = new UserDto();
    user.setUsername("johndoe");
    user.setEmail("johndoe@example.com"); // Email already exists in db
    user.setName("John");
    user.setLastName("Doe");
    user.setRoles(roles);

    // Call
    var response = this.userService.create(user);

    // Validation
    assertNotNull(response);
    assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS.getCode(), response.getHeader().getCode());
  }

  /**
   * Test method for update inexistent user.
   */
  @Test
  void shouldNotUpdateInexistentUser() {
    // Given
    var user = new UserDto();
    user.setId(999999);

    // Then
    var ex = assertThrows(BusinessException.class, () -> this.userService.update(user));
    assertEquals(ErrorCode.OFFICE_NOT_FOUND.getCode(), ex.getCode());
  }
}
