package com.philips.onespace.jpa.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
@Entity
@Table(name = "action")
public class ActionEntity {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(name = "action_id")
    private UUID id;

    @Column(name = "initiator")
    private UUID initiator;

    @Column(name = "datetime")
    private LocalDateTime dateTime;

    @Column(name = "title")
    @Size(max = 255)
    private String title;

    @Column(name = "type")
    @Size(max = 20)
    private String type;

    @Column(name = "notify")
    private boolean notify;

    @Column(name = "message")
    @Size(max = 2048)
    private String message;

    @Column(name = "due_datetime")
    private LocalDateTime dueDateTime;

    @Column(name = "expiry_datetime")
    private LocalDateTime expiryDateTime;

    @Column(name = "related_object")
    @Size(max = 255)
    private String relatedObject;

    @Column(name = "metadata", columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private Map<String, Object> metadata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source", nullable = true)
    private ApplicationEntity application;

    @OneToMany(mappedBy = "actionEntity", cascade = CascadeType.ALL)
    private List<ActionOwnersEntity> actionOwnersEntities;

}
