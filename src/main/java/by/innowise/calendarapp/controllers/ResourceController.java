package by.innowise.calendarapp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    @GetMapping("/hellojhjh")
    public String sayHello() {
        return "Hello";
    }
}
