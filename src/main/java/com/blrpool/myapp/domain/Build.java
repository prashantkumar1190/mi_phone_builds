package com.blrpool.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Build.
 */
@Entity
@Table(name = "build")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "build")
public class Build implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "device", nullable = false)
    private String device;

    @NotNull
    @Column(name = "build_type", nullable = false)
    private String build_type;

    @NotNull
    @Column(name = "build_path", nullable = false)
    private String build_path;

    @NotNull
    @Column(name = "build_name", nullable = false)
    private String build_name;

    @NotNull
    @Column(name = "sha1", nullable = false)
    private String sha1;

    @NotNull
    @Column(name = "dt_added", nullable = false)
    private LocalDate dt_added;

    @Column(name = "time_added")
    private ZonedDateTime time_added;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getBuild_type() {
        return build_type;
    }

    public void setBuild_type(String build_type) {
        this.build_type = build_type;
    }

    public String getBuild_path() {
        return build_path;
    }

    public void setBuild_path(String build_path) {
        this.build_path = build_path;
    }

    public String getBuild_name() {
        return build_name;
    }

    public void setBuild_name(String build_name) {
        this.build_name = build_name;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public LocalDate getDt_added() {
        return dt_added;
    }

    public void setDt_added(LocalDate dt_added) {
        this.dt_added = dt_added;
    }

    public ZonedDateTime getTime_added() {
        return time_added;
    }

    public void setTime_added(ZonedDateTime time_added) {
        this.time_added = time_added;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Build build = (Build) o;
        return Objects.equals(id, build.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Build{" +
            "id=" + id +
            ", device='" + device + "'" +
            ", build_type='" + build_type + "'" +
            ", build_path='" + build_path + "'" +
            ", build_name='" + build_name + "'" +
            ", sha1='" + sha1 + "'" +
            ", dt_added='" + dt_added + "'" +
            ", time_added='" + time_added + "'" +
            '}';
    }
}
