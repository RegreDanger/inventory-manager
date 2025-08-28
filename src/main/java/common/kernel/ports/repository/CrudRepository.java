package common.kernel.ports.repository;

import java.util.Optional;
import java.util.List;

public interface CrudRepository<T, I> {
    public I create(T entity);

    public Optional<T> findById(I id);

    public Optional<T> findByName(String name);

    public List<T> findAll();

    public boolean update(T entity);

    public boolean delete(I id);
    
}
