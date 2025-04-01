package com.supercart.repository;

import com.supercart.model.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<PackageEntity, String> {
}