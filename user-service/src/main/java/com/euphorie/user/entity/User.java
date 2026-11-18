package com.euphorie.user.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;


import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=true)
    private String email;

    @Column(nullable = true)
    private String hashedPassword; 

    
    @Column(nullable = true , unique= true)
    private String githubId; 

    @Column(nullable= true)
    private String githubPseudo;

    @PrePersist
    @PreUpdate  
    private void validateUser() {
        boolean noEmail = email == null || email.isBlank();
        boolean noGithub = githubPseudo == null || githubPseudo.isBlank();

        if (noEmail && noGithub) {
            throw new IllegalStateException(
                "Email ou pseudo GitHub obligatoire"
            );
        }
    }
}