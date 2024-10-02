package org.uhanov.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uhanov.repository.api.*;

@Component
public class TestedRepositoryHolder {
    @Autowired
    UserRepository userRepository;

    @Autowired
    StaffRepository staffRepository;
    @Autowired
    CreatorRepository creatorRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    AgeRatingRepository ageRatingRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PurchaseRepository purchaseRepository;
}
