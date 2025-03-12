# Code Explanation
This code defines two Java entity classes for a notebook application using Spring Boot and JPA (Java Persistence API):
## 1. Role Class

- An entity that represents user roles in the application (likely for authorization purposes)
- Uses Lombok annotations (`@Data`, `@NoArgsConstructor`, etc.) to reduce boilerplate code
- Has an `AppRole` enum field that defines the actual role name (though the enum definition isn't included)
- Contains a one-to-many relationship with the `User` entity

```java
package com.notebook.app.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "roles")
public class Role{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "role_name")
    private AppRole roleName;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JsonBackReference
    @ToString.Exclude
    private Set<User> users = new HashSet<>();

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }
}
```

## 2. User Class

- An entity that represents users of the application
- Contains various fields for user information (username, email, password)
- Includes Spring Security related fields (accountNonLocked, accountNonExpired, etc.)
- Has fields for two-factor authentication
- Contains a many-to-one relationship with the `Role` entity
- Tracks creation and update timestamps
- Overrides `equals()` and `hashCode()` methods for proper identity management

```java
package com.notebook.app.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Size(max = 20)
    @Column(name = "username")
    private String userName;

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(name = "email")
    private String email;

    @Size(max = 120)
    @Column(name = "password")
    @JsonIgnore
    private String password;

    private boolean accountNonLocked = true;
    private boolean accountNonExpired = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    private LocalDate credentialsExpiryDate;
    private LocalDate accountExpiryDate;

    private String twoFactorSecret;
    private boolean isTwoFactorEnabled = false;
    private String signUpMethod;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @JsonBackReference
    @ToString.Exclude
    private Role role;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    // equals and hashCode methods are needed for Spring Security to work properly with the User class
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return userId != null && userId.equals(((User) o).getUserId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
```

# Key Relationships

- A `Role` can be assigned to many `User` entities (one-to-many)
- Each `User` belongs to a single `Role` (many-to-one)
- The relationship is bidirectional with `Role.users` and `User.role` properties



<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 800 400">
  <!-- Role Class -->
  <rect x="100" y="100" width="250" height="180" fill="#e6f7ff" stroke="#1890ff" stroke-width="2" rx="5" ry="5"/>
  <line x1="100" y1="135" x2="350" y2="135" stroke="#1890ff" stroke-width="2"/>
  <text x="175" y="125" font-family="Arial" font-size="18" font-weight="bold">Role</text>
  <text x="110" y="160" font-family="Arial" font-size="14">- roleId: Integer</text>
  <text x="110" y="185" font-family="Arial" font-size="14">- roleName: AppRole (enum)</text>
  <text x="110" y="210" font-family="Arial" font-size="14">- users: Set&lt;User&gt;</text>
  <text x="110" y="235" font-family="Arial" font-size="14">+ Role(roleName: AppRole)</text>
  <text x="110" y="260" font-family="Arial" font-size="14">+ getters/setters (via @Data)</text>

  <!-- User Class -->
  <rect x="450" y="60" width="250" height="280" fill="#f6ffed" stroke="#52c41a" stroke-width="2" rx="5" ry="5"/>
  <line x1="450" y1="95" x2="700" y2="95" stroke="#52c41a" stroke-width="2"/>
  <text x="525" y="85" font-family="Arial" font-size="18" font-weight="bold">User</text>
  <text x="460" y="120" font-family="Arial" font-size="14">- userId: Long</text>
  <text x="460" y="145" font-family="Arial" font-size="14">- userName: String</text>
  <text x="460" y="170" font-family="Arial" font-size="14">- email: String</text>
  <text x="460" y="195" font-family="Arial" font-size="14">- password: String</text>
  <text x="460" y="220" font-family="Arial" font-size="14">- security fields (enabled, etc.)</text>
  <text x="460" y="245" font-family="Arial" font-size="14">- twoFactorSecret: String</text>
  <text x="460" y="270" font-family="Arial" font-size="14">- role: Role</text>
  <text x="460" y="295" font-family="Arial" font-size="14">- timestamps (created/updated)</text>
  <text x="460" y="320" font-family="Arial" font-size="14">+ constructors, equals, hashCode</text>

  <!-- Relationship -->
  <line x1="350" y1="190" x2="450" y2="270" stroke="#000000" stroke-width="2"/>
  <polygon points="350,190 365,185 360,200" fill="#000000"/>
  <polygon points="450,270 435,275 440,260" fill="#000000"/>
  <text x="370" y="210" font-family="Arial" font-size="14">1</text>
  <text x="430" y="250" font-family="Arial" font-size="14">*</text>

  <!-- JPA Annotations -->
  <rect x="200" y="20" width="400" height="30" fill="#fff2e8" stroke="#fa8c16" stroke-width="2" rx="5" ry="5"/>
  <text x="215" y="40" font-family="Arial" font-size="14">JPA Annotations (@Entity, @Table, @Column, etc.)</text>

  <!-- Lombok -->
  <rect x="200" y="350" width="400" height="30" fill="#f9f0ff" stroke="#722ed1" stroke-width="2" rx="5" ry="5"/>
  <text x="245" y="370" font-family="Arial" font-size="14">Lombok (@Data, @NoArgsConstructor, etc.)</text>
</svg>

# Detailed Annotations Explanation
## Annotations Explained

### JPA Annotations
- `@Entity`: Marks classes as JPA entities (database tables)
- `@Table`: Specifies table details and constraints
- `@Id` and `@GeneratedValue`: Define primary key fields with auto-increment strategy
- `@Column`: Customizes column properties (name, length)
- `@JoinColumn`: Defines the foreign key relationship
- `@OneToMany` / `@ManyToOne`: Define the relationship cardinality
- `@Enumerated`: Maps Java enum to database column

### Lombok Annotations
- `@Data`: Generates getters, setters, toString, equals, and hashCode methods
- `@NoArgsConstructor` / `@AllArgsConstructor`: Generate constructors
- `@ToString.Exclude`: Prevents circular references in toString() output

### JSON Annotations
- `@JsonBackReference`: Manages bidirectional relationships for JSON serialization
- `@JsonIgnore`: Prevents password field from being included in JSON responses

### Validation Annotations
- `@NotBlank`: Ensures fields are not empty
- `@Size`: Restricts field length
- `@Email`: Validates email format

### Hibernate Annotations
- `@CreationTimestamp`: Automatically sets creation timestamp
- `@UpdateTimestamp`: Automatically updates timestamp on entity changes

# Bidirectional Relationship Implementation
The code defines a bidirectional relationship between User and Role entities:

## One-to-Many Relationship
The code defines a one-to-many relationship between User and Role entities:
- A Role can have multiple Users
- Each User belongs to exactly one Role
- The relationship is maintained through users collection in Role class
- Foreign key mapping with role_id column

## Many-to-One Relationship
The code defines a many-to-one relationship between User and Role entities:
- A User belongs to a single Role
- Each Role can have multiple Users
- The relationship is maintained through role reference in User class
- Foreign key mapping with user_id column

## Design Purpose

This entity relationship design supports role-based access control (RBAC) in the application:
- Each user is assigned exactly one role
- Roles determine user permissions and access rights
- The bidirectional mapping allows efficient querying from both directions