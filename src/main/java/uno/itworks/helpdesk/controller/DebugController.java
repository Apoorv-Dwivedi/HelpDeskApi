package uno.itworks.helpdesk.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DebugController {

    @GetMapping("/public/ping")
    String ping() { return "pong"; }

    @GetMapping("/me")
    Map<String, Object> me(Authentication authentication, @AuthenticationPrincipal OidcUser user) {
        return Map.of(
                "sub", user.getSubject(),
                "email", user.getEmail(),
                "authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).sorted().toList());
    }

    @GetMapping("/admin/ping")
    String adminPing() { return "admin ok"; }
}
