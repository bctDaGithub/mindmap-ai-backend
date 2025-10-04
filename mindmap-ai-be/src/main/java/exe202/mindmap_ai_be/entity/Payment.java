package exe202.mindmap_ai_be.entity;

import exe202.mindmap_ai_be.entity.enums.PaymentMethod;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table(name = "payments")
@Data
@RequiredArgsConstructor
public class Payment {
    private Long paymentId;
    private Long subscriptionId;
    private Float amount;
    private PaymentMethod paymentMethod;
    private String transactionCode;
    private Timestamp paidAt;
}
