package repository.memory;

import domain.Entity;
import domain.validators.Validator;
import repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private final Validator<E> validator;
    private final Map<ID, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID, E>();
    }

    @Override
    public E save(E newEntity) {
        if(newEntity == null)
        {
            throw new IllegalArgumentException();
        }

        validator.validate(newEntity);

        for(E entity: findAll())
        {
            if(entity.equals(newEntity))
                return entity;
        }

        entities.put(newEntity.getId(), newEntity);
        return null;
    }

    @Override
    public E findOne(ID entityID) {
        if(entityID == null)
            throw new IllegalArgumentException();

        return entities.get(entityID);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E update(E newEntity) {
        if(newEntity == null)
            throw new IllegalArgumentException();

        validator.validate(newEntity);

        //entities.put(newEntity.getId(), newEntity); TODO: de ce e nevoie de linia asta? Nu strica tot?

        if(entities.get(newEntity.getId()) != null) {
            entities.put(newEntity.getId(), newEntity);
            return null;
        }
        return newEntity;

    }

    @Override
    public E delete(ID id) {
        if(id == null)
            throw new IllegalArgumentException();
        if(entities.get(id) == null)
        {
            return null;
        }
        return entities.remove(id);
    }


}
