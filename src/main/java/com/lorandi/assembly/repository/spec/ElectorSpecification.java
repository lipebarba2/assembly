package com.lorandi.assembly.repository.spec;

import com.lorandi.assembly.entity.Elector;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Optional;


@Builder
public class ElectorSpecification implements Specification<Elector> {
    @Builder.Default
    private final transient Optional<String> cpf = Optional.empty();


    @Override
    public Predicate toPredicate(Root<Elector> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        final var predicate = new ArrayList<Predicate>();
        cpf.ifPresent(s -> predicate.add(root.get("cpf").in(s)));
        criteriaQuery.distinct(true);
        return builder.and(predicate.toArray(new Predicate[0]));
    }
}
