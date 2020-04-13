package engine.service;

import engine.domain.Completion;
import engine.domain.Quiz;
import engine.domain.User;
import engine.dto.QuizDto;
import engine.exception.QuizNotFoundException;
import engine.exception.UserNotFoundException;
import engine.repository.CompletionRepository;
import engine.repository.QuizRepository;
import engine.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Set;

import static engine.util.Utils.*;

@Service
public class QuizService {

    private QuizRepository quizRepository;
    private UserRepository userRepository;
    private CompletionRepository completionRepository;

    public QuizService(QuizRepository quizRepository, UserRepository userRepository, CompletionRepository completionRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.completionRepository = completionRepository;
    }

    public long create(QuizDto quizDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        Quiz quiz = convertQuizDtoToEntity(quizDto);
        quiz.setCreatedBy(user);
        return quizRepository.save(quiz).getId();
    }

    public Quiz get(long id) {
        return quizRepository.findById(id).orElseThrow(QuizNotFoundException::new);
    }

    public Page<Quiz> getAll(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    public boolean solve(long id, Set<Integer> answer, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        Quiz quiz = get(id);
        Set<Integer> correctAnswer = quiz.getAnswer();

        boolean correct = Objects.equals(answer, correctAnswer);

        if (correct) {
            completionRepository.save(new Completion(user, quiz));
        }

        return correct;
    }

    public Quiz update(long id, QuizDto quizDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        Quiz quiz = get(id);
        if (Objects.equals(quiz.getCreatedBy().getId(), user.getId())) {
            quiz.setTitle(quizDto.getTitle());
            quiz.setText(quizDto.getText());
            quiz.setOptions(quizDto.getOptions());
            quiz.setAnswer(quizDto.getAnswer());
            return quizRepository.save(quiz);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    public void delete(long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        Quiz quiz = get(id);
        if (Objects.equals(quiz.getCreatedBy().getId(), user.getId())) {
            quizRepository.delete(quiz);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    public Page<Completion> getAllCompleted(String username, Pageable pageable) {
        return completionRepository.findAllByUsername(username, pageable);
    }
}
