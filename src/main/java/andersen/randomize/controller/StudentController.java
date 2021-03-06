package andersen.randomize.controller;

import andersen.randomize.entity.Student;
import andersen.randomize.service.StudentService;
import andersen.randomize.service.wrapper.StudentListWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StudentController {

    private final StudentService studentService;
    private List<Student> presentStudent;

    public StudentController(andersen.randomize.service.StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/presentStudents")
    String getAllPresent(@ModelAttribute("studentList") StudentListWrapper studentWrapper, Model model) {
        studentService.getPresentedStudentById(studentWrapper); //this return rows only with id
        return "redirect:/start";
    }

    @GetMapping("/start")
    String startGame(Model studentsGradeWrapper) {
        List<Student> players = studentService.findRandomPlayers();
        StudentListWrapper listWrapper = new StudentListWrapper();
        listWrapper.setStudents((ArrayList<Student>) players);
        if (players.isEmpty()) {
            return "redirect:/start"; //redirect to list with today's students
        }
        studentsGradeWrapper.addAttribute("studentsGradeWrapper", listWrapper);
        return "random";
    }

    @PostMapping("/estimate")
    String estimateStudent(@ModelAttribute("studentsGradeWrapper") StudentListWrapper studentsGradeWrapper) {
        if (studentsGradeWrapper.getStudents().isEmpty()) {

        }
        studentService.changeStudentGrade(studentsGradeWrapper);
        return "redirect:/start";
    }
}
