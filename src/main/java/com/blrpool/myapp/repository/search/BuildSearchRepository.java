package com.blrpool.myapp.repository.search;

import com.blrpool.myapp.domain.Build;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Build entity.
 */
public interface BuildSearchRepository extends ElasticsearchRepository<Build, Long> {
}
