package Raiffeisen.Onboarding.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Entity
@Table(name="c_checklist")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name="c_id")
    private String id;

    @ManyToMany
    @JoinTable(
            name = "c_checkliste_has_u_user",
            joinColumns = @JoinColumn(name = "c_checkliste_c_id"),
            inverseJoinColumns = @JoinColumn(name = "u_user_u_id")
    )
    private Set<User> users;

    @Column(name="c_saved")
    private boolean saved;

    @Column(name="c_überschrift")
    private String überschrift;

    @ManyToOne
    @JoinColumn(name = "d_c_id", referencedColumnName = "d_id")
    private Device device;

    @ManyToOne
    @JoinColumn(name = "a_c_id", referencedColumnName = "d_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "p_c_id", referencedColumnName = "p_id")
    private Position position;
}
