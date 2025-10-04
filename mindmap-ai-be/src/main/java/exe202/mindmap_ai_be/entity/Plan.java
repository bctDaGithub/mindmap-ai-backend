package exe202.mindmap_ai_be.entity;

import exe202.mindmap_ai_be.entity.enums.UnitPrice;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "plans")
@Data
@RequiredArgsConstructor
public class Plan {
    @Id
    private Long planId;
    private String name;
    private String description;
    private Float price;
    private UnitPrice unitPrice;
    private Integer durationMonths;
}
