package com.example.nhom1.respository;

import com.example.nhom1.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    List<Permission> findByPermissionNameContainingIgnoreCaseAndDeletedAtIsNull(String name);

    List<Permission> findByPermissionCodeContainingIgnoreCaseAndDeletedAtIsNull(String code);

    List<Permission> findByModuleAndDeletedAtIsNull(String module);

    List<Permission> findByIsActiveAndDeletedAtIsNull(Boolean isActive);

    List<Permission> findByDeletedAtIsNull();

    Optional<Permission> findByPermissionCodeAndDeletedAtIsNull(String permissionCode);

    Optional<Permission> findByIdAndDeletedAtIsNull(UUID id);

    Page<Permission> findByDeletedAtIsNull(Pageable pageable);

}
