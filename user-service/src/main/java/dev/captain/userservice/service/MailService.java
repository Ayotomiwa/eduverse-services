package dev.captain.userservice.service;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class MailService {

    private final SpringTemplateEngine templateEngine;
    @Value("fe101b385953c1003e1ce04f9502882c")
    private String apiKey;
    @Value("4a03b06a463c9a12f6599ce68855dddb")
    private String secretKey;

    public MailService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void sendEmail(String to, String subject, Context context, String templateName) throws MailjetException {
        String html = templateEngine.process(templateName, context);

        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient(apiKey, secretKey);
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "omopea2@lsbu.ac.uk"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", to)))
                                .put(Emailv31.Message.SUBJECT, subject)
                                .put(Emailv31.Message.HTMLPART, html)));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());
    }
}
