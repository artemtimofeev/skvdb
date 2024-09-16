package org.skvdb;

import org.skvdb.controller.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@ComponentScan
public class Main {

    public static void main(String[] args) throws IOException {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main.class);

        ConnectionListener connectionListener = applicationContext.getBean(ConnectionListener.class);
        //connectionListener.listen();

        System.out.println((applicationContext.getBean(Controller.class).getClass()));
    }
}