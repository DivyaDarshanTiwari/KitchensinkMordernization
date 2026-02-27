package org.mongodb.springboot.kitchensinkmordernization.repositories;

import org.mongodb.springboot.kitchensinkmordernization.entites.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends MongoRepository<Member, Long> {
    UserDetails findMemberByName(String name);
}
