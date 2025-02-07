package Raiffeisen.Onboarding.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="u_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User implements UserDetails {

    @Id
    @Column(name="u_username")
    private String username;

    @Column(name = "u_password")
    private String password;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;


    @Column(name = "u_enabled")
    @Builder.Default
    private boolean enabled = true;


    public enum Role {
        USER,
        ADMIN,
        SUPER_ADMIN
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "u_role")
    private Role role;

    @Override
    @JsonIgnore // Prevents serialization issues
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @JsonProperty("authorities")
    public List<String> getAuthoritiesAsStrings() {
        return Collections.singletonList("ROLE_" + role.name());
    }


}

