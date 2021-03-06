/*
 *   Copyright 2009-2010 Rustici Software. Licensed under the
 *   Educational Community License, Version 2.0 (the "License"); you may
 *   not use this file except in compliance with the License. You may
 *   obtain a copy of the License at
 *   
 *   http://www.osedu.org/licenses/ECL-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an "AS IS"
 *   BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *   or implied. See the License for the specific language governing
 *   permissions and limitations under the License.
 */


package com.rusticisoftware.scormcloud.logic.entity;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.entity.api.EntityAccessOverloadException;
import org.sakaiproject.entity.api.EntityCopyrightException;
import org.sakaiproject.entity.api.EntityNotDefinedException;
import org.sakaiproject.entity.api.EntityPermissionException;
import org.sakaiproject.entity.api.HttpAccess;
import org.sakaiproject.entity.api.Reference;

import com.rusticisoftware.scormcloud.content.types.ScormCloudResourceType;


public class ScormCloudHttpAccess implements HttpAccess {
    private static Log log = LogFactory.getLog(ScormCloudHttpAccess.class);
    
    public void handleAccess(HttpServletRequest req, HttpServletResponse res, Reference ref, Collection copyrightAcceptedRefs) throws EntityPermissionException, EntityNotDefinedException, EntityAccessOverloadException, EntityCopyrightException {
        try {
            String packageId = (String)ref.getEntity()
                                        .getProperties()
                                        .get(ScormCloudResourceType.PROP_SCORMCLOUD_PACKAGE_ID);
           
            String refId = ref.getId();
            
            //This isn't the id, just a unique key to provide a 
            //sort of assignment "context" for the launch / registration
            String assignmentKey = "";
            
            //Parse out a possible assignment key/context of this reference
            if(refId.contains("Assignments/")){
                //Typically, reference is like "Assignments/[assignment key]/etc",
                //here we parse out the [assignment key]
                String assignmentString = refId.substring(refId.indexOf("Assignments/"));
                int beginAssignmentId = assignmentString.indexOf("/") + 1;
                int endAssignmentId = assignmentString.indexOf("/", beginAssignmentId);
                assignmentKey = assignmentString.substring(beginAssignmentId, endAssignmentId);
                
                log.debug("In handleAccess: assignmentKey = " + assignmentKey);
            }
           
            redirectToLaunchPage(req, res, packageId, assignmentKey);
           
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
     }

     private void redirectToLaunchPage(HttpServletRequest req, HttpServletResponse res, String packageId, String assignmentKey) throws Exception {
        res.sendRedirect("/scormcloud-tool/controller?action=launchPackage&resourceLink=true&id=" + packageId + "&assignmentKey=" + assignmentKey); 
     }

}
