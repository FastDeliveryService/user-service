package com.fds.userservice.servise.identity;

import com.fds.userservice.dto.user.UserResponseWithPassword;
import com.fds.userservice.entity.UserEntity;
import com.fds.userservice.enums.Role;
import com.fds.userservice.exceptions.IdentityProviderException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KeycloakCommunicator implements IdentityProviderCommunicator {

    @Value("${keycloak.client-realm}")
    private String realmName;

    private final Keycloak keycloak;

    @Override
    public void createUser(UserResponseWithPassword customerToCreate) {
        UserRepresentation keycloakUser = prepareUserToRegistration(customerToCreate);

        try (Response response = keycloak.realm(realmName).users().create(keycloakUser)) {
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {

                try {
                    RoleRepresentation roleRepresentation = keycloak
                            .realm(realmName)
                            .roles()
                            .get(customerToCreate.role().toString().toLowerCase(Locale.ROOT))
                            .toRepresentation();

                    keycloak.realm(realmName)
                            .users()
                            .get(customerToCreate.id().toString())
                            .roles()
                            .realmLevel()
                            .add(Collections.singletonList(roleRepresentation));

                } catch (Exception e) {
                    throw new IdentityProviderException(String.format(
                            "User was created, but the role has not been assigned. " +
                                    "Error message: %s", e.getMessage()
                    ), e);
                }

            } else {
                String errorMessage = String.format("Keycloak response status code: %s %s",
                        response.getStatus(), response.readEntity(String.class));
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            throw new IdentityProviderException(String.format("An error occurred while trying to create a user. " +
                    "Error message: %s", e.getMessage()), e);
        }
    }


    @Override
    public void updateEmail(UUID userId, String email) {
        try {
            UserResource userResource = keycloak.realm(realmName).users().get(userId.toString());

            UserRepresentation user = userResource.toRepresentation();

            String oldEmail = user.getEmail();
            if (oldEmail.equals(email)) {
                return;
            }

            user.setEmail(email);
            user.setEmailVerified(false);

            userResource.update(user);

        } catch (Exception e) {
            throw new IdentityProviderException(String.format("The error occurred during attempt " +
                    "to update user (%s) email: (%s). Error-message: %s", userId.toString(), email, e.getMessage()), e);
        }
    }

    @Override
    public void updateRole(UUID userId, Role oldRole, Role newRole) {
        try {
            RoleRepresentation newRoleRepresentation = keycloak.realm(realmName).roles()
                    .get(newRole.toString().toLowerCase(Locale.ROOT)).toRepresentation();
            RoleRepresentation oldRoleRepresentation = keycloak.realm(realmName).roles()
                    .get(oldRole.toString().toLowerCase(Locale.ROOT)).toRepresentation();


            RoleScopeResource userRoleAccess = keycloak.realm(realmName).users()
                    .get(userId.toString()).roles().realmLevel();

            userRoleAccess.remove(Collections.singletonList(oldRoleRepresentation));
            userRoleAccess.add(Collections.singletonList(newRoleRepresentation));


        } catch (Exception e) {
            throw new IdentityProviderException(String.format("The error occurred during attempt " +
                    "to update user (%s) role: (%s). Error-message: %s",
                    userId.toString(),
                    newRole.toString(),
                    e.getMessage()), e);
        }
    }

    private static @NonNull UserRepresentation prepareUserToRegistration(UserResponseWithPassword customerToCreate) {
        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setId(customerToCreate.id().toString()); //todo not working
        keycloakUser.setUsername(customerToCreate.email());
        keycloakUser.setEmail(customerToCreate.email());
        keycloakUser.setEmailVerified(false);
        keycloakUser.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(customerToCreate.password());

        keycloakUser.setCredentials(List.of(credential));
        return keycloakUser;
    }
}
