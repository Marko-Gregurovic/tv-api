package fer.hr.tvapi.service.impl;

import fer.hr.tvapi.entity.Users;
import fer.hr.tvapi.exception.EmailAlreadyExistsException;
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


  private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

  public Optional<Users> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public Users createUser(Users user) {
    if (findByEmail(user.getEmail()).isPresent()) {
      LOGGER.error("Cannot create User beacause email is already taken: {} ", user.getEmail());
      throw new EmailAlreadyExistsException("Email is already taken.");
    }
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

    // user
    user.setRoleId(1L);

    user = userRepository.save(user);
    LOGGER.info("Successfully created User with id: {}", user.getId());
    return user;
  }
}
