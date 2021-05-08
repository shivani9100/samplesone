package com.Inventory.Project.AssectService.RequestAsset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestAssetLocationDetailsRepository extends JpaRepository<RequestAssetLocationDetailsModel, Integer> {

}
