package fer.hr.tvapi.service;

import fer.hr.tvapi.entity.Users;

import java.util.Optional;

public interface UserService {

  public Optional<Users> findByEmail(String email);

  Users createUser(Users user);

}
