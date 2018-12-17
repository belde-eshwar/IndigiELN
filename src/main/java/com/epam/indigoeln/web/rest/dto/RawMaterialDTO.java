/**
 * 
 */
package com.epam.indigoeln.web.rest.dto;

import com.epam.indigoeln.core.model.BasicModelObject;
import com.epam.indigoeln.core.model.RawMaterial;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Eshwar Kumar
 *
 */
/**
 * DTO for Chemical.
 */
@JsonTypeName("RawMaterial")
public class RawMaterialDTO extends  BasicModelObject{

    /**
     * 
     */
    private static final long serialVersionUID = 5016287109193825895L;
    String rawMaterialName;
    Double quanitity;
    String units;
    String vendor;
    String batchId;
    String status;
    /**
     * 
     */
    public RawMaterialDTO() {
	super();
    }

    public RawMaterialDTO(RawMaterial rawMaterial) {
	super();
	this.rawMaterialName = rawMaterial.getRawMaterialName();
	this.quanitity = rawMaterial.getQuanitity();
	this.units = rawMaterial.getUnits();
	this.vendor = rawMaterial.getVendor();
	this.batchId = rawMaterial.getBatchId();
	this.status = rawMaterial.getStatus();
    }

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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "RawMaterialDTO [rawMaterialName=" + rawMaterialName + ", quanitity=" + quanitity + ", units=" + units
		+ ", vendor=" + vendor + ", batchId=" + batchId + ", status=" + status + "]";
    }


}
