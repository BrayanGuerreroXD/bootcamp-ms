package co.com.bootcamp.usecase.signupbootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.BootcampPeople;
import co.com.bootcamp.model.bootcamp.gateways.BootcampPeopleRepository;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.exception.BadRequestException;
import co.com.bootcamp.model.exception.ConflictException;
import co.com.bootcamp.model.exception.ForbiddenException;
import co.com.bootcamp.model.exception.GlobalExceptionEnum;
import co.com.bootcamp.model.exception.NotFoundException;
import co.com.bootcamp.model.security.UserContext;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SignUpBootcampUseCase implements SignUpBootcampService {
    private final BootcampRepository bootcampRepository;
    private final BootcampPeopleRepository bootcampPeopleRepository;
    private final UserContext userContext;

    @Override
    public Mono<BootcampPeople> signUp(Long bootcampId, String email) {
        return userContext.currentUser()
                .flatMap(user -> {
                    if (Boolean.TRUE.equals(user.getIsAdmin())) {
                        return Mono.error(new ForbiddenException(GlobalExceptionEnum.FORBIDDEN_ACCESS));
                    }
                    String normalizedEmail = email.toLowerCase();
                    return bootcampPeopleRepository.countByEmail(normalizedEmail)
                            .flatMap(count -> {
                                if (count >= 5) {
                                    return Mono.error(new BadRequestException(
                                            GlobalExceptionEnum.MAX_BOOTCAMPS_ENROLLED));
                                }
                                return bootcampPeopleRepository.existsByBootcampIdAndEmail(bootcampId, normalizedEmail)
                                        .flatMap(exists -> {
                                            if (exists) {
                                                return Mono.error(new ConflictException(
                                                        GlobalExceptionEnum.ALREADY_ENROLLED));
                                            }
                                            return bootcampRepository.findById(bootcampId)
                                                    .switchIfEmpty(Mono.error(new NotFoundException(
                                                            GlobalExceptionEnum.NOT_FOUND)))
                                                    .flatMap(bootcamp -> {
                                                        BootcampPeople people = BootcampPeople.builder()
                                                                .bootcamp(bootcamp)
                                                                .email(normalizedEmail)
                                                                .build();
                                                        return bootcampPeopleRepository.save(people);
                                                    });
                                        });
                            });
                });
    }
}