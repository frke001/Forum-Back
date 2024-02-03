package org.unibl.etf.forumback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.forumback.models.entities.BlacklistEntity;

@Repository
public interface BlacklistRepository extends JpaRepository<BlacklistEntity, Long> {

    boolean existsByToken(String token);
}
