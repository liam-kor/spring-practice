package com.example.circle.domain.repository;

import com.example.circle.domain.entity.CircleMember;
import com.example.circle.domain.entity.QCircleMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.circle.domain.entity.QCircleMember.circleMember;

@Repository
@RequiredArgsConstructor
public class CircleMemberCustomRepository {
    private final EntityManager em;

    public List<CircleMember> findCircleMembers(Long circleId) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.select(circleMember)
                .from(circleMember)
                .join(circleMember.circle)
                .join(circleMember.member).fetchJoin()
                .where(circleMember.circle.id.eq(circleId))
                .createQuery().getResultList();
//        return em.createQuery("select cm from CircleMember cm join fetch cm.member m " +
//                        "join cm.circle c " +
//                        "where c.id = :circleId", CircleMember.class)
//                .setParameter("circleId", circleId)
//                .setMaxResults(1000) // 최대 1000건
//                .getResultList();
    }
}
