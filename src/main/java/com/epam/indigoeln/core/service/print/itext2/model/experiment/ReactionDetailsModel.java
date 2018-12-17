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
package com.epam.indigoeln.core.service.print.itext2.model.experiment;

import com.epam.indigoeln.core.service.print.itext2.model.common.SectionModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Implementation of SectionModel interface for reaction details.
 */
@Data
@Accessors(chain = true)
public class ReactionDetailsModel implements SectionModel {
    private ZonedDateTime creationDate;
    /**
     * @return the creationDate
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }
    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
    /**
     * @return the therapeuticArea
     */
    public String getTherapeuticArea() {
        return therapeuticArea;
    }
    /**
     * @param therapeuticArea the therapeuticArea to set
     */
    public void setTherapeuticArea(String therapeuticArea) {
        this.therapeuticArea = therapeuticArea;
    }
    /**
     * @return the continuedFrom
     */
    public String getContinuedFrom() {
        return continuedFrom;
    }
    /**
     * @param continuedFrom the continuedFrom to set
     */
    public void setContinuedFrom(String continuedFrom) {
        this.continuedFrom = continuedFrom;
    }
    /**
     * @return the continuedTo
     */
    public String getContinuedTo() {
        return continuedTo;
    }
    /**
     * @param continuedTo the continuedTo to set
     */
    public void setContinuedTo(String continuedTo) {
        this.continuedTo = continuedTo;
    }
    /**
     * @return the projectCode
     */
    public String getProjectCode() {
        return projectCode;
    }
    /**
     * @param projectCode the projectCode to set
     */
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
    /**
     * @return the projectAlias
     */
    public String getProjectAlias() {
        return projectAlias;
    }
    /**
     * @param projectAlias the projectAlias to set
     */
    public void setProjectAlias(String projectAlias) {
        this.projectAlias = projectAlias;
    }
    /**
     * @return the linkedExperiment
     */
    public List<String> getLinkedExperiment() {
        return linkedExperiment;
    }
    /**
     * @param linkedExperiment the linkedExperiment to set
     */
    public void setLinkedExperiment(List<String> linkedExperiment) {
        this.linkedExperiment = linkedExperiment;
    }
    /**
     * @return the literatureReference
     */
    public String getLiteratureReference() {
        return literatureReference;
    }
    /**
     * @param literatureReference the literatureReference to set
     */
    public void setLiteratureReference(String literatureReference) {
        this.literatureReference = literatureReference;
    }
    /**
     * @return the coAuthors
     */
    public List<String> getCoAuthors() {
        return coAuthors;
    }
    /**
     * @param coAuthors the coAuthors to set
     */
    public void setCoAuthors(List<String> coAuthors) {
        this.coAuthors = coAuthors;
    }
    private String therapeuticArea;
    private String continuedFrom;
    private String continuedTo;
    private String projectCode;
    private String projectAlias;
    private List<String> linkedExperiment;
    private String literatureReference;
    private List<String> coAuthors;
}
