package pl.michalzadrozny.asmodule3.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Getter
@Setter
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private Role role;
    private boolean isAccountEnabled;
    private boolean adminRightsRequest;
    private boolean adminApprovedRole;

    public AppUser() {
        this.role = Role.ROLE_USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (adminRightsRequest && adminApprovedRole) {
            return Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_ADMIN.getAuthority()));
        }
        return Collections.singleton(new SimpleGrantedAuthority(Role.ROLE_USER.getAuthority()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isAccountEnabled;
    }
}
