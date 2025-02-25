package Raiffeisen.Onboarding.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="I_Item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name="I_id")
    private String id;

    @Column(name="I_name")
    private String name;

    @Column(name="I_type")
    private String type;

    @Column(name="I_suchbegriff")
    private String suchbegriff;

    public Item(String name, String type, String suchbegriff) {
        this.name = name;
        this.type = type;
        this.suchbegriff = "";
    }

}