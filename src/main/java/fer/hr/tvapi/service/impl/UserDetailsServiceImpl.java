package fer.hr.tvapi.service.impl;

import fer.hr.tvapi.entity.Role;
import fer.hr.tvapi.entity.Users;
import fer.hr.tvapi.exception.NotFoundException;
import fer.hr.tvapi.service.UserService;
import fer.hr.tvapi.util.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String email = username;
        Optional<Users> userOptional = userService.findByEmail(email);

        if(userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        Users user = userOptional.get();

        // Fetch all user roles
        List<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().getName());
        authorities.add(authority);

        return new AuthenticatedUser(email, user.getPassword(), authorities, user.getId());
    }
}
