package com.jk.learn.configserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigServerApplicationTests {

    @Test
    public void contextLoads() {
        try {
            Enumeration<URL> enumeration = getClass().getClassLoader().getResources("META-INF/spring.factories");
            while (enumeration.hasMoreElements()) {
                System.out.println(enumeration.nextElement().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
