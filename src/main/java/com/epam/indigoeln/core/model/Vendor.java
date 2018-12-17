/**
 * 
 */
package com.epam.indigoeln.core.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Eshwar Kumar
 *
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = Vendor.COLLECTION_NAME)
public class Vendor extends BasicModelObject{
    
    /**
     * 
     */
    private static final long serialVersionUID = 5989790731938171374L;

    /**
     * 
     */
    public static final String COLLECTION_NAME = "vendor";
    
    String vendorCode;
    String vendorName;
    String address;
    /**
     * @return the vendorCode
     */
    public String getVendorCode() {
        return vendorCode;
    }
    /**
     * @param vendorCode the vendorCode to set
     */
    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }
    /**
     * @return the vendorName
     */
    public String getVendorName() {
        return vendorName;
    }
    /**
     * @param vendorName the vendorName to set
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }
    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

}
