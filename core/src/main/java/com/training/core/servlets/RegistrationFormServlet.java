package com.training.core.servlets;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Registartion form servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=/bin/registerTrainingServlet"
        })

public class RegistrationFormServlet extends SlingAllMethodsServlet {

    /**
     * The resolver factory.
     */
    @Reference
    private ResourceResolverFactory resolverFactory;
    private static final long serialVersionUID = 1L;


    @Override
    protected void doPost(final SlingHttpServletRequest request,
                          final SlingHttpServletResponse response) throws ServletException, IOException {

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String hobbies = request.getParameter("hobbies");

        injestCustData(firstName, lastName, hobbies, response, request);
    }


    //Stores customer data in the Adobe CQ JCR
    public void injestCustData(String firstName, String lastName,
                               String hobbies, final SlingHttpServletResponse response, SlingHttpServletRequest request) {

        Node registration;
        try {

            /**
             * Getting the instance of resource resolver from the request
             */
            ResourceResolver resourceResolver = request.getResourceResolver();

            /**
             * Getting the resource object via path
             */
            Resource resource = resourceResolver.getResource("/content/training/jcr:content");

            //Create a node that represents the root node
            Node root = resource.adaptTo(Node.class);

            if (doesRegistrationNodeExist(root) == -1) {
                registration = root.addNode("registration", "nt:unstructured");
                registration.setProperty("register", "name");
            } else
                registration = root.getNode("registration");

            Date date = new Date();
            long timeMilli = date.getTime();


            Node name = registration.addNode(String.valueOf(timeMilli));
            name.setProperty("firstname", firstName);
            name.setProperty("lastname", lastName);

            if (!hobbies.equals("")){
                String hobbiesArray[] = new String[]{hobbies};
                name.setProperty("hobbies", hobbiesArray);
            }

            resourceResolver.commit();

            response.getWriter().write("Done");
        } catch (Exception e) {
        }

    }

    /*
     * Determines if the content/registration node exists
     * This method returns these values:
     * -1 - if registration does not exist
     * 0 - if content/registration node exists; however, contains no children
     * number - the number of children that the content/registration node contains
     */
    private int doesRegistrationNodeExist(Node content) {
        try {
            int index = 0;
            int childRecs = 0;

            Iterable<Node> custNode = JcrUtils.getChildNodes(content, "registration");
            Iterator it = custNode.iterator();

            //only going to be 1 content/customer node if it exists
            if (it.hasNext()) {
                //Count the number of child nodes to customer
                Node customerRoot = content.getNode("registration");
                Iterable itCust = JcrUtils.getChildNodes(customerRoot);
                Iterator childNodeIt = itCust.iterator();

                while (childNodeIt.hasNext()) {
                    childRecs++;
                    childNodeIt.next();
                }
                return childRecs;
            } else
                return -1; //content/registration does not exist
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
