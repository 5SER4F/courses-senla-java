package org.uhanov.model.patcher;

import org.springframework.stereotype.Component;
import org.uhanov.dto.UserAuthDTO;
import org.uhanov.model.User;

@Component
public class UserPatcher extends EntityPatcher<User, UserAuthDTO> {
}
