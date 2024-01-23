package com.jfudali.coursesapp.quiz.service;

import com.jfudali.coursesapp.answer.model.Answer;
import com.jfudali.coursesapp.course.model.Course;
import com.jfudali.coursesapp.exceptions.AlreadyExistsException;
import com.jfudali.coursesapp.exceptions.NotFoundException;
import com.jfudali.coursesapp.lesson.service.LessonService;
import com.jfudali.coursesapp.lesson.model.Lesson;
import com.jfudali.coursesapp.lesson.repository.LessonRepository;
import com.jfudali.coursesapp.ownership.model.Ownership;
import com.jfudali.coursesapp.ownership.model.OwnershipKey;
import com.jfudali.coursesapp.ownership.repository.OwnershipRepository;
import com.jfudali.coursesapp.ownership.service.OwnershipService;
import com.jfudali.coursesapp.quiz.dto.CreateQuizDto;
import com.jfudali.coursesapp.quiz.dto.UpdateQuizDto;
import com.jfudali.coursesapp.quiz.dto.UserCompletedQuizDto;
import com.jfudali.coursesapp.quiz.dto.UserQuestionAnswerDto;
import com.jfudali.coursesapp.quiz.model.Quiz;
import com.jfudali.coursesapp.quiz.repository.QuizRepository;
import com.jfudali.coursesapp.user.model.User;
import com.jfudali.coursesapp.user.repository.UserRepository;
import com.jfudali.coursesapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final LessonService lessonService;
    private final LessonRepository lessonRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final OwnershipRepository ownershipRepository;
    public Quiz createQuiz(Integer courseId, Integer lessonId, CreateQuizDto createQuizDto) throws NotFoundException {
        Lesson lesson = lessonService.getLessonById(courseId, lessonId);
        if(lesson.getQuiz() != null) throw new AlreadyExistsException("Lesson already has a quiz");
        Quiz quiz = Quiz.builder()
                    .title(createQuizDto.getTitle())
                    .build();
        lesson.setQuiz(quiz);
        lessonRepository.save(lesson);
        return quiz;
    }
    public Quiz updateQuiz(Integer courseId, Integer lessonId, UpdateQuizDto updateQuizDto) throws NotFoundException {
        Lesson lesson = lessonService.getLessonById(courseId, lessonId);
        if(lesson.getQuiz() == null) throw new NotFoundException("Quiz not found");
        lesson.getQuiz().setTitle(updateQuizDto.getTitle());
        lessonRepository.save(lesson);
        return lesson.getQuiz();
    }
    public void deleteQuiz(Integer courseId, Integer lessonId) throws NotFoundException {
        Lesson lesson = lessonService.getLessonById(courseId, lessonId);
        if(lesson.getQuiz() == null) throw  new NotFoundException("Quiz not found");
        Set<User> users = lesson.getQuiz().getExaminee();
        users.forEach(u -> u.getQuizzes().remove(lesson.getQuiz()));
        lesson.setQuiz(null);
        lessonRepository.save(lesson);
    }
    @Transactional
    public boolean checkUserPassedQuiz(Integer courseId, Integer lessonId,
                                       UserCompletedQuizDto userCompletedQuizDto,
                                       String userEmail){
        Lesson lesson = lessonService.getLessonById(courseId, lessonId);
        User user = userService.getUserByEmail(userEmail);
        Quiz quiz = lesson.getQuiz();
        if(quiz == null) throw  new NotFoundException("Quiz not found");
        if(quiz.getQuestions().isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There are no questions in this course to give answer on");
        if(user.getQuizzes().contains(quiz)) throw new AlreadyExistsException("You already passed this quiz");
        AtomicInteger correctAnswersCounter = new AtomicInteger();
        quiz.getQuestions().forEach(question -> {
            List<Integer> correctAnswers = question.getAnswers().stream().filter(Answer::getIsCorrect).map(Answer::getIdanswer).toList();
            Optional<UserQuestionAnswerDto> questionInUserResponse = userCompletedQuizDto.getUserQuestionsAnswers().stream()
                    .filter(userQuestionAnswerDto -> userQuestionAnswerDto.getQuestionId()
                            .equals(question.getIdquestion())).findFirst();
            if(questionInUserResponse.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You do not answer on all questions");
            if(new HashSet<>(questionInUserResponse.get().getSelectedAnswers()).containsAll(correctAnswers) && new HashSet<>(correctAnswers).containsAll(questionInUserResponse.get().getSelectedAnswers())){
                correctAnswersCounter.getAndIncrement();
            }
        });
        boolean correct = quiz.getQuestions().size() == correctAnswersCounter.get();
        if(correct){
            user.getQuizzes().add(quiz);
            userRepository.save(user);
            int courseLessonsNumber = lesson.getCourse().getLessons().stream().filter(lesson1 -> lesson1.getQuiz() != null).toList().size();
            int userPassedQuizzesOfCourse = user.getQuizzes().stream().filter(passedQuiz -> Objects.equals(passedQuiz.getLesson().getCourse().getIdcourse(), courseId)).toList().size();
            if(courseLessonsNumber == userPassedQuizzesOfCourse){
                Ownership ownership = ownershipRepository.findById(new OwnershipKey(courseId, user.getIduser())).orElseThrow(() -> new NotFoundException("User owned course with provided course id not found"));
                ownership.setIsCompleted(true);
                ownershipRepository.save(ownership);
            }
        }
        return correct;
    }
}
