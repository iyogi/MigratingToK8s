package liveproject.m2k8s;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
1. Start MySQL database server before starting this application
2. Create jar: mvn package spring-boot:repackage
3. Run jar: java -jar target/profiles.jar
*/
@SpringBootApplication
public class ProfilesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProfilesApplication.class, args);
    }
}
