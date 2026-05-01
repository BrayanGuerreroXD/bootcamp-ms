package co.com.bootcamp.drivenadapters.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("bootcamp_capacities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BootcampCapacityEntity {
    @Id
    private Long id;

    @Column("bootcamp_id")
    private Long bootcampId;

    @Column("capacity_id")
    private Long capacityId;
}
