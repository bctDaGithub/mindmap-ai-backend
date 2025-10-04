package exe202.mindmap_ai_be.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table(name = "subscriptions")
@Data
@RequiredArgsConstructor
public class Subscription {
    @Id
    private Long subscriptionId;
    private Long userId;
    private Long planId;
    private Timestamp startDate;
    private Timestamp endDate;
    private String status;
}
