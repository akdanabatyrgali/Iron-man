package edu.stark.web;

import edu.stark.core.GameSnapshot;
import edu.stark.persistence.MissionRecord;
import java.util.List;

public record DashboardResponse(
        GameSnapshot state,
        List<MissionRecord> history
) {
}
