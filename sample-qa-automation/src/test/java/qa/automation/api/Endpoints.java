package qa.automation.api;

import qa.automation.config.Config;

public class Endpoints {


    public static final String UPLOAD_PATH = Config.apiConfigBaseUrl() + "/upload";


    public static final String CONFIG_PATH = Config.apiConfigBaseUrl() + "/upload";

    public static final String DELETE_UPDATES = Config.apiDeleteUrl() + "/delete/%s";


    public static String byId(String id) {
    return Config.apiConfigBaseUrl() + id;
  }
  // add more endpoints as needed
}