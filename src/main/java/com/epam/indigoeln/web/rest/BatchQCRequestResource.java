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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.indigoeln.core.model.User;
import com.epam.indigoeln.core.service.batchqcrequest.BatchQCRequestService;
import com.epam.indigoeln.core.service.user.UserService;
import com.epam.indigoeln.web.rest.dto.BatchQCRequestDTO;
import com.epam.indigoeln.web.rest.util.HeaderUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api
@RestController
@RequestMapping(BatchQCRequestResource.URL_MAPPING)
public class BatchQCRequestResource {

    static final String URL_MAPPING = "/api/batchQCRequests";
    private static final String PATH_ID = "/{id:[\\d]+}";
    private static final String ENTITY_NAME = "BatchQCRequest";

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchQCRequestResource.class);

    @Autowired
    private BatchQCRequestService batchQCRequestService;

    @Autowired
    private UserService userService;
    
    /**
     * GET  /batchQCRequests/:id -> Returns batchQCRequest with specified id according to User permissions.
     *
     * @param id Identifier
     * @return BatchQCRequest
     */
    @ApiOperation(value = "Returns batchQCRequest by it's id according to permissions.")
    @RequestMapping(value = PATH_ID, method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BatchQCRequestDTO> getBatchQCRequest(
            @ApiParam("batchQCRequestId") @PathVariable("batchQCRequestId") int batchQCRequestId
    ) {
        LOGGER.debug("REST request to get BatchQCRequest: {}", batchQCRequestId);
        User user = userService.getUserWithAuthorities();
        BatchQCRequestDTO batchQCRequest = batchQCRequestService.getBatchQCRequestById(batchQCRequestId, user);
        return ResponseEntity.ok(batchQCRequest);
    }
    
    /**
     * GET  /batchQCRequests/:id -> Returns batchQCRequest with specified id according to User permissions.
     *
     * @param id Identifier
     * @return BatchQCRequest
     */
    @ApiOperation(value = "Returns batchQCRequest by it's id according to permissions.")
    @RequestMapping(value = "/batchQCRequestByNBKBatchId", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BatchQCRequestDTO> batchQCRequestByNBKBatchId(
            @ApiParam("nbkBatchId") @RequestParam("nbkBatchId") String nbkBatchId
    ) {
        LOGGER.debug("REST request to get BatchQCRequest By nbkBatchId: {}", nbkBatchId);
        User user = userService.getUserWithAuthorities();
        BatchQCRequestDTO batchQCRequest = batchQCRequestService.getBatchQCRequestByNBKBatchId(nbkBatchId, user);
        return ResponseEntity.ok(batchQCRequest);
    }

    /**
     * POST  /batchQCRequests -> Creates batchQCRequest with OWNER's permissions for current User.
     *
     * @param BatchQCRequest batchQCRequest
     * @return Created batchQCRequest
     * @throws URISyntaxException If URI is not correct
     */
    
    @ApiOperation(value = "Creates BatchQCRequest with OWNER's permissions for current user.")
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BatchQCRequestDTO> createBatchQCRequest(
            @ApiParam("BatchQCRequest to create") @RequestBody BatchQCRequestDTO batchQCRequest
    ) throws URISyntaxException {
    
	System.out.println("Creating BatchQCRequest \n BatchRequest Name:"+batchQCRequest.getBatchQCRequestName());
        LOGGER.debug("REST request to create BatchQCRequest: {}", batchQCRequest);
        User user = userService.getUserWithAuthorities();
        BatchQCRequestDTO createdBatchQCRequest = batchQCRequestService.createBatchQCRequest(batchQCRequest, user);
        HttpHeaders headers = HeaderUtil.createEntityCreateAlert(ENTITY_NAME, createdBatchQCRequest.getName());
       // headers.add("batchQCRequestStatus", createdBatchQCRequest.getBatchQCRequestStatus());
        return ResponseEntity.created(new URI(URL_MAPPING + "/" + createdBatchQCRequest.getId()))
                .headers(headers).body(createdBatchQCRequest);
    }
    
    @ApiOperation(value = "Updates BatchQCRequest with OWNER's permissions for current user.")
    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BatchQCRequestDTO> updateBatchQCRequest(
            @ApiParam("BatchQCRequest to update") @RequestBody BatchQCRequestDTO batchQCRequest
    ) throws URISyntaxException {
    
	System.out.println("Updating BatchQCRequest\n BatchQCRequest Name:"+batchQCRequest.getBatchQCRequestName());
        LOGGER.debug("REST request to update BatchQCRequest: {}", batchQCRequest);
        User user = userService.getUserWithAuthorities();
        BatchQCRequestDTO updatedBatchQCRequest = batchQCRequestService.updateBatchQCRequest(batchQCRequest, user);
        HttpHeaders headers = HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, updatedBatchQCRequest.getName());
        return ResponseEntity.ok().headers(headers).body(updatedBatchQCRequest);
    }
    
    @ApiOperation(value = "Returns all Pending BatchQCRequests.")
    @RequestMapping(value = "/pendingBatchQCRequests", method = RequestMethod.GET)
    public ResponseEntity<List<BatchQCRequestDTO>> getAllPendingBatchQCRequests() {
	System.out.println("REST request to get all pending batchQCRequests");
        LOGGER.debug("REST request to get all pending batchQCRequests");
        List<BatchQCRequestDTO> result = batchQCRequestService.getAllPendingBatchQCRequests();
        return ResponseEntity.ok(result);
    }
    
    @ApiOperation(value = "Returns all Completed BatchQCRequests.")
    @RequestMapping(value = "/completedBatchQCRequests", method = RequestMethod.GET)
    public ResponseEntity<List<BatchQCRequestDTO>> getAllCompletedBatchQCRequests() {
	System.out.println("REST request to get all completed batchQCRequests");
        LOGGER.debug("REST request to get all completed batchRequests");
        List<BatchQCRequestDTO> result = batchQCRequestService.getAllCompletedBatchQCRequests();
        return ResponseEntity.ok(result);
    }


    /**
     * DELETE  /batchQCRequests/:id -> Removes BatchQCRequest with specified id.
     *
     * @param id Identifier
     */
    @ApiOperation(value = "Removes BatchQCRequest.")
    @RequestMapping(value = PATH_ID, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteBatchQCRequest(
            @ApiParam("batchQCRequestId") @PathVariable String batchQCRequestId
    ) {

        LOGGER.debug("REST request to remove batchQCRequest: {}", batchQCRequestId);
        batchQCRequestService.deleteBatchQCRequest(batchQCRequestId);
        HttpHeaders headers = HeaderUtil.createEntityDeleteAlert(ENTITY_NAME, null);
        return ResponseEntity.ok().headers(headers).build();
    }
}
