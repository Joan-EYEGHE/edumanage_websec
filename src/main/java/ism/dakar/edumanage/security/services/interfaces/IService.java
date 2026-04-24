package ism.dakar.edumanage.security.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IService<D> {
    D create(D dto);
    D update(D dto);
    void delete(Long id);
    D get(Long id);
    Page<D> getAll(Map<String, String> searchParams, Pageable pageable);
    List<D> getAll(Map<String, String> searchParams);
    long countAll(Map<String, String> searchParams);
}
