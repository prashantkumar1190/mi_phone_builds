package com.blrpool.myapp.repository;

import com.blrpool.myapp.domain.Build;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Build entity.
 */
public interface BuildRepository extends JpaRepository<Build,Long> {

}
