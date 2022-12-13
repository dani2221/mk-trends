package mk.trends.webapp.dtos.mappers;

public interface Mapper<T, V, W> {
    T toEntity(V dto);
    W toDto(T entity);
}
