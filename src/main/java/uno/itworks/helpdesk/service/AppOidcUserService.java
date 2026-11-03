package uno.itworks.helpdesk.service;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import uno.itworks.helpdesk.domain.AppUser;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AppOidcUserService implements OAuth2UserService<OidcUserRequest,OidcUser> {


    private final UserProvisioningService userProvisioningService;
    private final OidcUserService delegate = new OidcUserService();


    @Override
    public @Nullable OidcUser loadUser(OidcUserRequest request) throws OAuth2AuthenticationException {

        OidcUser oidcUser = delegate.loadUser(request);

        if(!Boolean.TRUE.equals(oidcUser.getEmailVerified())){

            throw new OAuth2AuthenticationException
                    (new OAuth2Error("email_not_verified"),"Google email is not verified");
        }


        AppUser user = userProvisioningService.upsert(oidcUser.getSubject(),oidcUser.getEmail(),oidcUser.getFullName());
        Set<GrantedAuthority> authorities = new HashSet<>(oidcUser.getAuthorities());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));


        return  new DefaultOidcUser(authorities,oidcUser.getIdToken(),oidcUser.getUserInfo(),"sub");
    }
}