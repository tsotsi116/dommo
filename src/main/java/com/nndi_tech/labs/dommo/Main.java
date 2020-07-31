package com.nndi_tech.labs.dommo;

import com.google.gson.Gson;
import com.nndi_tech.labs.dommo.routes.RequestMobileMoneyPaymentVerificationRoute;
import com.nndi_tech.labs.dommo.routes.VerifyMobileMoneyPaymentByTxn;
import org.slf4j.LoggerFactory;
import spark.Spark;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.getenv;
import static spark.Spark.redirect;

/**
 * Entry point for our service
 */
public class Main {
    private static final HashMap<String, String> corsHeaders = new HashMap<String, String>();

    public static void main(String... args) throws Exception {
        new Main().run(args);
    }

    private void run(String... args) throws Exception {
        final Gson gson = new Gson();


        Map<String, String> persistenceUnitProps = new HashMap<>();
        persistenceUnitProps.put("hibernate.connection.url",
            getenv().getOrDefault("DATABASE_URL", "jdbc:postgresql://localhost:5432/dommo"));
        persistenceUnitProps.put("hibernate.connection.username",
            getenv().getOrDefault("DATABASE_USERNAME", "dommo"));
        persistenceUnitProps.put("hibernate.connection.password",
            getenv().getOrDefault("DATABASE_PASSWORD", "dommo123"));
        EntityManagerFactory sessionFactory;
        try {
            sessionFactory = Persistence.createEntityManagerFactory(
                "com.nndi_tech.labs.dommo",
                persistenceUnitProps
            );
        } catch(Exception e) {
            throw new RuntimeException("Failed to start service", e);
        }

        UserService userService = new UserService(sessionFactory);
        MobileMoneyService mobileMoneyService = new MobileMoneyService(
            sessionFactory,
            userService
        );
        NotificationsService notificationsService = new NotificationsServiceImpl();

        Spark.ipAddress(getenv().getOrDefault("HOST", "0.0.0.0"));
        Spark.port(Integer.parseInt(getenv().getOrDefault("PORT", "4567")));

        // root is 'src/main/resources', so put files in 'src/main/resources/public'
        Spark.staticFiles.location("/public");

        addCORS();

        redirect.get("/", "/public/index.html");

        Spark.get("/auth", (request, response) -> {
            response.type("application/json;charset=utf-8");
            return gson.toJson("Not authenticated/authorized");
        });

        Spark.post("/request-verification", new RequestMobileMoneyPaymentVerificationRoute(gson, mobileMoneyService, notificationsService));
        Spark.post("/verify/manual", new VerifyMobileMoneyPaymentByTxn(gson, mobileMoneyService, notificationsService));
        // TODO: Spark.post("/verify/sms", new RequestMobileMoneyPaymentVerificationRoute());
        // TODO: Spark.post("/verify/network", new RequestMobileMoneyPaymentVerificationRoute());
        // TODO: Spark.get("/verifications/all", (request, response) -> "Not Implemented");
        // TODO: Spark.get("/verifications/pending", (request, response) -> "Not Implemented");
        // TODO: Spark.get("/verifications/verified", (request, response) -> "Not Implemented");
        // TODO: Spark.get("/verifications/cancelled", (request, response) -> "Not Implemented");

        LoggerFactory.getLogger(Main.class).info("========== API RUNNING =================");
    }

    static {
        corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
        corsHeaders.put("Access-Control-Allow-Origin", "*");
        corsHeaders.put("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
        corsHeaders.put("Access-Control-Allow-Credentials", "true");
    }

    public static void addCORS() {
        Spark.after((request, response) -> {
            corsHeaders.forEach(response::header);
        });
    }
}
