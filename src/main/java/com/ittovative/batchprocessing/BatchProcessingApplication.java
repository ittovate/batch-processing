package com.ittovative.batchprocessing;


import static com.ittovative.batchprocessing.constant.SwaggerConstant.CONTACT_EMAIL;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.CONTACT_NAME;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.CONTACT_URL;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.DESCRIPTION;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.DEVELOPMENT_SERVER_DESCRIPTION;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.DEVELOPMENT_SERVER_URL;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.TITLE;
import static com.ittovative.batchprocessing.constant.SwaggerConstant.VERSION;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * The type Batch processing application.
 */
@SpringBootApplication
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
@OpenAPIDefinition(
        info = @Info(
                title = TITLE,
                description = DESCRIPTION,
                contact = @Contact(
                        email = CONTACT_EMAIL,
                        name = CONTACT_NAME,
                        url = CONTACT_URL
                ),
                version = VERSION
        ),
        servers = {
                @Server(url = DEVELOPMENT_SERVER_URL, description = DEVELOPMENT_SERVER_DESCRIPTION)
        }
)
public class BatchProcessingApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String... args) {
        SpringApplication.run(BatchProcessingApplication.class, args);
    }

}
