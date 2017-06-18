package io.github.k_gregory.insurance.entity;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "\"user\"")
public class User {
    private String login;
    private String passwordHash;
    private Set<Role> roles;

    @Id
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "login"),
            inverseJoinColumns = @JoinColumn(name = "role")
    )
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
