package com.tcc.tccchallenge.controllers;

import com.google.common.collect.Lists;
import com.tcc.tccchallenge.models.Campus;
import com.tcc.tccchallenge.models.Student;
import com.tcc.tccchallenge.models.User;
import com.tcc.tccchallenge.repositories.Campuses;
import com.tcc.tccchallenge.repositories.Students;
import com.tcc.tccchallenge.repositories.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MasterController {

    private final Users userDao;
    private final Students studentDao;
    private final Campuses campusDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public MasterController(Users userDao, Students studentDao, Campuses campusDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.studentDao = studentDao;
        this.campusDao = campusDao;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        return "hello";
    }

    @GetMapping("/register")
    public String showSignupForm(Model model){
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            return "redirect:/welcome";
        }
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user, @RequestParam(name = "username") String username, @RequestParam(name = "email") String email, @RequestParam(name = "password") String password, @RequestParam(name = "password-confirm") String confirm, HttpServletRequest request) {
        if (!password.equals(confirm)) {
            return "redirect:/register";
        }
        String hash = passwordEncoder.encode(password);
        user.setPassword(hash);
        user.setUsername(username);
        user.setEmail(email);
        userDao.save(user);

        request.getSession();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication registeredUser = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(registeredUser);

        return "redirect:/";
    }

    @GetMapping("users/edit")
    public String editUserForm(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/users/edit")
    public String saverUserUpdate(@ModelAttribute User user, @RequestParam(name = "username") String username, @RequestParam(name = "email") String email, @RequestParam(name = "password") String password, @RequestParam(name = "password-confirm") String confirm) {
        if (!password.equals(confirm)) {
            return "redirect:/users/edit";
        }
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String hash = passwordEncoder.encode(password);
        user.setId(currentUser.getId());
        user.setPassword(hash);
        user.setUsername(username);
        user.setEmail(email);
        userDao.save(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        userDetails.setUsername(username);
        userDetails.setEmail(email);
        userDetails.setPassword(hash);
        return "redirect:/";
    }

    @GetMapping("/welcome")
    public String welcomeUser(Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "landing";
    }

    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam(name = "search-params") String params, @RequestParam (name = "query") String query) {
        List<Student> searchResults;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        switch (params) {
            case("1"):
                searchResults = studentDao.findAllByNameContaining(query);
                model.addAttribute("formatter", formatter);
                model.addAttribute("searchResults", searchResults);
                break;
            case("2"):
                searchResults = studentDao.findAllByGradeLevelContaining(query);
                model.addAttribute("formatter", formatter);
                model.addAttribute("searchResults", searchResults);
                break;
            case("3"):
                searchResults = studentDao.campusNameSearch(query);
                model.addAttribute("formatter", formatter);
                model.addAttribute("searchResults", searchResults);
                break;
            case("4"):
                searchResults = studentDao.campusIdSearch(query);
                model.addAttribute("formatter", formatter);
                model.addAttribute("searchResults", searchResults);
                break;
            case("5"):
                List<Student> oneSec = Lists.newArrayList(studentDao.findAll());
                searchResults = new ArrayList<>();
                for (Student student : oneSec){
                    String dateString = student.getEntryDate().toString();
                    if (dateString.contains(query)) {
                        searchResults.add(student);
                    }
                }
                model.addAttribute("formatter", formatter);
                model.addAttribute("searchResults", searchResults);
                break;
            default:
                searchResults = studentDao.findAllByNameContaining(query);
                searchResults.addAll(studentDao.findAllByGradeLevelContaining(query));
                searchResults.addAll(studentDao.campusNameSearch(query));
                searchResults.addAll(studentDao.campusIdSearch(query));
                List<Student> oneSecond = Lists.newArrayList(studentDao.findAll());
                searchResults = new ArrayList<>();
                for (Student student : oneSecond){
                    String dateString = formatter.format(student.getEntryDate());
                    if (dateString.contains(query)) {
                        searchResults.add(student);
                    }
                }
                Set<Student> holdUp = new HashSet<>(searchResults);
                searchResults = new ArrayList<>(holdUp);
                model.addAttribute("formatter", formatter);
                model.addAttribute("searchResults", searchResults);
                model.addAttribute("query", query);
                break;
        }
        return "search/results";
    }

    @GetMapping("/search/results")
    public String showResults(@ModelAttribute(name = "searchResults") ArrayList<Student> searchResults, @ModelAttribute(name = "query") String query) {
        return "search/results";
    }

    @GetMapping("/students")
    public String showStudents(Model model) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        model.addAttribute("formatter", formatter);
        model.addAttribute("allStudents", studentDao.findAll());
        return "all-students";
    }

    @GetMapping("/campuses/{id}")
    public String showStudents(Model model, @PathVariable String id) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Campus destination = campusDao.findOneByCampusId(id);
        model.addAttribute("formatter", formatter);
        model.addAttribute("campusName", destination.getName());
        model.addAttribute("campusStudents", studentDao.findAllByCampus(destination));
        return "campus-students";
    }

    @GetMapping("/campuses")
    public String showCampuses(Model model) {
        model.addAttribute("allCampuses", campusDao.findAll());
        return "all-campuses";
    }

    @GetMapping("/students/add")
    public String getAddStudentForm(Model model){
        List<String> takenIds = new ArrayList<>();
        List<Student> allStudents = Lists.newArrayList(studentDao.findAll());
        for (Student student : allStudents){
            takenIds.add(student.getStudentId());
        }
        model.addAttribute("student", new Student());
        model.addAttribute("takenIds", takenIds);
        model.addAttribute("allCampuses", campusDao.findAll());
        return "add";
    }

    @PostMapping("/students/add")
    public String newStudent(@ModelAttribute Student student, @RequestParam(name = "name") String name, @RequestParam(name = "month") String month, @RequestParam(name = "day") String day, @RequestParam(name = "year") String year, @RequestParam(name = "gradeLevel") String gradeLevel, @RequestParam(name = "studentId") String studentId, @RequestParam(name = "campus_id") String campus_id) {
        long newId = Long.parseLong(campus_id);
        student.setCampus(campusDao.findOne(newId));
        student.setGradeLevel(gradeLevel);
        student.setName(name);
        student.setStudentId(studentId);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month + "-" + day);
            student.setEntryDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        studentDao.save(student);
        return "redirect:/students";
    }

    @GetMapping("/students/{id}")
    public String editStudent(Model model, @PathVariable String id) {
        Student student = studentDao.findOneByStudentId(id);
        model.addAttribute("id", id);
        model.addAttribute("student", student);
        model.addAttribute("date", student.getEntryDate().toString());
        model.addAttribute("allCampuses", campusDao.findAll());
        return "edit";
    }

    @PostMapping("/students/{id}")
    public String changeStudent(@PathVariable String id, @ModelAttribute Student student, @RequestParam(name = "name") String name, @RequestParam(name = "month") String month, @RequestParam(name = "day") String day, @RequestParam(name = "year") String year, @RequestParam(name = "gradeLevel") String gradeLevel, @RequestParam(name = "studentId") String studentId, @RequestParam(name = "campus_id") String campus_id) {
        long newId = Long.parseLong(campus_id);
        student.setCampus(campusDao.findOne(newId));
        student.setGradeLevel(gradeLevel);
        student.setName(name);
        student.setStudentId(studentId);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month + "-" + day);
            student.setEntryDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        studentDao.save(student);
        return "redirect:/students";
    }

    @PostMapping("/students/{id}/delete")
    public String deleteStudent(@PathVariable String id){
        Student deleted = studentDao.findOneByStudentId(id);
        studentDao.delete(deleted);
        return "redirect:/students";
    }
}
