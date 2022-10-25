package ru.practicum.item;

import com.querydsl.core.annotations.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "items")
@NoArgsConstructor
class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    private String url;

    @ElementCollection
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "name")
    private Set<String> tags = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "resolved_url")
    private String resolvedUrl;
    @Column(name = "mime_type")
    private String mimeType;
    private String title;
    @Column(name = "has_image")
    private boolean hasImage;
    @Column(name = "has_video")
    private boolean hasVideo;
    @Column(name = "date_resolved")
    private Instant dateResolved;
    private boolean unread = true;

    public Item(Long userId, String url, Set<String> tags) {
        this.userId = userId;
        this.url = url;
        this.tags = tags;
    }

    public Item(Long id, Long userId, String url, Set<String> tags) {
        this.id = id;
        this.userId = userId;
        this.url = url;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        return id != null && id.equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}