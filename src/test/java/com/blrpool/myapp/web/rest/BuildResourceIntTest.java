package com.blrpool.myapp.web.rest;

import com.blrpool.myapp.Application;
import com.blrpool.myapp.domain.Build;
import com.blrpool.myapp.repository.BuildRepository;
import com.blrpool.myapp.repository.search.BuildSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the BuildResource REST controller.
 *
 * @see BuildResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BuildResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_DEVICE = "AAAAA";
    private static final String UPDATED_DEVICE = "BBBBB";
    private static final String DEFAULT_BUILD_TYPE = "AAAAA";
    private static final String UPDATED_BUILD_TYPE = "BBBBB";
    private static final String DEFAULT_BUILD_PATH = "AAAAA";
    private static final String UPDATED_BUILD_PATH = "BBBBB";
    private static final String DEFAULT_BUILD_NAME = "AAAAA";
    private static final String UPDATED_BUILD_NAME = "BBBBB";
    private static final String DEFAULT_SHA1 = "AAAAA";
    private static final String UPDATED_SHA1 = "BBBBB";

    private static final LocalDate DEFAULT_DT_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DT_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_TIME_ADDED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIME_ADDED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIME_ADDED_STR = dateTimeFormatter.format(DEFAULT_TIME_ADDED);

    @Inject
    private BuildRepository buildRepository;

    @Inject
    private BuildSearchRepository buildSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBuildMockMvc;

    private Build build;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BuildResource buildResource = new BuildResource();
        ReflectionTestUtils.setField(buildResource, "buildSearchRepository", buildSearchRepository);
        ReflectionTestUtils.setField(buildResource, "buildRepository", buildRepository);
        this.restBuildMockMvc = MockMvcBuilders.standaloneSetup(buildResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        build = new Build();
        build.setDevice(DEFAULT_DEVICE);
        build.setBuild_type(DEFAULT_BUILD_TYPE);
        build.setBuild_path(DEFAULT_BUILD_PATH);
        build.setBuild_name(DEFAULT_BUILD_NAME);
        build.setSha1(DEFAULT_SHA1);
        build.setDt_added(DEFAULT_DT_ADDED);
        build.setTime_added(DEFAULT_TIME_ADDED);
    }

    @Test
    @Transactional
    public void createBuild() throws Exception {
        int databaseSizeBeforeCreate = buildRepository.findAll().size();

        // Create the Build

        restBuildMockMvc.perform(post("/api/builds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(build)))
                .andExpect(status().isCreated());

        // Validate the Build in the database
        List<Build> builds = buildRepository.findAll();
        assertThat(builds).hasSize(databaseSizeBeforeCreate + 1);
        Build testBuild = builds.get(builds.size() - 1);
        assertThat(testBuild.getDevice()).isEqualTo(DEFAULT_DEVICE);
        assertThat(testBuild.getBuild_type()).isEqualTo(DEFAULT_BUILD_TYPE);
        assertThat(testBuild.getBuild_path()).isEqualTo(DEFAULT_BUILD_PATH);
        assertThat(testBuild.getBuild_name()).isEqualTo(DEFAULT_BUILD_NAME);
        assertThat(testBuild.getSha1()).isEqualTo(DEFAULT_SHA1);
        assertThat(testBuild.getDt_added()).isEqualTo(DEFAULT_DT_ADDED);
        assertThat(testBuild.getTime_added()).isEqualTo(DEFAULT_TIME_ADDED);
    }

    @Test
    @Transactional
    public void checkDeviceIsRequired() throws Exception {
        int databaseSizeBeforeTest = buildRepository.findAll().size();
        // set the field null
        build.setDevice(null);

        // Create the Build, which fails.

        restBuildMockMvc.perform(post("/api/builds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(build)))
                .andExpect(status().isBadRequest());

        List<Build> builds = buildRepository.findAll();
        assertThat(builds).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBuild_typeIsRequired() throws Exception {
        int databaseSizeBeforeTest = buildRepository.findAll().size();
        // set the field null
        build.setBuild_type(null);

        // Create the Build, which fails.

        restBuildMockMvc.perform(post("/api/builds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(build)))
                .andExpect(status().isBadRequest());

        List<Build> builds = buildRepository.findAll();
        assertThat(builds).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBuild_pathIsRequired() throws Exception {
        int databaseSizeBeforeTest = buildRepository.findAll().size();
        // set the field null
        build.setBuild_path(null);

        // Create the Build, which fails.

        restBuildMockMvc.perform(post("/api/builds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(build)))
                .andExpect(status().isBadRequest());

        List<Build> builds = buildRepository.findAll();
        assertThat(builds).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBuild_nameIsRequired() throws Exception {
        int databaseSizeBeforeTest = buildRepository.findAll().size();
        // set the field null
        build.setBuild_name(null);

        // Create the Build, which fails.

        restBuildMockMvc.perform(post("/api/builds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(build)))
                .andExpect(status().isBadRequest());

        List<Build> builds = buildRepository.findAll();
        assertThat(builds).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSha1IsRequired() throws Exception {
        int databaseSizeBeforeTest = buildRepository.findAll().size();
        // set the field null
        build.setSha1(null);

        // Create the Build, which fails.

        restBuildMockMvc.perform(post("/api/builds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(build)))
                .andExpect(status().isBadRequest());

        List<Build> builds = buildRepository.findAll();
        assertThat(builds).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDt_addedIsRequired() throws Exception {
        int databaseSizeBeforeTest = buildRepository.findAll().size();
        // set the field null
        build.setDt_added(null);

        // Create the Build, which fails.

        restBuildMockMvc.perform(post("/api/builds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(build)))
                .andExpect(status().isBadRequest());

        List<Build> builds = buildRepository.findAll();
        assertThat(builds).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBuilds() throws Exception {
        // Initialize the database
        buildRepository.saveAndFlush(build);

        // Get all the builds
        restBuildMockMvc.perform(get("/api/builds?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(build.getId().intValue())))
                .andExpect(jsonPath("$.[*].device").value(hasItem(DEFAULT_DEVICE.toString())))
                .andExpect(jsonPath("$.[*].build_type").value(hasItem(DEFAULT_BUILD_TYPE.toString())))
                .andExpect(jsonPath("$.[*].build_path").value(hasItem(DEFAULT_BUILD_PATH.toString())))
                .andExpect(jsonPath("$.[*].build_name").value(hasItem(DEFAULT_BUILD_NAME.toString())))
                .andExpect(jsonPath("$.[*].sha1").value(hasItem(DEFAULT_SHA1.toString())))
                .andExpect(jsonPath("$.[*].dt_added").value(hasItem(DEFAULT_DT_ADDED.toString())))
                .andExpect(jsonPath("$.[*].time_added").value(hasItem(DEFAULT_TIME_ADDED_STR)));
    }

    @Test
    @Transactional
    public void getBuild() throws Exception {
        // Initialize the database
        buildRepository.saveAndFlush(build);

        // Get the build
        restBuildMockMvc.perform(get("/api/builds/{id}", build.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(build.getId().intValue()))
            .andExpect(jsonPath("$.device").value(DEFAULT_DEVICE.toString()))
            .andExpect(jsonPath("$.build_type").value(DEFAULT_BUILD_TYPE.toString()))
            .andExpect(jsonPath("$.build_path").value(DEFAULT_BUILD_PATH.toString()))
            .andExpect(jsonPath("$.build_name").value(DEFAULT_BUILD_NAME.toString()))
            .andExpect(jsonPath("$.sha1").value(DEFAULT_SHA1.toString()))
            .andExpect(jsonPath("$.dt_added").value(DEFAULT_DT_ADDED.toString()))
            .andExpect(jsonPath("$.time_added").value(DEFAULT_TIME_ADDED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingBuild() throws Exception {
        // Get the build
        restBuildMockMvc.perform(get("/api/builds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBuild() throws Exception {
        // Initialize the database
        buildRepository.saveAndFlush(build);

		int databaseSizeBeforeUpdate = buildRepository.findAll().size();

        // Update the build
        build.setDevice(UPDATED_DEVICE);
        build.setBuild_type(UPDATED_BUILD_TYPE);
        build.setBuild_path(UPDATED_BUILD_PATH);
        build.setBuild_name(UPDATED_BUILD_NAME);
        build.setSha1(UPDATED_SHA1);
        build.setDt_added(UPDATED_DT_ADDED);
        build.setTime_added(UPDATED_TIME_ADDED);

        restBuildMockMvc.perform(put("/api/builds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(build)))
                .andExpect(status().isOk());

        // Validate the Build in the database
        List<Build> builds = buildRepository.findAll();
        assertThat(builds).hasSize(databaseSizeBeforeUpdate);
        Build testBuild = builds.get(builds.size() - 1);
        assertThat(testBuild.getDevice()).isEqualTo(UPDATED_DEVICE);
        assertThat(testBuild.getBuild_type()).isEqualTo(UPDATED_BUILD_TYPE);
        assertThat(testBuild.getBuild_path()).isEqualTo(UPDATED_BUILD_PATH);
        assertThat(testBuild.getBuild_name()).isEqualTo(UPDATED_BUILD_NAME);
        assertThat(testBuild.getSha1()).isEqualTo(UPDATED_SHA1);
        assertThat(testBuild.getDt_added()).isEqualTo(UPDATED_DT_ADDED);
        assertThat(testBuild.getTime_added()).isEqualTo(UPDATED_TIME_ADDED);
    }

    @Test
    @Transactional
    public void deleteBuild() throws Exception {
        // Initialize the database
        buildRepository.saveAndFlush(build);

		int databaseSizeBeforeDelete = buildRepository.findAll().size();

        // Get the build
        restBuildMockMvc.perform(delete("/api/builds/{id}", build.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Build> builds = buildRepository.findAll();
        assertThat(builds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
