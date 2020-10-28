package pl.michalzadrozny.asmodule3.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @Setter
    private AppUser appUser;

    @Setter
    private String value;

    public VerificationToken(AppUser appUser, String value) {
        this.appUser = appUser;
        this.value = value;
    }

    public VerificationToken() {
    }
}
