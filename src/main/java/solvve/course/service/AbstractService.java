package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.repository.RepositoryHelper;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractService {

    @Autowired
    protected TranslationService translationService;

    @Autowired
    protected RepositoryHelper repositoryHelper;
}
