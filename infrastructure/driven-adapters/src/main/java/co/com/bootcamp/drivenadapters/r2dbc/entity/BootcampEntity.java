package co.com.bootcamp.drivenadapters.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("bootcamps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BootcampEntity {
    @Id
    private Long id;

    private String name;

    private String description;

    @Column("init_time")
    private LocalDateTime initTime;

    private Integer duration;

    @Column("capacity_count")
    private Integer capacityCount;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
