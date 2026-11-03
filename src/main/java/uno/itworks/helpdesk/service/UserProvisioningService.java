package uno.itworks.helpdesk.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uno.itworks.helpdesk.domain.AppUser;
import uno.itworks.helpdesk.domain.Role;
import uno.itworks.helpdesk.repository.AppUserRepository;

@Service
@RequiredArgsConstructor
public class UserProvisioningService {

    private final AppUserRepository users;

    @Value("${app.bootstrap-admin-email:}")
    private String bootstrapAdminEmail;

    @Transactional
    public AppUser upsert(String sub, String email, String name) {
        return users.findByGoogleSub(sub)
                .map(existing -> {
                    existing.setEmail(email);   // profile data may change...
                    existing.setName(name);     // ...but never the role
                    return existing;
                })
                .orElseGet(() -> {
                    AppUser created = new AppUser();
                    created.setGoogleSub(sub);
                    created.setEmail(email);
                    created.setName(name);
                    if (email.equalsIgnoreCase(bootstrapAdminEmail)) {
                        created.setRole(Role.ADMIN);
                    }
                    return users.save(created);
                });
    }
}