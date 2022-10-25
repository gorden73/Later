package ru.practicum.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = 2084451991L;

    public static final QItem item = new QItem("item");

    public final DateTimePath<java.time.Instant> dateResolved = createDateTime("dateResolved", java.time.Instant.class);

    public final BooleanPath hasImage = createBoolean("hasImage");

    public final BooleanPath hasVideo = createBoolean("hasVideo");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath mimeType = createString("mimeType");

    public final StringPath resolvedUrl = createString("resolvedUrl");

    public final EnumPath<State> state = createEnum("state", State.class);

    public final SetPath<String, StringPath> tags = this.<String, StringPath>createSet("tags", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final BooleanPath unread = createBoolean("unread");

    public final StringPath url = createString("url");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QItem(String variable) {
        super(Item.class, forVariable(variable));
    }

    public QItem(Path<? extends Item> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItem(PathMetadata metadata) {
        super(Item.class, metadata);
    }

}

