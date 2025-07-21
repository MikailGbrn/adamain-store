package com.adamain.store.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    private final String principalAttribute = "preferred_username"; // extract variable to yaml

    private final String resourceId = "adamain-app"; // extract variable to yaml

    /**
     * method to convert Roles from default role to match
     * preauthorize role requirements
     *
     * @param source
     * @return converted authentication token with converted Roles
     */
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        // concat string 'ROLES_' with role that exists in keycloak
        // e.g. 'ROLES_' + 'adamain_admin'
        // expected to be 'ROLES_adamin_admin
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(source).stream(),
                extractResourceRoles(source).stream()
        )
                .collect(Collectors.toSet());

        return new JwtAuthenticationToken(
                source,
                authorities,
                getPrincipleClaimName(source)
        );
    }

    private String getPrincipleClaimName(Jwt source) {
        String claimName = JwtClaimNames.SUB;

        if (Objects.nonNull(principalAttribute)) {
            claimName = principalAttribute;
        }

        return  source.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt source) {
        // can find these resources from token payload
        // extract bearer token to jwt.io for more details of the payload
        Map<String, Object> resourceAccess;
        Map<String, Object> resource;
        Collection<String> resourceRoles;

        // attempt to read payload's json resource_access field
        if (Objects.isNull(source.getClaim("resource_access"))) {
            return Set.of();
        }
        resourceAccess = source.getClaim("resource_access");

        // attempt to read payload's json resource_id field
        // which is the client id from keycloak
        if (Objects.isNull(resourceAccess.get(resourceId))) {
            return Set.of();
        }
        resource = (Map<String, Object>) resourceAccess.get(resourceId);

        // get roles from client role and concat with 'ROLE_'
        resourceRoles = (Collection<String>) resource.get("roles");
        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }


}
