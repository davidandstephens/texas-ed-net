package com.tcc.tccchallenge.controllers;

import com.google.common.collect.Lists;
import com.tcc.tccchallenge.models.Student;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class MasterController {
    @GetMapping("/")
    public String homePage(Model model) {
        return "hello";
    }

//    @PostMapping("/search")
//    public String makeSearch(Model model, @RequestParam(name = "search-params") String params, @RequestParam (name = "query") String query) {
//        List<Student> searchResults;
//        switch (params) {
//            case(1)
//        }
//        return "search/results";
//    }

    @GetMapping("/search/results")
    public String showResults(@ModelAttribute(name = "searchResults") ArrayList<Student> searchResults) {
        return "search/results";
    }

}
