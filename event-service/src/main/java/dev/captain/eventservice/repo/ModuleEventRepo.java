package dev.captain.eventservice.repo;

import dev.captain.eventservice.model.ModuleEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ModuleEventRepo extends MongoRepository<ModuleEvent, String> {
    List<ModuleEvent> findAllByModuleId(Long moduleId);

    List<ModuleEvent> findAllByModuleIdIn(List<Long> moduleIds);
}
