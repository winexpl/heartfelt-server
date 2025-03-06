package com.app.heartfelt.model;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.heartfelt.security.Role;
import com.app.heartfelt.utils.RoleListConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Builder
@Table(name  =  "users",  schema  =  "public")
@NoArgsConstructor
@AllArgsConstructor
@NamedNativeQueries({
    @NamedNativeQuery(name = "User.findAllByUsername",
    query = "SELECT * FROM users " +
        "WHERE u_username ILIKE '%' || :username || '%' ",
    resultClass = User.class)
})
public class User implements UserDetails {
    @Id
    @Column(name = "u_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name = "u_username", nullable = false)
    private String username;

    @Column(name = "u_password", nullable = false)
    private String password;

    @Column(name = "u_nickname")
    private String nickname;

    @Convert(converter = RoleListConverter.class)
    @Column(name = "u_roles")
    private List<Role> role;

    @Column(name = "u_created_at")
    private Timestamp time;

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return role;
    }
}