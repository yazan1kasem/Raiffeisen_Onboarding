package Raiffeisen.Onboarding.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="p_position")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "p_id")
    private String id;

    @Column(name = "p_bez")
    private String bezeichnung;

}