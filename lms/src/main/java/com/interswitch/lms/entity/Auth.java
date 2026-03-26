//package com.interswitch.lms.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "auths")
//@Getter
//@Setter
//public class Auth {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true, length = 50)
//    private String email;
//
//    @Column(nullable = false, unique = true, length = 50)
//    private String username;
//
//    @JsonIgnore
//    @Column(nullable = false)
//    private String password;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, length = 20)
//    private Role role;
//
//    public enum Role {
//        STUDENT,
//        TEACHER,
//        ADMIN
//    }
//
//    @CreationTimestamp
//    @Column(nullable = false,  updatable = false)
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    @Column()
//    private LocalDateTime updatedAt;
//
//    @OneToOne(mappedBy = "auth", cascade = CascadeType.ALL)
////    @JsonIgnore
//    @JsonManagedReference
//    private Student student;
//
//    @OneToOne(mappedBy = "auth", cascade = CascadeType.ALL)
////    @JsonIgnore
//    @JsonManagedReference
//    private Teacher teacher;
//}

package com.interswitch.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "auths")
@Getter
@Setter
public class Auth implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "auth_roles",
            joinColumns = @JoinColumn(name = "auth_id")
    )
    @Column(name = "role")
    private Set<Role> roles;

    public enum Role {
        STUDENT,
        TEACHER,
        ADMIN
    }

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "auth", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Student student;

    @OneToOne(mappedBy = "auth", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Teacher teacher;

    // ===================== UserDetails Methods =====================
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Map all roles to Spring Security authorities
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return email; // Spring Security uses email as username
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
        return true;
    }
}