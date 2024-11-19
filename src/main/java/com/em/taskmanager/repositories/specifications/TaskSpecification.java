package com.em.taskmanager.repositories.specifications;

import com.em.taskmanager.dtos.TaskFilterDto;
import com.em.taskmanager.entities.task.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class TaskSpecification {
    public static Specification<Task> createSpecification(TaskFilterDto filterDto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getAuthorId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("author").get("id"), filterDto.getAuthorId()));
            }
            if (filterDto.getAssigneeId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("assignee").get("id"), filterDto.getAssigneeId()));
            }
            if (filterDto.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filterDto.getStatus()));
            }
            if (filterDto.getPriority() != null) {
                predicates.add(criteriaBuilder.equal(root.get("taskPriority"), filterDto.getPriority()));
            }
            if (filterDto.getSearchQuery() != null) {
                Predicate titlePredicate = criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("title")), "%" + filterDto.getSearchQuery().toLowerCase() + "%");
                Predicate descriptionPredicate = criteriaBuilder
                        .like(criteriaBuilder.lower(root.get("description")), "%" + filterDto.getSearchQuery().toLowerCase() + "%");
                predicates.add(criteriaBuilder.or(titlePredicate, descriptionPredicate));
            }
            if (filterDto.getCreatedFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("created"), filterDto.getCreatedFrom()));
            }
            if (filterDto.getCreatedTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("created"), filterDto.getCreatedTo()));
            }
            if (filterDto.getUpdatedFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("updated"), filterDto.getUpdatedFrom()));
            }
            if (filterDto.getUpdatedTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("updated"), filterDto.getUpdatedTo()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
