CREATE TABLE bootcamp_capacities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bootcamp_id BIGINT NOT NULL,
    capacity_id BIGINT NOT NULL,
    CONSTRAINT fk_bootcamp_capacities_bootcamp FOREIGN KEY (bootcamp_id) REFERENCES bootcamps(id) ON DELETE CASCADE,
    CONSTRAINT fk_bootcamp_capacities_capacity FOREIGN KEY (capacity_id) REFERENCES capacity_catalogs(id),
    UNIQUE KEY uk_bootcamp_capacity (bootcamp_id, capacity_id)
);