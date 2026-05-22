package com.vlad.scooterrental.presentation.user;

import com.vlad.scooterrental.application.auth.UserView;
import com.vlad.scooterrental.application.user.command.CreateUserCommand;
import com.vlad.scooterrental.application.user.command.CreateUserCommandHandler;
import com.vlad.scooterrental.application.user.command.DeleteUserCommand;
import com.vlad.scooterrental.application.user.command.DeleteUserCommandHandler;
import com.vlad.scooterrental.application.user.command.UpdateUserCommand;
import com.vlad.scooterrental.application.user.command.UpdateUserCommandHandler;
import com.vlad.scooterrental.application.user.query.GetCurrentUserQueryHandler;
import com.vlad.scooterrental.application.user.query.GetUserByIdQueryHandler;
import com.vlad.scooterrental.application.user.query.ListUsersQueryHandler;
import com.vlad.scooterrental.presentation.dto.SecurityUtils;
import com.vlad.scooterrental.presentation.dto.UserCreateRequest;
import com.vlad.scooterrental.presentation.dto.UserUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CreateUserCommandHandler createUserCommandHandler;
    private final UpdateUserCommandHandler updateUserCommandHandler;
    private final DeleteUserCommandHandler deleteUserCommandHandler;
    private final GetCurrentUserQueryHandler getCurrentUserQueryHandler;
    private final GetUserByIdQueryHandler getUserByIdQueryHandler;
    private final ListUsersQueryHandler listUsersQueryHandler;

    public UserController(
            CreateUserCommandHandler createUserCommandHandler,
            UpdateUserCommandHandler updateUserCommandHandler,
            DeleteUserCommandHandler deleteUserCommandHandler,
            GetCurrentUserQueryHandler getCurrentUserQueryHandler,
            GetUserByIdQueryHandler getUserByIdQueryHandler,
            ListUsersQueryHandler listUsersQueryHandler
    ) {
        this.createUserCommandHandler = createUserCommandHandler;
        this.updateUserCommandHandler = updateUserCommandHandler;
        this.deleteUserCommandHandler = deleteUserCommandHandler;
        this.getCurrentUserQueryHandler = getCurrentUserQueryHandler;
        this.getUserByIdQueryHandler = getUserByIdQueryHandler;
        this.listUsersQueryHandler = listUsersQueryHandler;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserView> create(@Valid @RequestBody UserCreateRequest request) {
        UserView createdUser = createUserCommandHandler.handle(
                CreateUserCommand.of(request.fullName(), request.email(), request.password(), request.role())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserView> findAll() {
        return listUsersQueryHandler.handle();
    }

    @GetMapping("/me")
    public UserView currentUser(Authentication authentication) {
        return getCurrentUserQueryHandler.handle(SecurityUtils.userId(authentication));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserView findById(@PathVariable UUID userId) {
        return getUserByIdQueryHandler.handle(userId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserView update(@PathVariable UUID userId, @Valid @RequestBody UserUpdateRequest request) {
        return updateUserCommandHandler.handle(
                UpdateUserCommand.of(userId, request.fullName(), request.email(), request.password(), request.role())
        );
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        deleteUserCommandHandler.handle(new DeleteUserCommand(userId));
        return ResponseEntity.noContent().build();
    }
}
