package Raiffeisen.Onboarding.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents an individual item within a user-specific checklist.
 */
@Entity
@Table(name = "user_checklist_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User_Checklist_Items {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "uci_id")
    private String id;

    /**
     * The original item that this user-specific item is based on.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "original_item_id", nullable = false)
    private Item originalItem;

    /**
     * Indicates if the item is checked.
     */
    @JsonProperty("isChecked")
    @Column(name = "is_checked", nullable = false)
    private boolean isChecked;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}