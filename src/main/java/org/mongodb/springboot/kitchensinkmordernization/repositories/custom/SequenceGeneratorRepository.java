package org.mongodb.springboot.kitchensinkmordernization.repositories.custom;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableMongoRepositories(basePackageClasses = SequenceGeneratorRepository.class)
public interface SequenceGeneratorRepository {
    public Long generateSequenceByName(String name);
}
