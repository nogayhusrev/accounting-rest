package com.nogayhusrev.accounting_rest.service.impl;

import com.nogayhusrev.accounting_rest.config.KeycloakProperties;
import com.nogayhusrev.accounting_rest.dto.UserDto;
import com.nogayhusrev.accounting_rest.entity.User;
import com.nogayhusrev.accounting_rest.entity.common.UserPrincipal;
import com.nogayhusrev.accounting_rest.repository.UserRepository;
import com.nogayhusrev.accounting_rest.service.SecurityService;
import com.nogayhusrev.accounting_rest.service.UserService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.IDToken;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.List;

import static java.util.Arrays.asList;
import static org.keycloak.admin.client.CreatedResponseUtil.getCreatedId;


@Service
public class SecurityServiceImpl implements SecurityService {
    private final UserRepository userRepository;

    private final UserService userService;

    private final KeycloakProperties keycloakProperties;

    public SecurityServiceImpl(UserRepository userRepository, UserService userService, KeycloakProperties keycloakProperties) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.keycloakProperties = keycloakProperties;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException(username);

        return new UserPrincipal(user);

    }

    @Override
    public UserDto getCurrentUser() {

        KeycloakAuthenticationToken authentication =
                (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        Principal principal = (Principal) authentication.getPrincipal();

        KeycloakPrincipal<KeycloakSecurityContext> kPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) principal;
        IDToken token = kPrincipal.getKeycloakSecurityContext().getToken();


        return userService.findByName(token.getPreferredUsername());
    }

    @Override
    public Response userCreate(UserDto userDto) {

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setTemporary(false);
        credential.setValue(userDto.getPassword());

        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(userDto.getUsername());
        keycloakUser.setFirstName(userDto.getFirstname());
        keycloakUser.setLastName(userDto.getLastname());
        keycloakUser.setEmail(userDto.getUsername());
        keycloakUser.setCredentials(asList(credential));
        keycloakUser.setEmailVerified(true);
        keycloakUser.setEnabled(true);


        Keycloak keycloak = getKeycloakInstance();

        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realmResource.users();

        // Create Keycloak user
        Response result = usersResource.create(keycloakUser);

        String userId = getCreatedId(result);
        ClientRepresentation appClient = realmResource.clients()
                .findByClientId(keycloakProperties.getClientId()).get(0);

        RoleRepresentation userClientRole = realmResource.clients().get(appClient.getId()) //
                .roles().get(userDto.getRole().getDescription()).toRepresentation();

        realmResource.users().get(userId).roles().clientLevel(appClient.getId())
                .add(List.of(userClientRole));


        keycloak.close();
        return result;
    }

    @Override
    public void delete(String userName) {

        Keycloak keycloak = getKeycloakInstance();

        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realmResource.users();

        List<UserRepresentation> userRepresentations = usersResource.search(userName);
        String uid = userRepresentations.get(0).getId();
        usersResource.delete(uid);

        keycloak.close();
    }

    private Keycloak getKeycloakInstance() {
        return Keycloak.getInstance(keycloakProperties.getAuthServerUrl(),
                keycloakProperties.getMasterRealm(), keycloakProperties.getMasterUser()
                , keycloakProperties.getMasterUserPswd(), keycloakProperties.getMasterClient());
    }
}
