package com.training.core.servlets;

import com.google.gson.JsonObject;
import com.training.core.services.RestAPIService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Country detail servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=/bin/countryDetailsServlet"
        })
public class CountryDetailsServlet extends SlingAllMethodsServlet {

    @Reference
    RestAPIService restAPIService;

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(RegistrationFormServlet.class);

    @Override
    protected void doPost(final SlingHttpServletRequest request,
                          final SlingHttpServletResponse response) throws ServletException, IOException {

        String countryName = request.getParameter("countryName");

        getCountryDetail(countryName, response, request);
    }

    private void getCountryDetail(String countryName, SlingHttpServletResponse response, SlingHttpServletRequest request) {
        try {
            String status = "OK";

            HttpClient httpClient = HttpClientBuilder.create().build();

            HttpGet getRequest = new HttpGet(restAPIService.getUrl() + countryName);
            getRequest.addHeader("accept", "application/json");

            HttpResponse httpResponse = httpClient.execute(getRequest);

            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                status = "Not OK";
                sendResponse(status, response, "");
                return;

            }

            BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));

            String output;
            String myJSON = "";
            while ((output = br.readLine()) != null) {
                myJSON = myJSON + output;
            }

            httpClient.getConnectionManager().shutdown();

            sendResponse(status, response, myJSON);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(String status, SlingHttpServletResponse response, String apiResponse) throws JSONException, IOException {
        JSONObject mainJsonObject;
        if (!apiResponse.equals(""))
            mainJsonObject = new JSONObject(apiResponse);
        else
            mainJsonObject = new JSONObject();

        mainJsonObject.put("status", status);
        log.error("mainJsonObject" + mainJsonObject.toString());
        PrintWriter out = response.getWriter();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
        out.write(mainJsonObject.toString());
        out.flush();

    }
}
