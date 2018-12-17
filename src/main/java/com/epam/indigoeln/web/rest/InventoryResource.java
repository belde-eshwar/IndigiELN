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
package com.epam.indigoeln.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.epam.indigoeln.core.model.RawMaterial;
import com.epam.indigoeln.core.service.inventory.InventoryService;
import com.epam.indigoeln.core.service.user.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api
@RestController
@RequestMapping(InventoryResource.URL_MAPPING)
public class InventoryResource {

    static final String URL_MAPPING = "/api/warehouse";
    private static final String PATH_ID = "/{id:[\\d]+}";
    private static final String ENTITY_NAME = "RawMaterial";

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryResource.class);

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private UserService userService;

/*    @ApiOperation(value = "Updates rawmaterial according to permissions.")
    @RequestMapping(method = RequestMethod.PUT, value=InventoryResource.URL_MAPPING+"/rawMaterialPurchase",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RawMaterialDTO> rawMaterialPurchase(
            @ApiParam("RawMaterial to update") @RequestBody RawMaterialDTO rawMaterialDTO
    ) {
        LOGGER.debug("REST request to update warehouse: {}", rawMaterialDTO);
        User user = userService.getUserWithAuthorities();
        RawMaterialDTO updatedRawMaterial = inventoryService.rawMaterialPurchase(rawMaterialDTO, user);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, updatedRawMaterial.getRawMaterialName());
        return ResponseEntity.ok().headers(headers).body(updatedRawMaterial);
    }*/

/*    @ApiOperation(value = "Updates rawmaterial according to permissions.")
    @RequestMapping(method = RequestMethod.PUT,value=InventoryResource.URL_MAPPING+"/rawMaterialConsumption",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RawMaterialDTO> rawMaterialConsumption(
            @ApiParam("RawMaterial to update") @RequestBody RawMaterialDTO rawMaterialDTO
    ) {
        LOGGER.debug("REST request to update warehouse: {}", rawMaterialDTO);
        User user = userService.getUserWithAuthorities();
        RawMaterialDTO updatedRawMaterial = inventoryService.rawMaterialConsumption(rawMaterialDTO, user);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, updatedRawMaterial.getRawMaterialName());
        return ResponseEntity.ok().headers(headers).body(updatedRawMaterial);
    }*/
    
    
    
    @ApiOperation(value = "get all Batches by rawMaterialName.")
    @RequestMapping(method = RequestMethod.PUT,value=InventoryResource.URL_MAPPING+"/getBatches",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RawMaterial>> getBatchesByRawMaterialNameAndQuantityGreaterThanZero(
            @ApiParam("get Batches By RawMaterialName") @RequestBody String rawMaterialName
    ) {
        LOGGER.debug("REST request to get Batches BY rawMaterialName: {}", rawMaterialName);
        List<RawMaterial> rawMaterialsList = inventoryService.getBatchesByRawMaterialNameAndQuantityGreaterThanZero(rawMaterialName);
        return ResponseEntity.ok(rawMaterialsList);
    }
    
    @ApiOperation(value = "Returns rawmaterial by it's name and batchId ")
    @RequestMapping(value=InventoryResource.URL_MAPPING+"/getRawMaterial", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RawMaterial> getRawMaterialByrawMaterialNameAndBatchId(
            @ApiParam("rawMaterialName") @PathVariable String rawMaterialName,
            @ApiParam("batchIdId") @PathVariable String batchId
    ) {
        LOGGER.debug("REST request to get RawMaterial By: {rawMateterialName:", rawMaterialName+", batchId  "+batchId+"}");
        RawMaterial rawMaterial = inventoryService.getRawMaterialByrawMaterialNameAndBatchId(rawMaterialName,batchId);
        return ResponseEntity.ok(rawMaterial);
    }

    /**
     * POST  /chemicals -> Creates chemical with OWNER's permissions for current User.
     *
     * @param RawMaterial chemical
     * @return Created chemical
     * @throws URISyntaxException If URI is not correct
     */
 /*   @ApiOperation(value = "Creates BatchRequest with OWNER's permissions for current user.")
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChemicalDTO> createChemical(
            @ApiParam("Chemical to create") @RequestBody ChemicalDTO chemicalDTO
    ) throws URISyntaxException {
        LOGGER.debug("REST request to create Chemical: {}", chemicalDTO);
        ChemicalDTO createdChemical = inventoryService.addChemical(chemicalDTO);
        HttpHeaders headers = HeaderUtil.createEntityCreateAlert(ENTITY_NAME, createdChemical.getChemicalName());
        return ResponseEntity.created(new URI(URL_MAPPING + "/"+createdChemical.getChemicalName()))
                .headers(headers).body(createdChemical);
    }
*/
    /**
     * PUT  /projects -> Updates project according to User permissions.
     *
     * @param project Project to update
     * @return Updated project
     */
 /*   @ApiOperation(value = "Updates chemical according to permissions.")
    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChemicalDTO> updateProject(
            @ApiParam("Project to update") @RequestBody ChemicalDTO chemicalDTO
    ) {
        LOGGER.debug("REST request to update project: {}", chemicalDTO);
        User user = userService.getUserWithAuthorities();
        ChemicalDTO updatedChemical = inventoryService.updateChemical(chemicalDTO, user);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, updatedChemical.getChemicalName());
        return ResponseEntity.ok().headers(headers).body(updatedChemical);
    }
*/
    /**
     * DELETE  /chemicals/:id -> Removes Chemical with specified id.
     *
     * @param id Identifier
     */
 /*   @ApiOperation(value = "Removes Chemical.")
    @RequestMapping(value = PATH_ID, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteChemical(
            @ApiParam("chemicalId") @PathVariable String chemicalId
    ) {
        LOGGER.debug("REST request to remove batch Request: {}", chemicalId);
        inventoryService.deleteChemical(chemicalId);
        HttpHeaders headers = HeaderUtil.createEntityDeleteAlert(ENTITY_NAME, null);
        return ResponseEntity.ok().headers(headers).build();
    }
    
    */
}
