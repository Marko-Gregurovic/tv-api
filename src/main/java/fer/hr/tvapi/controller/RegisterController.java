package fer.hr.tvapi.controller;

import fer.hr.tvapi.dto.RegisterDTO;
import fer.hr.tvapi.dto.UserDTO;
import fer.hr.tvapi.entity.Users;
import fer.hr.tvapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    private final static Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    public RegisterController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody RegisterDTO registerDTO) {
        Users user = Users.builder()
            .email(registerDTO.getEmail())
            .password(registerDTO.getPassword())
            .build();

        user = userService.createUser(user);
        LOGGER.info("Registration finished for User with id: {}", user.getId());
        UserDTO output = UserDTO.builder()
            .email(user.getEmail())
            .build();
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

}
