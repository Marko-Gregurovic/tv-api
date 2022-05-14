package fer.hr.tvapi.service;

import fer.hr.tvapi.entity.Users;

import java.util.Optional;

public interface UserService {

  Optional<Users> findByEmail(String email);

  Optional<Users> findById(Long id);

  Users createUser(Users user);

}
