package com.training.core.servlets;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Registartion form servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=/bin/allRegistrationTrainingData"
        })

public class GetAllRegistrationDataServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 2L;
    private static final Logger log = LoggerFactory.getLogger(GetAllRegistrationDataServlet.class);

    @Override
    protected void doGet(final SlingHttpServletRequest request,
                         final SlingHttpServletResponse response) throws ServletException, IOException {

        getNodeAndCheckData(request, response);

    }

    private void getNodeAndCheckData(SlingHttpServletRequest request, SlingHttpServletResponse response) {

       ArrayList<Node> userRegistrationDataList = new ArrayList<>();

        try {
            ResourceResolver resourceResolver = request.getResourceResolver();

            if (checkNodeExist(resourceResolver))
                getAllRegistrationData(resourceResolver, userRegistrationDataList, response);

            else {
                setMessageAndSendResponse("no record found", userRegistrationDataList, response);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllRegistrationData(ResourceResolver resourceResolver, List<Node> userRegistrationDataList, SlingHttpServletResponse response) throws IOException, RepositoryException {

        Node node = resourceResolver.getResource("/content/training/jcr:content/registration").adaptTo(Node.class);
        if (node.hasNodes()) {
            log.error("111111");
            NodeIterator ni = node.getNodes();
            while (ni.hasNext()) {

                log.error("22222");
               /* UserRegistrationData userRegistrationData = new UserRegistrationData();

                Property references = ni.nextNode().getProperty("hobbies");
                log.error("here111");
                Value[] values = references.getValues();
                log.error("here222");
                String hobbies = values[0].getString();
                log.error("here333");
                userRegistrationData.setFirstName(ni.nextNode().getProperty("firstname").getString());
                userRegistrationData.setLastName(ni.nextNode().getProperty("lastname").getString());
                userRegistrationData.setHobbies(hobbies);

                log.error("3333"+userRegistrationData.getFirstName());*/
                userRegistrationDataList.add(ni.nextNode());
            }

            for (Node node1:userRegistrationDataList){
               log.error("***"+ node1.getProperty("firstname").getString());
            }
            setMessageAndSendResponse("all done", userRegistrationDataList, response);

        } else
            setMessageAndSendResponse("no record found", userRegistrationDataList, response);

       /* if (resource != null) {
            Iterator<Resource> linkResources = resource.listChildren();
            while (linkResources.hasNext()) {
                Resource childResource = linkResources.next();
                UserRegistrationData userRegistrationData = childResource.adaptTo(UserRegistrationData.class);
                if (userRegistrationData != null) {
                    log.error("data is not null");
                    log.error("1111" + userRegistrationData.getFirstName());
                    userRegistrationDataList.add(userRegistrationData);
                } else
                    log.error("data is null");
            }
            setMessageAndSendResponse("all done", userRegistrationDataList, userRegistrationMessageAndData, response);
        }*/

    }

    private void setMessageAndSendResponse(String message, List<Node> userRegistrationDataList, SlingHttpServletResponse response) throws IOException {

        log.error("4444" + message);

        try {
            JSONObject mainJsonObject=new JSONObject();
            JSONArray jsonArray=new JSONArray();

            mainJsonObject.put("message",message);

            for (Node node:userRegistrationDataList){

                Property references = node.getProperty("hobbies");
                Value[] values = references.getValues();
                String hobbies = values[0].getString();

                JSONObject dataJsonObject=new JSONObject();
                dataJsonObject.put("firstName",node.getProperty("firstname").getString());
                dataJsonObject.put("lastName",node.getProperty("lastname").getString());
                dataJsonObject.put("hobbies",hobbies);

                jsonArray.put(dataJsonObject);
            }

            mainJsonObject.put("registrationData", jsonArray);

            log.error("5555" + mainJsonObject.toString());
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json");
            out.write(mainJsonObject.toString());
            out.flush();
        } catch (RepositoryException | JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean checkNodeExist(ResourceResolver resourceResolver) {

        Resource resource = resourceResolver.getResource("/content/training/jcr:content");
        Node root = resource.adaptTo(Node.class);
        try {
            Iterable<Node> custNode = JcrUtils.getChildNodes(root, "registration");
            Iterator it = custNode.iterator();

            //only going to be 1 content/registration node if it exists
            if (it.hasNext()) {
                //Count the number of child nodes to registration
                return true;
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
