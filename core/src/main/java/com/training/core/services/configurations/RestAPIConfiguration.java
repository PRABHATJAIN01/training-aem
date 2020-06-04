package com.training.core.services.configurations;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The interface Training Google API Configuration.
 */
@ObjectClassDefinition(name = "Training Rest API Configuration", description = "Rest API configurations")
public @interface RestAPIConfiguration {

    /**
     * url for rest API.
     *
     * @return the string
     */
    @AttributeDefinition(name = "URL", description = "URL for Rest API")
    String url();
}
