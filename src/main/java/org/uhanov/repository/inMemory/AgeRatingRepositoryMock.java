package org.uhanov.repository.inMemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.uhanov.model.AgeRating;

@Repository
@RequiredArgsConstructor
public class AgeRatingRepositoryMock extends AbstractRepositoryMock<AgeRating> {

}
