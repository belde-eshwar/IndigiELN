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
package com.epam.indigoeln.web.rest.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epam.indigoeln.core.model.BatchQCRequest;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * DTO for BatchRequest.
 */
@JsonTypeName("BatchQCRequest")
public class BatchQCRequestDTO extends BasicDTO {

    private int batchQCRequestId;
    private String batchQCRequestName;
    private String batchQCRequestStatus;
    private String description;
    private String nbkBatchId;
    private String fileId;
    
    private List<Map<String,Object>> properties = new ArrayList<Map<String,Object>>();

   // private DBRef experiment;

    public BatchQCRequestDTO() {
        super();
    }

    public BatchQCRequestDTO(BatchQCRequest batchQCRequest) {

        super(batchQCRequest);
        this.batchQCRequestId = batchQCRequest.getBatchQCRequestId();
        this.batchQCRequestName = batchQCRequest.getBatchQCRequestName();
        this.batchQCRequestStatus = batchQCRequest.getBatchQCRequestStatus();
        this.description = batchQCRequest.getDescription();
        this.nbkBatchId = batchQCRequest.getNbkBatchId();
        this.fileId = batchQCRequest.getFileId();
        this.properties = batchQCRequest.getProperties();
    //    this.experiment = batchRequest.getExperiment();
    }



    public String getDescription() {
        return description;
    }

    /**
     * @return the experiment
     */
 /*   public DBRef getExperiment() {
        return experiment;
    }

    *//**
     * @param experiment the experiment to set
     *//*
    public void setExperiment(DBRef experiment) {
        this.experiment = experiment;
    }*/

    public void setDescription(String description) {
        this.description = description;
    }
       
    /**
     * @return the nbkBatchId
     */
    public String getNbkBatchId() {
        return nbkBatchId;
    }

    /**
     * @param nbkBatchId the nbkBatchId to set
     */
    public void setNbkBatchId(String nbkBatchId) {
        this.nbkBatchId = nbkBatchId;
    }

    /**
     * @return the batchQCRequestId
     */
    public int getBatchQCRequestId() {
        return batchQCRequestId;
    }

    /**
     * @param batchQCRequestId the batchQCRequestId to set
     */
    public void setBatchQCRequestId(int batchQCRequestId) {
        this.batchQCRequestId = batchQCRequestId;
    }

    /**
     * @return the batchQCRequestName
     */
    public String getBatchQCRequestName() {
        return batchQCRequestName;
    }

    /**
     * @param batchQCRequestName the batchQCRequestName to set
     */
    public void setBatchQCRequestName(String batchQCRequestName) {
        this.batchQCRequestName = batchQCRequestName;
    }

    /**
     * @return the batchQCRequestStatus
     */
    public String getBatchQCRequestStatus() {
        return batchQCRequestStatus;
    }

    /**
     * @param batchQCRequestStatus the batchQCRequestStatus to set
     */
    public void setBatchQCRequestStatus(String batchQCRequestStatus) {
        this.batchQCRequestStatus = batchQCRequestStatus;
    }

    /**
     * @return the fileId
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * @param fileId the fileId to set
     */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    /**
     * @return the properties
     */
    public List<Map<String, Object>> getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(List<Map<String, Object>> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "batchRequestDTO{} " + super.toString();
    }
}
