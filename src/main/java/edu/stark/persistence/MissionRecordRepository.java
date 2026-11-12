package edu.stark.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data JPA generates the SQL persistence implementation. */
public interface MissionRecordRepository extends JpaRepository<MissionRecord, Long> {
    List<MissionRecord> findAllByOrderByIdDesc();
}
