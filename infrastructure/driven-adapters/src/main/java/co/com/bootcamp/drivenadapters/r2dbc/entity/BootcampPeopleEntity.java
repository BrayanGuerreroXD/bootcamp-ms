package co.com.bootcamp.drivenadapters.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("bootcamp_people")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BootcampPeopleEntity {
    @Id
    private Long id;

    @Column("bootcamp_id")
    private Long bootcampId;

    private String email;

    private String name;

    @Column("created_at")
    private LocalDateTime createdAt;
}
