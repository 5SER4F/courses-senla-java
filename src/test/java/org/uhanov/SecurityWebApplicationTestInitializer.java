package org.uhanov;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.test.context.ActiveProfiles;

@Profile("test")
@ComponentScan("org.uhanov")
public class SecurityWebApplicationTestInitializer extends AbstractSecurityWebApplicationInitializer {


}
