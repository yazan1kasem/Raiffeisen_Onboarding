package Raiffeisen.Onboarding.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name="u_user_checklists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class user_checklists {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name="uc_id")
    private String id;

    @JoinColumn(name="uc_uco")
    @ManyToOne(fetch = FetchType.EAGER)
    private CheckList User_checklist_inherited;

    @JoinColumn(name="uc_uci")
    @OneToMany(fetch = FetchType.EAGER)
    private List<user_checklist_items> User_checklist_items;

    private enum status{
        open,
        in_progress,
        done
    }

    @Column(name="uc_status")
    private status status;
}
