package edu.stark.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/** A completed mission stored in a persistent H2 database. */
@Entity
@Table(name = "mission_records")
public class MissionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String missionName;

    @Column(nullable = false)
    private String suitName;

    @Column(nullable = false)
    private boolean success;

    @Column(nullable = false)
    private Instant completedAt;

    protected MissionRecord() { }

    public MissionRecord(String missionName, String suitName, boolean success, Instant completedAt) {
        this.missionName = missionName;
        this.suitName = suitName;
        this.success = success;
        this.completedAt = completedAt;
    }

    public Long getId() { return id; }
    public String getMissionName() { return missionName; }
    public String getSuitName() { return suitName; }
    public boolean isSuccess() { return success; }
    public Instant getCompletedAt() { return completedAt; }
}
