package com.training.core.models;

import com.training.core.services.RestAPIService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Model(adaptables = Resource.class)
public class CountryModel {

    protected static final Logger log = LoggerFactory.getLogger(CountryModel.class);

    @Inject
    private String countryName;

    private String name;

    private String capital;

    private String demonym;

    private String nativeName;

    private String flag;

    private String status="OK";

    @Inject
    RestAPIService restAPIService;

    @PostConstruct
    protected void init() {

        getCountryDetails();

    }

    private void getCountryDetails() {
        try {


            HttpClient httpClient = HttpClientBuilder.create().build();

            HttpGet getRequest = new HttpGet(restAPIService.getUrl() + countryName);
            getRequest.addHeader("accept", "application/json");

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                status="Not ok";
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            String output;
            String myJSON = "";
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                myJSON = myJSON + output;
            }

            httpClient.getConnectionManager().shutdown();

            JSONObject jsonObject = new JSONObject(myJSON);

            name = jsonObject.getString("name");
            capital = jsonObject.getString("capital");
            demonym = jsonObject.getString("demonym");
            nativeName = jsonObject.getString("nativeName");
            flag = jsonObject.getString("flag");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getCountryName() {
        return countryName;
    }

    public String getName() {
        return name;
    }

    public String getCapital() {
        return capital;
    }

    public String getDemonym() {
        return demonym;
    }

    public String getNativeName() {
        return nativeName;
    }

    public String getStatus() {
        return status;
    }

    public String getFlag() {
        return flag;
    }
}
