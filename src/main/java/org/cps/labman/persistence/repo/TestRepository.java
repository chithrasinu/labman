package org.cps.labman.persistence.repo;

import org.cps.labman.persistence.model.Test;
import org.springframework.data.repository.CrudRepository;

public interface TestRepository extends CrudRepository<Test,Long> {
}
