package com.blrpool.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.blrpool.myapp.domain.Build;
import com.blrpool.myapp.repository.BuildRepository;
import com.blrpool.myapp.repository.search.BuildSearchRepository;
import com.blrpool.myapp.web.rest.util.HeaderUtil;
import com.blrpool.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Build.
 */
@RestController
@RequestMapping("/api")
public class BuildResource {

    private final Logger log = LoggerFactory.getLogger(BuildResource.class);
        
    @Inject
    private BuildRepository buildRepository;
    
    @Inject
    private BuildSearchRepository buildSearchRepository;
    
    /**
     * POST  /builds -> Create a new build.
     */
    @RequestMapping(value = "/builds",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Build> createBuild(@Valid @RequestBody Build build) throws URISyntaxException {
        log.debug("REST request to save Build : {}", build);
        if (build.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("build", "idexists", "A new build cannot already have an ID")).body(null);
        }
        Build result = buildRepository.save(build);
        buildSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/builds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("build", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /builds -> Updates an existing build.
     */
    @RequestMapping(value = "/builds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Build> updateBuild(@Valid @RequestBody Build build) throws URISyntaxException {
        log.debug("REST request to update Build : {}", build);
        if (build.getId() == null) {
            return createBuild(build);
        }
        Build result = buildRepository.save(build);
        buildSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("build", build.getId().toString()))
            .body(result);
    }

    /**
     * GET  /builds -> get all the builds.
     */
    @RequestMapping(value = "/builds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Build>> getAllBuilds(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Builds");
        Page<Build> page = buildRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/builds");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /builds/:id -> get the "id" build.
     */
    @RequestMapping(value = "/builds/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Build> getBuild(@PathVariable Long id) {
        log.debug("REST request to get Build : {}", id);
        Build build = buildRepository.findOne(id);
        return Optional.ofNullable(build)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /builds/:id -> delete the "id" build.
     */
    @RequestMapping(value = "/builds/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBuild(@PathVariable Long id) {
        log.debug("REST request to delete Build : {}", id);
        buildRepository.delete(id);
        buildSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("build", id.toString())).build();
    }

    /**
     * SEARCH  /_search/builds/:query -> search for the build corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/builds/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Build> searchBuilds(@PathVariable String query) {
        log.debug("REST request to search Builds for query {}", query);
        return StreamSupport
            .stream(buildSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
