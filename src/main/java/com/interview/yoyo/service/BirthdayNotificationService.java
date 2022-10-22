package com.interview.yoyo.service;

import com.interview.yoyo.dao.People;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.client.RestTemplate;


@Service
public class BirthdayNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BirthdayNotificationService.class);
    private final String API_URI = "http://localhost:9292/list/all";

    @Autowired
    EmailService emailService;
    @Autowired
    RestTemplate restTemplate;


    @Scheduled(fixedDelay = 20000)
    public void birthdayNotificationJSONFile() throws IOException, ParseException {
        LOGGER.info("::::::::::::Reading data from Json file");
        JSONParser parser = new JSONParser();
        JSONArray a = (JSONArray) parser.parse(new FileReader("/home/kibe/Springboot/yoyo/people.json"));

        for (Object o : a) {
            JSONObject person = (JSONObject) o;
            String dob = (String) person.get("dob");
            String[] dateArray = dob.split("/");
            if (dateChecker(dateArray)) {
                String names = (String) person.get("firstName") + " " + (String) person.get("lastName");
                String email = (String) person.get("email");
                mailSender(names, email);
            }
        }
    }

    @Scheduled(fixedDelay = 20000)
    public void birthdayNotificationCSVFile() throws FileNotFoundException {
        LOGGER.info("::::::::::::::::::: Reading data from CSV file");
        try (BufferedReader br = new BufferedReader(new FileReader("/home/kibe/Springboot/yoyo/people.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] personInfo = line.split(",");
                String[] dateArray = personInfo[2].split("/");

                if (dateChecker(dateArray)) {
                    String names = personInfo[0] + " " + personInfo[1];
                    String email = personInfo[3];
                    mailSender(names, email);
                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Scheduled(fixedDelay = 20000)
    public void birthdayNotificationAPIDataFetch() {
        LOGGER.info("::::::::::::Fetching data from an API");

            try{
        ResponseEntity<List<People>> rateResponse = restTemplate.exchange(API_URI, HttpMethod.GET, null, new ParameterizedTypeReference<List<People>>() {
        });
        List<People> people = rateResponse.getBody();

        for (People user : people) {
            String[] dateArray = user.getDob().split("/");
            if (dateChecker(dateArray)) {
                String names = user.getFirstName() + " " + user.getLastName();
                mailSender(names, user.getEmail());
            }

        }}catch(Exception e){


            }

    }

    public boolean dateChecker(String[] dateArray) {
        LOGGER.info("::::::::::::Conditional Statement to check date match");

        if (Integer.valueOf(dateArray[0]) % 4 != 0 && Integer.valueOf(dateArray[1]) == 2 && Integer.valueOf(dateArray[2]) == 29 && LocalDateTime.now().getMonthValue() == 2 && LocalDateTime.now().getDayOfMonth() == 28) {
            return true;
        } else if (LocalDateTime.now().getMonthValue() == Integer.valueOf(dateArray[1]) && LocalDateTime.now().getDayOfMonth() == Integer.valueOf(dateArray[2])) {
            return false;

        }
        return false;
    }


    private void mailSender(String name, String email) {
        LOGGER.info("::::::::::::::: Sending Email Asynchronously");
        new Thread(new Runnable() {
            public void run() {
                String subject = "Happy Birthday";
                String emailBody = "Dear " + name + "," + "\n" + "\n" + "Congratulations on being even more experienced. I’m not sure what you learned this year, but every experience transforms us into the people we are today." + "\n" + "Happy birthday!"
                        + "\n\n" + "Regards" + "\n" + "YOYO |SaltPay HR Department";

                emailService.sendMail(email, subject, emailBody);

            }
        }).start();

    }

    public List<People> listAllPeople() throws IOException, ParseException {
        LOGGER.info("::::::::::::Reading data from Json file and sending for API Consumption");
        JSONParser parser = new JSONParser();
        JSONArray a = (JSONArray) parser.parse(new FileReader("/home/kibe/Springboot/yoyo/people.json"));
        List<People> people= new ArrayList<>();


        for (Object o : a) {
            JSONObject person = (JSONObject) o;
            String dob = (String) person.get("dob");
            People user= new People();

            user.setDob(dob);
            user.setFirstName((String)person.get("firstName") );
            user.setLastName((String) person.get("lastName"));
            user.setEmail((String) person.get("email"));
                 people.add(user)    ;

        }
        return people;
    }


}
