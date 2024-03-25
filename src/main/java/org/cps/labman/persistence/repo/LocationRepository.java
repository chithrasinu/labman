package org.cps.labman.persistence.repo;

import org.cps.labman.persistence.model.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location,Long> {
}
