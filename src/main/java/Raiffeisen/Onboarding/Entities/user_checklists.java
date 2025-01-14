package Raiffeisen.Onboarding.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a user-specific version of a checklist.
 * The original checklist remains unaltered, while this class allows users to
 * manage their own modifications and share the checklist with others.
 */
@Entity
@Table(name = "user_checklists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class user_checklists {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "uc_id")
    private String id;

    /**
     * The original checklist that this user-specific checklist is based on.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_checklist_id", nullable = false)
    private CheckList originalChecklist;

    /**
     * The user who owns this checklist.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * List of items in this user-specific checklist.
     */
    @OneToMany(mappedBy = "userChecklist", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<user_checklist_items> items;

    /**
     * The status of the checklist (e.g., IN_PROGRESS, COMPLETED, LOCKED).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChecklistStatus status;

    /**
     * Indicates if the checklist is shared with other users.
     */
    @Column(name = "is_shared", nullable = false)
    private boolean isShared = false;

    /**
     * Indicates if the checklist is locked and cannot be modified.
     */
    @Column(name = "is_locked", nullable = false)
    private boolean isLocked = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Possible statuses for a checklist.
     */
    public enum ChecklistStatus {
        IN_PROGRESS,
        COMPLETED,
        LOCKED
    }
}