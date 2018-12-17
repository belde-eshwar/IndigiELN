/**
 * 
 */
package com.epam.indigoeln.core.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Eshwar Kumar
 *
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = RawMaterial.COLLECTION_NAME)
public class RawMaterial extends BasicModelObject{
    
    /**
     * 
     */
    private static final long serialVersionUID = -2664720310934726201L;
    public static final String COLLECTION_NAME = "rawmaterial";
    
    String rawMaterialName;
    Double quanitity;
    String units;
    String vendor;
    String batchId;
    String status;
    /**
     * @return the rawMaterialName
     */
    public String getRawMaterialName() {
        return rawMaterialName;
    }
    /**
     * @param rawMaterialName the rawMaterialName to set
     */
    public void setRawMaterialName(String rawMaterialName) {
        this.rawMaterialName = rawMaterialName;
    }
    /**
     * @return the quanitity
     */
    public Double getQuanitity() {
        return quanitity;
    }
    /**
     * @param quanitity the quanitity to set
     */
    public void setQuanitity(Double quanitity) {
        this.quanitity = quanitity;
    }
    /**
     * @return the units
     */
    public String getUnits() {
        return units;
    }
    /**
     * @param units the units to set
     */
    public void setUnits(String units) {
        this.units = units;
    }
    /**
     * @return the vendor
     */
    public String getVendor() {
        return vendor;
    }
    /**
     * @param vendor the vendor to set
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    /**
     * @return the batchId
     */
    public String getBatchId() {
        return batchId;
    }
    /**
     * @param batchId the batchId to set
     */
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
