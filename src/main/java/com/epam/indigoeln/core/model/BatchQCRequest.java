/**
 * 
 */
package com.epam.indigoeln.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.DBRef;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Eshwar Kumar
 *
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = BatchQCRequest.COLLECTION_NAME)
public class BatchQCRequest extends BasicModelObject{

    /**
     * 
     */
    private static final long serialVersionUID = 2486279402618311539L;

    public static final String COLLECTION_NAME = "batchqcrequest";

    private int batchQCRequestId;
    private String batchQCRequestName;
    private String batchQCRequestStatus;
    private String description;
    
    private String nbkBatchId;

    @JsonIgnore
    private String fileId;
    
    @JsonIgnore
    private List<Map<String,Object>> properties = new ArrayList<Map<String,Object>>();
    
    private DBRef component;

/**
     * @return the component
     */
    public DBRef getComponent() {
        return component;
    }
    /**
     * @param component the component to set
     */
    public void setComponent(DBRef component) {
        this.component = component;
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
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
     * @return the fileIds
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * @param fileIds the fileIds to set
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
}