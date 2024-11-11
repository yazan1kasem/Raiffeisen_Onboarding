package Raiffeisen.Onboarding.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="u_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name="u_id")
    private String id;

    @Column(name="u_name")
    private String name;

    @Column(name="u_admin")
    private boolean isAdmin;
}
