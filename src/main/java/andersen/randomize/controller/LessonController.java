package andersen.randomize.controller;

import andersen.randomize.dao.LessonRepository;
import andersen.randomize.dao.StudentRepository;
import andersen.randomize.entity.Lesson;
import andersen.randomize.entity.Student;
import andersen.randomize.service.wrapper.StudentListWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class LessonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LessonController.class);

    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;

    public LessonController(LessonRepository lessonRepository, StudentRepository studentRepository) {
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/choseLessonDate")
    String goToChoseLessonDatePage(Model model) {
        model.addAttribute("lesson", new Lesson());
        return "chose_lesson_date";
    }

    @PostMapping("/choseDate")
    String showStudentByDate(@Valid @ModelAttribute("lesson") Lesson lesson, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "chose_lesson_date";
        }
        if (lesson.getDate().isBefore(LocalDate.now())) {
            List<Student> students = studentRepository.findAllByDate(lesson.getDate());
            LOGGER.debug("Students are: {} were at date {}", students, lesson.getDate());
        } else {
            lessonRepository.save(lesson);//create new lesson
            List<Student> students = StreamSupport.stream(studentRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
            StudentListWrapper studentWrapper = new StudentListWrapper();
            studentWrapper.setStudents((ArrayList<Student>) students);
            studentWrapper.setLesson(lesson);
            model.addAttribute("wrapper", studentWrapper);
        }
        return "list";
    }
}
