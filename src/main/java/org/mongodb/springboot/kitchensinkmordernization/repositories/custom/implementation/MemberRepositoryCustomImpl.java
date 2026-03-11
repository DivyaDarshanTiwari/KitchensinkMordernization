package org.mongodb.springboot.kitchensinkmordernization.repositories.custom.implementation;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.mongodb.springboot.kitchensinkmordernization.entites.Member;
import org.mongodb.springboot.kitchensinkmordernization.repositories.custom.MemberRepositoryCustom;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.FacetOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Member> findAll(
            String sortField,
            Sort.Direction direction,
            Object lastSortValue,
            Long lastId,
            int pageSize,
            int pageNumber,
            String searchQuery) {

        List<AggregationOperation> pipeline = new ArrayList<>();


        // ═══════════════════════════════════════
        // STAGE 1: $match for SEARCH (BEFORE $facet)
        // Filters ONCE, result goes into BOTH pipelines
        // ═══════════════════════════════════════
        if (searchQuery != null && !searchQuery.isBlank()) {
            Criteria searchCriteria = buildSearchCriteria(searchQuery);
            pipeline.add(Aggregation.match(searchCriteria));
        }


        // ═══════════════════════════════════════
        // STAGE 2: $facet (data + count in ONE call)
        // Only FILTERED documents enter here
        // ═══════════════════════════════════════

        // Data pipeline: sort → pagination → limit
        List<AggregationOperation> dataPipeline = new ArrayList<>();
        Sort sort = buildSort(sortField, direction);
        dataPipeline.add(Aggregation.sort(sort));

        if (lastSortValue != null && lastId != null) {
            Criteria paginationCriteria = buildPaginationCriteria(
                    sortField, direction, lastSortValue, lastId);
            dataPipeline.add(Aggregation.match(paginationCriteria));
        }

        dataPipeline.add(Aggregation.limit(pageSize));

        // Count pipeline: just count
        List<AggregationOperation> countPipeline = new ArrayList<>();
        countPipeline.add(Aggregation.count().as("total"));

        // Combine into $facet
        FacetOperation facet = Aggregation.facet()
                .and(dataPipeline.toArray(new AggregationOperation[0])).as("data")
                .and(countPipeline.toArray(new AggregationOperation[0])).as("count");

        pipeline.add(facet);


        // ═══════════════════════════════════════
        // EXECUTE (single DB call)
        // ═══════════════════════════════════════
        Aggregation aggregation = Aggregation.newAggregation(pipeline);

        AggregationResults<Document> results =
                mongoTemplate.aggregate(aggregation, "members", Document.class);

        Document document = results.getUniqueMappedResult();


        // ═══════════════════════════════════════
        // EXTRACT results
        // ═══════════════════════════════════════
        List<Member> members = extractMembers(document);
        long totalElements = extractCount(document);


        // ═══════════════════════════════════════
        // BUILD Page<Member>
        // ═══════════════════════════════════════
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return new PageImpl<>(members, pageable, totalElements);
    }


    // ═══════════════════════════════════════════════
    // EXTRACT: Members from facet result
    // ═══════════════════════════════════════════════
    private List<Member> extractMembers(Document document) {
        if (document == null) {
            return List.of();
        }

        List<Document> dataDocs = document.getList("data", Document.class);
        if (dataDocs == null || dataDocs.isEmpty()) {
            return List.of();
        }

        return dataDocs.stream()
                .map(doc -> mongoTemplate.getConverter().read(Member.class, doc))
                .toList();
    }


    // ═══════════════════════════════════════════════
    // EXTRACT: Count from facet result
    // ═══════════════════════════════════════════════
    private long extractCount(Document document) {
        if (document == null) {
            return 0;
        }

        List<Document> countDocs = document.getList("count", Document.class);
        if (countDocs == null || countDocs.isEmpty()) {
            return 0;
        }

        return countDocs.get(0).getInteger("total", 0);
    }


    // ═══════════════════════════════════════════════
    // SEARCH: Only by email
    // ═══════════════════════════════════════════════
    private Criteria buildSearchCriteria(String searchQuery) {
        String regex = Pattern.quote(searchQuery);
        return Criteria.where("email").regex(regex);
    }


    // ═══════════════════════════════════════════════
    // SORT
    // ═══════════════════════════════════════════════
    private Sort buildSort(String sortField, Sort.Direction direction) {
        if (sortField.equals("id")) {
            return Sort.by(direction, "_id");
        }
        return Sort.by(direction, sortField)
                .and(Sort.by(direction, "_id"));
    }


    // ═══════════════════════════════════════════════
    // PAGINATION CRITERIA
    // ═══════════════════════════════════════════════
    private Criteria buildPaginationCriteria(
            String sortField,
            Sort.Direction direction,
            Object lastSortValue,
            Long lastId) {

        if (direction == Sort.Direction.ASC) {
            return buildAscendingCriteria(sortField, lastSortValue, lastId);
        } else {
            return buildDescendingCriteria(sortField, lastSortValue, lastId);
        }
    }

    private Criteria buildAscendingCriteria(
            String sortField, Object lastSortValue, Long lastId) {

        if (sortField.equals("id")) {
            return Criteria.where("_id").gt(lastId);
        }

        return new Criteria().orOperator(
                Criteria.where(sortField).gt(lastSortValue),
                new Criteria().andOperator(
                        Criteria.where(sortField).is(lastSortValue),
                        Criteria.where("_id").gt(lastId)
                )
        );
    }

    private Criteria buildDescendingCriteria(
            String sortField, Object lastSortValue, Long lastId) {

        if (sortField.equals("id")) {
            return Criteria.where("_id").lt(lastId);
        }

        return new Criteria().orOperator(
                Criteria.where(sortField).lt(lastSortValue),
                new Criteria().andOperator(
                        Criteria.where(sortField).is(lastSortValue),
                        Criteria.where("_id").lt(lastId)
                )
        );
    }
}
