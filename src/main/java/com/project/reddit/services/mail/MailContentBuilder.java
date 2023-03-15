package com.project.reddit.services.mail;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
//at run thymeleaf will automatically add the email message in html template
//takes the email message we want to send to user as input
@Service
@AllArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    String build(String message){
        Context context=new Context();
        //set email message in thymeleaf context object
         context.setVariable("message",message);
         //passing the html file name and template engine
         return templateEngine.process("mailTemplate",context);
    }
}
