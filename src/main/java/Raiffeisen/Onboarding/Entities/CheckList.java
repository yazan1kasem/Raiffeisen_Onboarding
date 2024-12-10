package Raiffeisen.Onboarding.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name="c_checklist")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID, generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name="c_id")
    private String id;

    @Column(name="c_überschrift")
    private String ueberschrift;

    @Column(name = "c_abteilungsname")
    private String abteilungsname;

    @Column(name = "c_Position")
    private String position;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "c_i_id")
    private List<Item> items;


}
