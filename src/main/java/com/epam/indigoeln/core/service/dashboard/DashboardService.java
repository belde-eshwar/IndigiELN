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
package com.epam.indigoeln.core.service.dashboard;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.epam.indigoeln.IndigoRuntimeException;
import com.epam.indigoeln.config.DashboardProperties;
import com.epam.indigoeln.core.model.Component;
import com.epam.indigoeln.core.model.Experiment;
import com.epam.indigoeln.core.model.ExperimentStatus;
import com.epam.indigoeln.core.model.Notebook;
import com.epam.indigoeln.core.model.Project;
import com.epam.indigoeln.core.model.User;
import com.epam.indigoeln.core.model.UserPermission;
import com.epam.indigoeln.core.service.signature.SignatureService;
import com.epam.indigoeln.core.service.signature.SignatureService.Document;
import com.epam.indigoeln.core.service.user.UserService;
import com.epam.indigoeln.core.util.JSR310DateConverters;
import com.epam.indigoeln.core.util.SequenceIdUtil;
import com.epam.indigoeln.web.rest.DashboardResource;
import com.epam.indigoeln.web.rest.dto.DashboardDTO;
import com.epam.indigoeln.web.rest.dto.DashboardRowDTO;
import com.epam.indigoeln.web.rest.util.PermissionUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBRef;

import lombok.val;

@Service
public class DashboardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardResource.class);

    private static final String EXCEPTION_MESSAGE = "Unable to get list of documents from signature service.";

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private DashboardProperties dashboardProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Create the current user's dashboard information.
     *
     * @return the current user's dashboard information
     */
    @SuppressWarnings("unchecked")
    public DashboardDTO getDashboard() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("REST request to get dashboard experiments");
        }

        DashboardDTO dashboardDTO = new DashboardDTO();

        // Necessary collections

        DBCollection projectCollection = mongoTemplate.getCollection(Project.COLLECTION_NAME);
        DBCollection notebookCollection = mongoTemplate.getCollection(Notebook.COLLECTION_NAME);
        DBCollection experimentCollection = mongoTemplate.getCollection(Experiment.COLLECTION_NAME);
        DBCollection componentCollection = mongoTemplate.getCollection(Component.COLLECTION_NAME);
        DBCollection userCollection = mongoTemplate.getCollection(User.COLLECTION_NAME);

        // Current user and date threshold

        User user = userService.getUserWithAuthorities();

        ZonedDateTime zonedThreshold = ZonedDateTime.now()
                .minus(dashboardProperties.getThresholdLevel(), dashboardProperties.getThresholdUnit());
        Date threshold = JSR310DateConverters.ZonedDateTimeToDateConverter.INSTANCE.convert(zonedThreshold);

        // Load all necessary entities

        Map<Object,DBObject> experiments = new HashMap<Object, DBObject>();
        experimentCollection
                .find(new BasicDBObject("$or", Arrays.asList(
                        new BasicDBObject("author.$id", user.getId()),
                        new BasicDBObject("submittedBy.$id", user.getId()))))
                .forEach(e -> experiments.put(e.get("_id"), e));

        Map<Object,DBObject> notebooks = new HashMap<Object, DBObject>();
        notebookCollection
                .find(new BasicDBObject("experiments.$id", new BasicDBObject("$in", experiments.keySet())))
                .forEach(n -> notebooks.put(n.get("_id"), n));

        Map<Object,DBObject> projects = new HashMap<Object, DBObject>();
        projectCollection
                .find(new BasicDBObject("notebooks.$id", new BasicDBObject("$in", notebooks.keySet())))
                .forEach(p -> projects.put(p.get("_id"), p));

        Set<Object> componentIds = new HashSet<Object>();
        experiments.values().forEach(e -> ((Iterable) e.get("components")).forEach(c -> componentIds.add(((DBRef) c).getId())));

        Map<Object,DBObject> components = new HashMap<Object, DBObject>();
        componentCollection
                .find(new BasicDBObject()
                        .append("_id", new BasicDBObject("$in", componentIds))
                        .append("$or", Arrays.asList(
                                new BasicDBObject("name", "reaction"),
                                new BasicDBObject("name", "reactionDetails"),
                                new BasicDBObject("name", "conceptDetails"))))
                .forEach(c -> components.put(c.get("_id"), c));

        Set<Object> userIds = new HashSet<Object>();
        experiments.values()
                .forEach(e -> {
                    if (e.get("author") != null) {
                        userIds.add(((DBRef) e.get("author")).getId());
                    }
                    if (e.get("submittedBy") != null) {
                        userIds.add(((DBRef) e.get("submittedBy")).getId());
                    }
                });
        components.values()
                .forEach(c -> {
                    if (c.get("content") != null && ((DBObject) c.get("content")).get("coAuthors") != null) {
                        ((Iterable) ((DBObject) c.get("content")).get("coAuthors")).forEach(userIds::add);
                    }
                });

        Map<Object,DBObject> users = new HashMap<Object, DBObject>();
        userCollection.find(new BasicDBObject("_id", new BasicDBObject("$in", userIds))).forEach(u -> users.put(u.get("_id"), u));

        // Fill dashboard

        dashboardDTO.setOpenAndCompletedExp(openExperiments(threshold, user, projects, notebooks, experiments, components, users));
        dashboardDTO.setWaitingSignatureExp(waitingExperiments(user, projects, notebooks, experiments, components, users));
        dashboardDTO.setSubmittedAndSigningExp(submittedExperiments(threshold, user, projects, notebooks, experiments, components, users));

        return dashboardDTO;
    }

    /**
     * Get opened experiments for current user.
     *
     * @param threshold   date threshold
     * @param user        current user
     * @param projects    current user projects
     * @param notebooks   current user notebooks
     * @param experiments current user experiments
     * @param components  necessary components
     * @param users       necessary users
     * @return opened experiments for current user.
     */
    private List<DashboardRowDTO> openExperiments(Date threshold,
                                                  User user,
                                                  Map<Object, DBObject> projects,
                                                  Map<Object, DBObject> notebooks,
                                                  Map<Object, DBObject> experiments,
                                                  Map<Object, DBObject> components,
                                                  Map<Object, DBObject> users) {
	Map<Object,DBObject> openExperiments = new HashMap<Object, DBObject>();
        experiments
                .forEach((k, v) -> {
                    if (threshold.before((Date) v.get("creationDate")) && "open".equalsIgnoreCase(String.valueOf(v.get("status")))) {
                        openExperiments.put(k, v);
                    }
                });

        return openExperiments.values().stream()
                .map(e -> getEntities(e, notebooks, projects))
                .filter(t -> hasAccess(user, t))
                .map(t -> convert(t, components, users, null))
                .collect(Collectors.toList());
    }


    /**
     * Get experiments which are waiting for user's signature.
     *
     * @param user        current user
     * @param projects    current user projects
     * @param notebooks   current user notebooks
     * @param experiments current user experiments
     * @param components  necessary components
     * @param users       necessary users
     * @return experiments which are waiting for user's signature
     */
    private List<DashboardRowDTO> waitingExperiments(User user,
                                                     Map<Object, DBObject> projects,
                                                     Map<Object, DBObject> notebooks,
                                                     Map<Object, DBObject> experiments,
                                                     Map<Object, DBObject> components,
                                                     Map<Object, DBObject> users) {
        Map<String,SignatureService.Document> waitingDocuments = new HashMap<String, SignatureService.Document>();
        try {
            signatureService.getDocumentsByUser().stream()
                    .filter(d -> d.isActionRequired()
                            && (d.getStatus() == SignatureService.ISSStatus.SIGNING
                            || d.getStatus() == SignatureService.ISSStatus.SUBMITTED))
                    .forEach(d -> waitingDocuments.put(d.getId(), d));
        } catch (Exception e) {
            LOGGER.error(EXCEPTION_MESSAGE, e);
            throw new IndigoRuntimeException(EXCEPTION_MESSAGE);
        }

        Map<Object,DBObject> waitingExperiments = new HashMap<Object, DBObject>();
        experiments
                .forEach((k, v) -> {
                    if (v.get("documentId") != null && waitingDocuments.containsKey(v.get("documentId"))) {
                        waitingExperiments.put(k, v);
                    }
                });

        return waitingExperiments.values().stream()
                .map(e -> getEntities(e, notebooks, projects))
                .filter(t -> hasAccess(user, t))
                .map(t -> convert(t, components, users, waitingDocuments))
                .collect(Collectors.toList());
    }

    /**
     * Get submitted experiments for current user.
     *
     * @param threshold   date threshold
     * @param user        current user
     * @param projects    current user projects
     * @param notebooks   current user notebooks
     * @param experiments current user experiments
     * @param components  necessary components
     * @param users       necessary users
     * @return submitted experiments for current user.
     */
    private List<DashboardRowDTO> submittedExperiments(Date threshold,
                                                       User user,
                                                       Map<Object, DBObject> projects,
                                                       Map<Object, DBObject> notebooks,
                                                       Map<Object, DBObject> experiments,
                                                       Map<Object, DBObject> components,
                                                       Map<Object, DBObject> users) {
	Map<Object,DBObject> submittedExperiments = new HashMap<Object, DBObject>();
        experiments
                .forEach((k, v) -> {
                    if (threshold.before((Date) v.get("creationDate")) && v.get("documentId") != null && !"open".equals(v.get("status")) && !"completed".equals(v.get("status"))) {
                        submittedExperiments.put(k, v);
                    }
                });

        Map<String,SignatureService.Document> submittedDocuments = new HashMap<String, SignatureService.Document>();
        try {
            signatureService.getDocumentsByIds(submittedExperiments.values().stream().map(e -> String.valueOf(e.get("documentId"))).collect(Collectors.toList()))
                    .forEach(d -> submittedDocuments.put(d.getId(), d));
        } catch (IOException e) {
            LOGGER.error(EXCEPTION_MESSAGE, e);
            throw new IndigoRuntimeException(EXCEPTION_MESSAGE);
        }

        return submittedExperiments.values().stream()
                .map(e -> getEntities(e, notebooks, projects))
                .filter(t -> hasAccess(user, t))
                .map(t -> convert(t, components, users, submittedDocuments))
                .collect(Collectors.toList());
    }

    /**
     * Convert experiment to dashboard row.
     *
     * @param t          triple project-notebook-experiment
     * @param components necessary components
     * @param users      necessary users
     * @param documents  documents for this experiment
     * @return dashboard row for experiment
     */
    @SuppressWarnings("unchecked")
    private DashboardRowDTO convert(
            Triple<DBObject, DBObject, DBObject> t,
            Map<Object, DBObject> components,
            Map<Object, DBObject> users,
            Map<String, SignatureService.Document> documents) {
        DBObject project = t.getLeft();
        DBObject notebook = t.getMiddle();
        DBObject experiment = t.getRight();

        DashboardRowDTO result = new DashboardRowDTO();

        result.setProjectId(SequenceIdUtil.extractShortId(String.valueOf(project.get("_id"))));
        result.setNotebookId(SequenceIdUtil.extractShortId(String.valueOf(notebook.get("_id"))));
        result.setExperimentId(SequenceIdUtil.extractShortId(String.valueOf(experiment.get("_id"))));
        result.setId(notebook.get("name") + "-" + experiment.get("name"));
        result.setStatus(ExperimentStatus.fromValue(String.valueOf(experiment.get("status"))));
        result.setProject(String.valueOf(project.get("name")));

        result.setCreationDate(JSR310DateConverters.DateToZonedDateTimeConverter.INSTANCE.convert((Date) experiment.get("creationDate")));
        result.setLastEditDate(JSR310DateConverters.DateToZonedDateTimeConverter.INSTANCE.convert((Date) experiment.get("lastEditDate")));

        Object title = getExperimentDetailsValue(experiment, Arrays.asList("reactionDetails", "conceptDetails"), "title", components);
        result.setName(title == null ? null : String.valueOf(title));

        Object reactionImage = getExperimentDetailsValue(experiment, Collections.singletonList("reaction"), "image", components);
        result.setReactionImage(reactionImage == null ? null : String.valueOf(reactionImage));

        if (documents != null && experiment.get("documentId") != null) {
            Document document = documents.get(experiment.get("documentId"));
            if (document != null) {
                result.setWitnesses(
                        document.getWitnesses().stream()
                                .map(u -> new DashboardRowDTO.UserDTO(u.getFirstName(), u.getLastName()))
                                .collect(Collectors.toList()));

                result.setComments(
                        document.getWitnesses().stream()
                                .map(SignatureService.User::getComment)
                                .collect(Collectors.toList()));
            }
        }

        result.setAuthor(getUserDTO(experiment, "author", users));
        result.setSubmitter(getUserDTO(experiment, "submittedBy", users));

        Object coAuthors = getExperimentDetailsValue(experiment, Arrays.asList("reactionDetails", "conceptDetails"), "coAuthors", components);
        if (coAuthors instanceof Iterable) {
            List<String> ca = new ArrayList<String>();

            ((Iterable) coAuthors).forEach(c -> {
                if (c != null) {
                    ca.add(String.valueOf(c));
                }
            });

            result.setCoAuthors(ca);
        }

        return result;
    }

    /**
     * Get the UserDTO for given key.
     *
     * @param experiment experiment to get UserDTO for value
     * @param key        key to get value
     * @param users      all users
     * @return the UserDTO for given key
     */
    private DashboardRowDTO.UserDTO getUserDTO(DBObject experiment, String key, Map<Object, DBObject> users) {
        if (experiment != null) {
            Object value = experiment.get(key);

            if (value instanceof DBRef) {
                DBObject user = users.get(((DBRef) value).getId());

                if (user != null) {
                    return new DashboardRowDTO.UserDTO(
                            String.valueOf(user.get("first_name")),
                            String.valueOf(user.get("last_name")));
                }
            }
        }

        return null;
    }

    /**
     * Get the value for given key in reaction or concept details.
     *
     * @param experiment experiment to get details value
     * @param key        key to get value
     * @param components all components
     * @return the value for given key in reaction or concept details
     */
    private Object getExperimentDetailsValue(DBObject experiment, List<String> name, String key, Map<Object, DBObject> components) {
        if (experiment == null || key == null) {
            return null;
        }

        for (Object c : (Iterable) experiment.get("components")) {
            DBObject component = components.get(((DBRef) c).getId());

            if (component != null && name.contains(component.get("name"))) {
                DBObject content = (DBObject) component.get("content");

                if (content != null) {
                    return content.get(key);
                }

                break;
            }
        }

        return null;
    }

    /**
     * Get notebook and project for experiment.
     *
     * @param experiment experiment
     * @param notebooks  all notebooks
     * @param projects   all projects
     * @return triple with notebook and project for experiment
     */
    @SuppressWarnings("unchecked")
    private Triple<DBObject, DBObject, DBObject> getEntities(DBObject experiment, Map<Object, DBObject> notebooks, Map<Object, DBObject> projects) {
        DBObject notebook = experiment == null ? null : notebooks.values().stream()
                .filter(n -> {
                    for (Object e : (Iterable) n.get("experiments")) {
                        if (experiment.get("_id").equals(((DBRef) e).getId())) {
                            return true;
                        }
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);

        DBObject project = notebook == null ? null : projects.values().stream()
                .filter(p -> {
                    for (Object n : (Iterable) p.get("notebooks")) {
                        if (notebook.get("_id").equals(((DBRef) n).getId())) {
                            return true;
                        }
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);

        return Triple.of(project, notebook, experiment);
    }

    /**
     * Check user access for project, notebook and experiment.
     *
     * @param user current user
     * @param t    triple project-notebook-experiment
     * @return true if user has access to each of project/notebook/experiment
     */
    @SuppressWarnings("unchecked")
    private boolean hasAccess(User user, Triple<DBObject, DBObject, DBObject> t) {
        DBObject project = t.getLeft();
        DBObject notebook = t.getMiddle();
        DBObject experiment = t.getRight();

        if (project == null) {
            return false;
        }
        Set<UserPermission> projectAccessList = new HashSet<UserPermission>();
        ((Iterable) project.get("accessList")).forEach(a -> projectAccessList.add(new UserPermission((DBObject) a)));

        if (notebook == null) {
            return false;
        }
        Set<UserPermission> notebookAccessList = new HashSet<UserPermission>();
        ((Iterable) notebook.get("accessList")).forEach(a -> notebookAccessList.add(new UserPermission((DBObject) a)));

        if (experiment == null) {
            return false;
        }
        Set<UserPermission> experimentAccessList = new HashSet<UserPermission>();
        ((Iterable) experiment.get("accessList")).forEach(a -> experimentAccessList.add(new UserPermission((DBObject) a)));

        if (!PermissionUtil.hasEditorAuthorityOrPermissions(user, projectAccessList, UserPermission.READ_ENTITY)) {
            return false;
        }

        if (!PermissionUtil.hasEditorAuthorityOrPermissions(user, notebookAccessList, UserPermission.READ_ENTITY)) {
            return false;
        }

        return PermissionUtil.hasEditorAuthorityOrPermissions(user, experimentAccessList, UserPermission.READ_ENTITY);
    }
}
