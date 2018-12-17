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
package com.epam.indigoeln.core.service.batchqcrequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.indigo.IndigoException;
import com.epam.indigoeln.core.model.BatchQCRequest;
import com.epam.indigoeln.core.model.User;
import com.epam.indigoeln.core.model.UserPermission;
import com.epam.indigoeln.core.repository.batchQCrequest.BatchQCRequestRepository;
import com.epam.indigoeln.core.service.exception.EntityNotFoundException;
import com.epam.indigoeln.core.service.exception.OperationDeniedException;
import com.epam.indigoeln.core.service.user.UserService;
import com.epam.indigoeln.web.rest.dto.BatchQCRequestDTO;
import com.epam.indigoeln.web.rest.util.CustomDtoMapper;
import com.epam.indigoeln.web.rest.util.PermissionUtil;

/**
 * Provides a number of methods for access to project's data in database.
 */
@Service
public class BatchQCRequestService {

    /**
     * Instance of CustomDtoMapper for conversion from dto object.
     */
    @Autowired
    private CustomDtoMapper mapper;
    /**
     * Instance of ProjectRepository for access to projects in database.
     */

    @Autowired
    private BatchQCRequestRepository batchQCRequestRepository;

    /**
     * Instance of UserService for access to users in database.
     */
    @Autowired
    private UserService userService;


    public List<BatchQCRequestDTO> getAllBatchQCRequests() {
	return batchQCRequestRepository.findAll().stream().map(BatchQCRequestDTO::new).collect(Collectors.toList());
    }

    public List<BatchQCRequestDTO> getAllPendingBatchQCRequests() {
	return batchQCRequestRepository.findByBatchStatus("pending").stream().map(BatchQCRequestDTO::new).collect(Collectors.toList());
    }

    public List<BatchQCRequestDTO> getAllCompletedBatchQCRequests() {
	return batchQCRequestRepository.findByBatchStatus("completed").stream().map(BatchQCRequestDTO::new).collect(Collectors.toList());
    }
    /**
     * Returns BatchQCRequest by id according to permissions.
     *
     * @param id   BatchQCRequest's identifier
     * @param batchQCRequest BatchQCRequest
     * @return BatchQCRequest by id
     */
    public BatchQCRequestDTO getBatchQCRequestById(int batchQCRequestId, User user) {
	Optional<BatchQCRequest> batchRequestOpt = Optional.ofNullable(batchQCRequestRepository.findByBatchRequestId(batchQCRequestId));

	BatchQCRequest batchQCRequest = batchRequestOpt.orElseThrow(()
		-> EntityNotFoundException.createWithBatchRequestId(batchQCRequestId));

	//	BatchRequest batchRequest = mapper.convertFromDTO(batchRequestDTO);

	// Check of EntityAccess (User must have "Read Entity" permission in batchRequest's access list,
	// or must have CONTENT_EDITOR authority)
	if (!PermissionUtil.hasEditorAuthorityOrPermissions(user, batchQCRequest.getAccessList(),
		UserPermission.READ_ENTITY)) {
	    throw OperationDeniedException.createBatchRequestReadOperation(batchQCRequest.getId());
	}
	return new BatchQCRequestDTO(batchQCRequest);
    }
    /**
     * Returns BatchQCRequest by nBKBatchId according to permissions.
     *
     * @param id   BatchQCRequest's identifier
     * @param batchQCRequest BatchQCRequest
     * @return BatchQCRequest by NBKBatchId
     */
    public BatchQCRequestDTO getBatchQCRequestByNBKBatchId(String nbkBatchId, User user) {

	System.out.println("Getting BatchQCRequest By NBKBatchId");
	Optional<BatchQCRequest> batchRequestOpt = Optional.ofNullable(batchQCRequestRepository.findByNbkBatchId(nbkBatchId));
	BatchQCRequest batchQCRequest = batchRequestOpt.orElseThrow(()
		-> EntityNotFoundException.createWithBatchQCRequestId(nbkBatchId));

	if (!PermissionUtil.hasEditorAuthorityOrPermissions(user, batchQCRequest.getAccessList(),
		UserPermission.READ_ENTITY)) {
	    throw OperationDeniedException.createBatchRequestReadOperation(batchQCRequest.getId());
	}
	return new BatchQCRequestDTO(batchQCRequest);
    }



    public BatchQCRequestDTO createBatchQCRequest(BatchQCRequestDTO batchQCRequestDTO, User user) {
	System.out.println("In createBatchRequest method ");
	BatchQCRequest batchQCRequest = mapper.convertFromDTO(batchQCRequestDTO);
	batchQCRequest.setAuthor(user);
	batchQCRequest = saveBatchQCRequest(batchQCRequest);
	return new BatchQCRequestDTO(batchQCRequest);
    }

    public BatchQCRequestDTO updateBatchQCRequest(BatchQCRequestDTO batchQCRequestDTO, User user) {
	System.out.println("In updateBatchQCRequest method ");
	BatchQCRequest batchQCRequestFromDb = Optional.ofNullable(batchQCRequestRepository.findByBatchRequestId(batchQCRequestDTO.getBatchQCRequestId()))
		.orElseThrow(() -> EntityNotFoundException.createWithBatchRequestId(batchQCRequestDTO.getBatchQCRequestId()));

	/* if (!PermissionUtil.hasEditorAuthorityOrPermissions(user, batchQCRequestFromDb.getAccessList(),
	                UserPermission.UPDATE_ENTITY)) {
	            throw OperationDeniedException.createBatchRequestUpdateOperation(batchQCRequestFromDb.getId());
	        }*/
	BatchQCRequest batchQCRequest = mapper.convertFromDTO(batchQCRequestDTO);
	// check of user permissions's correctness in access control list
	//        PermissionUtil.checkCorrectnessOfAccessList(userService, batchRequest.getAccessList());

	batchQCRequestFromDb.setBatchQCRequestStatus(batchQCRequest.getBatchQCRequestStatus());
	batchQCRequestFromDb.setFileId(batchQCRequest.getFileId());
	batchQCRequestFromDb.setLastModifiedBy(user);
	batchQCRequestFromDb.setName(batchQCRequest.getName());
	batchQCRequestFromDb.setProperties(batchQCRequest.getProperties());

	batchQCRequest = saveBatchQCRequest(batchQCRequestFromDb);

	return new BatchQCRequestDTO(batchQCRequest);
    }


    /**
     * Removes batchRequest.
     *
     * @param id BatchRequest's identifier
     */
    public void deleteBatchQCRequest(String id) {
	Optional<BatchQCRequest> batchQCRequestOpt = Optional.ofNullable(batchQCRequestRepository.findOne(id));
	BatchQCRequest batchQCRequest = batchQCRequestOpt.orElseThrow(() -> EntityNotFoundException.createWithProjectId(id));
	batchQCRequestRepository.delete(batchQCRequest);
    }

    private BatchQCRequest saveBatchQCRequest(BatchQCRequest batchQCRequest) {
	try {
	    System.out.println("In saveBatchQCRequest method");
	    return batchQCRequestRepository.save(batchQCRequest);
	} catch(Exception e) {
	    throw new IndigoException(batchQCRequest, e.getMessage());
	}
    }

    public void deleteAll() {
	batchQCRequestRepository.deleteAll();
    }
}
