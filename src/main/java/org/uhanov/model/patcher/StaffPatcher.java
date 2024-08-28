package org.uhanov.model.patcher;

import org.springframework.stereotype.Component;
import org.uhanov.dto.StaffAuthDTO;
import org.uhanov.model.Staff;

@Component
public class StaffPatcher extends EntityPatcher<Staff, StaffAuthDTO> {
}
