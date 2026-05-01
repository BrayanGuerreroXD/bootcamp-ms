package co.com.bootcamp.model.bootcamp;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class BootcampPeople {
    private Long id;
    private Bootcamp bootcamp;
    private String email;
}