package me.scene.paper.updater.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


@RestController
@RequiredArgsConstructor @Slf4j
public class WebHookApiController {

    private static final String COMMAND = "/bin/sh /paper/deploy.sh";

    @Value("${updater.key}")
    private String key;

    @Value("${updater.value}")
    private String value;


    @PostMapping("/deploy")
    public void deploy(HttpServletRequest request) throws IOException, InterruptedException {
        log.info("===== Start Deployment =====");

        if (!value.equals(request.getHeader(key))) {
            log.error("- Stop Deployment (invalid request)");
        }

        Process process = Runtime.getRuntime().exec(COMMAND);
        try (BufferedReader commands = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader errors = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            String command;
            while ((command = commands.readLine()) != null) {
                log.info(command);
            }

            String error;
            while ((error = errors.readLine()) != null) {
                log.warn(error);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            process.waitFor();
            process.destroy();
        }

        log.info("===== Finished Deployment =====");
    }

}
