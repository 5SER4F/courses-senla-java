package org.uhanov;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Profile("test")
@ComponentScan("org.uhanov")
public class SecurityWebApplicationTestInitializer extends AbstractSecurityWebApplicationInitializer {


}
