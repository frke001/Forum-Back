package org.unibl.etf.forumback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.forumback.models.entities.SIEMEntity;

public interface SIEMRepository extends JpaRepository<SIEMEntity,Long> {
}
