package fer.hr.tvapi.service.impl;

import fer.hr.tvapi.entity.Role;
import fer.hr.tvapi.entity.Users;
import fer.hr.tvapi.exception.EmailAlreadyExistsException;
import fer.hr.tvapi.repository.RoleRepository;
import fer.hr.tvapi.repository.UserRepository;
import fer.hr.tvapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

  public Optional<Users> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public Optional<Users> findById(Long id) {
    return userRepository.findById(id);
  }

  @Override
  public Users createUser(Users user) {
    if (findByEmail(user.getEmail()).isPresent()) {
      LOGGER.error("Cannot create User beacause email is already taken: {} ", user.getEmail());
      throw new EmailAlreadyExistsException("Email is already taken.");
    }
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

    Role role = roleRepository.getById(1L);
    // user
    user.setRole(role);

    user = userRepository.save(user);
    LOGGER.info("Successfully created User with id: {}", user.getId());
    return user;
  }
}
