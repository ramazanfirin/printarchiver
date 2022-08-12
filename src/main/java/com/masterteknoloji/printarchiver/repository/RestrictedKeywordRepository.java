package com.masterteknoloji.printarchiver.repository;

import com.masterteknoloji.printarchiver.domain.RestrictedKeyword;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RestrictedKeyword entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestrictedKeywordRepository extends JpaRepository<RestrictedKeyword, Long> {

}
