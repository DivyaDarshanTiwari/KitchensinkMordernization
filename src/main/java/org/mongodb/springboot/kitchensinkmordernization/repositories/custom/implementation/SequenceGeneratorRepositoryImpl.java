package org.mongodb.springboot.kitchensinkmordernization.repositories.custom.implementation;


import lombok.RequiredArgsConstructor;
import org.mongodb.springboot.kitchensinkmordernization.entites.SequenceGenerator;
import org.mongodb.springboot.kitchensinkmordernization.repositories.custom.SequenceGeneratorRepository;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SequenceGeneratorRepositoryImpl implements SequenceGeneratorRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Long generateSequenceByName(String name){
        Query query = new Query();
        query.addCriteria(Criteria.where("collection_name").is(name));
        Update update = new Update();
        update.inc("count", 1);
        FindAndModifyOptions options = FindAndModifyOptions.options()
                .returnNew(true)
                .upsert(true);
       SequenceGenerator sequenceGenerator =  mongoTemplate.findAndModify(
               query,update,options, SequenceGenerator.class);

        assert sequenceGenerator != null;
        return  sequenceGenerator.getCount();
    }
}
