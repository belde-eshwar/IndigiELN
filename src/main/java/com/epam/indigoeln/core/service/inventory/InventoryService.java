/*
 *  Copyright (C) 2015-2018 EPAM Systems
 *
 *  This file is part of Indigo ELN.
 *
 *  Indigo ELN is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Indigo ELN is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Indigo ELN.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epam.indigoeln.core.service.inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.indigo.IndigoException;
import com.epam.indigoeln.core.model.RawMaterial;
import com.epam.indigoeln.core.model.User;
import com.epam.indigoeln.core.repository.inventory.InventoryRepository;
import com.epam.indigoeln.web.rest.dto.RawMaterialDTO;
import com.epam.indigoeln.web.rest.util.CustomDtoMapper;

/**
 * Provides a number of methods for access to inventory's data in database.
 */
@Service
public class InventoryService {

    /**
     * Instance of InventoryRepository for access to chemicals in database.
     */
    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * Instance of CustomDtoMapper for conversion from dto object.
     */
    @Autowired
    private CustomDtoMapper mapper;

    public List<RawMaterial> getBatchesByRawMaterialNameAndQuantityGreaterThanZero(String rawMaterialName) {
	        return inventoryRepository.findByRawMaterialNameAndQuantityGreaterThanZero(rawMaterialName);
       }
    

    public RawMaterial getRawMaterialByrawMaterialNameAndBatchId(String rawMaterialName,String batchId) {
	        return inventoryRepository.findByRawMaterialNameAndBatchId(rawMaterialName,batchId);
       }

   
/*    *//**
     * Returns Chemical by id according to permissions.
     *
     * @param id   Chemical's identifier
     * @param RawMaterial chemical
     * @return Chemical by id
     *//*
    public RawMaterial getChemicalById(String id) {
	return inventoryRepository.findOne(id);
    }*/

/*    public RawMaterialDTO rawMaterialPurchase(RawMaterialDTO rawMaterialDTO,User user) {
		RawMaterial rawMaterial = mapper.convertFromDTO(rawMaterialDTO);
		
		try {
		    rawMaterial = inventoryRepository.save(rawMaterial);
		} catch(Exception e) {
		    throw new IndigoException(rawMaterial, e.getMessage());
		}
		return new RawMaterialDTO(rawMaterial);
    }*/


/*    public RawMaterialDTO rawMaterialConsumption(RawMaterialDTO rawMaterialDTO, User user) {
	RawMaterial rawMaterial = mapper.convertFromDTO(rawMaterialDTO);
        RawMaterial rawMaterialFromDb = Optional.ofNullable(inventoryRepository.findByIdAndByVendorAndByBatchId(rawMaterial.getId(),rawMaterial.getVendor(),rawMaterial.getBatchId()))
                .orElseThrow(() -> EntityNotFoundException.createWithProjectId(rawMaterial.getId()));

        if (!PermissionUtil.hasEditorAuthorityOrPermissions(user, rawMaterialFromDb.getAccessList(),
                UserPermission.UPDATE_ENTITY)) {
            throw OperationDeniedException.createProjectUpdateOperation(rawMaterialFromDb.getId());
        }

        rawMaterialFromDb.setRawMaterialName(rawMaterial.getRawMaterialName());
        rawMaterialFromDb.setQuanitity(rawMaterialFromDb.getQuanitity() - rawMaterial.getQuanitity());
        rawMaterialFromDb.setUnits(rawMaterial.getUnits());
        rawMaterialFromDb.setVendor(rawMaterial.getVendor());
        rawMaterialFromDb.setBatchId(rawMaterial.getBatchId());
        rawMaterialFromDb.setStatus(rawMaterial.getStatus());
        
        try {
	        return new RawMaterialDTO(inventoryRepository.save(rawMaterial));
	} catch(Exception e) {
	    throw new IndigoException(rawMaterial, e.getMessage());
	}

    }
*/
/*    *//**
     * Removes project.
     *
     * @param id Project's identifier
     *//*
    public void deleteChemical(String id) {
        Optional<RawMaterial> chemicalOpt = Optional.ofNullable(inventoryRepository.findOne(id));
        RawMaterial chemical = chemicalOpt.orElseThrow(() -> EntityNotFoundException.createWithProjectId(id));

        inventoryRepository.delete(chemical);
    }

*/
/*    public void deleteAll() {
	inventoryRepository.deleteAll();
    }*/
}
