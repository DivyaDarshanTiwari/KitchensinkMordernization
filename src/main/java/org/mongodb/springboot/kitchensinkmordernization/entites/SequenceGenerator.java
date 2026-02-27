package org.mongodb.springboot.kitchensinkmordernization.entites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("sequence generator")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SequenceGenerator {
    @Id
    private String id;

    @Field("collection_name")
    @Indexed
    private String collectionName;

    @Field("count")
    private Long count;
}
