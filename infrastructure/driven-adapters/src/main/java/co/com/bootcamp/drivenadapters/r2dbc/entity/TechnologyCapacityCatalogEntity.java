package co.com.bootcamp.drivenadapters.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("technology_capacity_catalogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TechnologyCapacityCatalogEntity {
    @Id
    private Long id;

    @Column("capacity_catalog_id")
    private Long capacityCatalogId;

    @Column("external_id")
    private Long externalId;

    @Column("name")
    private String name;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}