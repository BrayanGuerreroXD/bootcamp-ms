package co.com.bootcamp.model.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PersonEvent {
    private String name;
    private String email;
}