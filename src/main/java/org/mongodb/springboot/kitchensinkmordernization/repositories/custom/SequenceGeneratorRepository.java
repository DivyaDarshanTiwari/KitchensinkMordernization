package org.mongodb.springboot.kitchensinkmordernization.repositories.custom;

import org.springframework.stereotype.Repository;

@Repository
public interface SequenceGeneratorRepository {
    public Long generateSequenceByName(String name);
}
