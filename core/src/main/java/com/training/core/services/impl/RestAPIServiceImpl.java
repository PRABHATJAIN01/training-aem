package com.training.core.services.impl;

import com.training.core.services.RestAPIService;
import com.training.core.services.configurations.RestAPIConfiguration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = RestAPIService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = RestAPIConfiguration.class)

public class RestAPIServiceImpl implements RestAPIService {

    private static final Logger logger = LoggerFactory.getLogger(RestAPIService.class);

    private RestAPIConfiguration config;

    /**
     * Activate.
     *
     * @param config the config
     */
    @Activate
    public void activate(RestAPIConfiguration config) {
        this.config = config;
    }

    @Override
    public String getUrl() {
        return config.url();
    }
}