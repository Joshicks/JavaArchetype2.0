package com.axity.office.persistence;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axity.office.model.UserDO;

@Repository
public interface UserPersistence extends JpaRepository<UserDO, Integer> {

  Optional<UserDO> findUserByUsername(String username);

  Optional<UserDO> findUserByEmail(String email);

  List<UserDO> findAll();

}
