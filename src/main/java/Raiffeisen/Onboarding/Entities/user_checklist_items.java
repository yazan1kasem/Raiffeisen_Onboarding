package Raiffeisen.Onboarding.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="u_user_checklist_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class user_checklist_items {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name="uci_id")
    private String id;

    @JoinColumn(name="uci_uci")
    @ManyToOne(fetch = FetchType.EAGER)
    private Item User_checklist_items;

    @Column(name="uci_checked")
    private boolean checked;
}
