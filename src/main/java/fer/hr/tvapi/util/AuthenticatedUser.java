package fer.hr.tvapi.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

public class AuthenticatedUser extends User {

    private Long id;

    public AuthenticatedUser(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            Long id) {
        super(username, password, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
