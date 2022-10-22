package com.interview.yoyo.controller;


import com.interview.yoyo.dao.People;
import com.interview.yoyo.service.BirthdayNotificationService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value="/list")
public class PeopleController {
    @Autowired
    BirthdayNotificationService birthdayNotificationService;

    @GetMapping("/all")
    public List<People> getAllPeople() throws IOException, ParseException {
        return birthdayNotificationService.listAllPeople();

    }
}
