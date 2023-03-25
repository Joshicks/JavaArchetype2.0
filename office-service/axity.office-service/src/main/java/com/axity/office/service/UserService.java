package com.axity.office.service;

import com.axity.office.commons.dto.UserDto;
import com.axity.office.commons.request.PaginatedRequestDto;
import com.axity.office.commons.response.GenericResponseDto;
import com.axity.office.commons.response.PaginatedResponseDto;

/**
 * Service interface for managing {@link com.axity.office.commons.dto.UserDto}
 * instances.
 * 
 * @author username@axity.com
 */
public interface UserService {

  /**
   * Retrieves paginated user list.
   *
   * @param request paginated request.
   * @return the paginated response of the user list.
   */
  PaginatedResponseDto<UserDto> findUsers(PaginatedRequestDto request);

  /**
   * Retrieves user details for the provided id.
   *
   * @param id the id of the user.
   * @return the generic response of the user details.
   */
  GenericResponseDto<UserDto> find(Integer id);

  /**
   * Creates a new user with provided data.
   *
   * @param dto the user dto containing the user details.
   * @return the generic response of the created user.
   */
  GenericResponseDto<UserDto> create(UserDto dto);

  /**
   * Updates an existing user with provided data.
   *
   * @param dto the user dto containing the updated user details.
   * @return the generic response of the update status.
   */
  GenericResponseDto<Boolean> update(UserDto dto);

  /**
   * Deletes a user for the provided id.
   *
   * @param id the id of the user to be deleted.
   * @return the generic response of the delete status.
   */
  GenericResponseDto<Boolean> delete(Integer id);

  /**
   * Checks if the provided username already exists.
   *
   * @param username the username to be checked.
   * @return true if username already exists, false otherwise.
   */
  boolean existUsername(String username);

  /**
   * Checks if the provided email already exists.
   *
   * @param email the email to be checked.
   * @return true if email already exists, false otherwise.
   */
  boolean existEmail(String email);
  /**
 * Method to check if a role exists
 *
 * @param roleId The ID of the role to check
 * @return true if the role exists, false otherwise
 */
boolean doesRoleExist(Integer roleId);


}
