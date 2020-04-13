package engine.controller;

import engine.dto.AnswerDto;
import engine.dto.CompletionDto;
import engine.dto.QuizDto;
import engine.domain.Quiz;
import engine.service.QuizService;
import engine.util.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static engine.util.Utils.*;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public QuizDto createQuiz(@Valid @RequestBody QuizDto quizDto, Principal principal) {
        long id = quizService.create(quizDto, principal.getName());
        quizDto.setId(id);
        return quizDto;
    }

    @GetMapping("/{id}")
    public QuizDto getQuiz(@PathVariable long id) {
        Quiz quiz = quizService.get(id);
        return convertQuizEntityToDto(quiz);
    }

    @GetMapping
    public Page<QuizDto> getAllQuizzes(@RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(defaultValue = "id") String sortBy) {
        Pageable paging = PageRequest.of(page, size, Sort.by(sortBy));
        return quizService.getAll(paging).map(Utils::convertQuizEntityToDto);
    }

    @PostMapping("/{id}/solve")
    public AnswerDto solveQuiz(@PathVariable long id, @RequestBody AnswerDto answer, Principal principal) {
        boolean correct = quizService.solve(id, answer.getAnswer(), principal.getName());
        return correct ? AnswerDto.success() : AnswerDto.failure();
    }

    @PutMapping("/{id}")
    public QuizDto updateQuiz(@PathVariable long id, @Valid @RequestBody QuizDto quizDto, Principal principal) {
        Quiz quiz = quizService.update(id, quizDto, principal.getName());
        return convertQuizEntityToDto(quiz);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteQuiz(@PathVariable long id, Principal principal) {
        quizService.delete(id, principal.getName());
    }

    @GetMapping("/completed")
    public Page<CompletionDto> getAllCompletedQuizzes(@RequestParam(defaultValue = "0") Integer page,
                                                      @RequestParam(defaultValue = "10") Integer size,
                                                      Principal principal) {
        Pageable paging = PageRequest.of(page, size, Sort.by("completedAt").descending());
        return quizService.getAllCompleted(principal.getName(), paging).map(Utils::convertCompletionEntityToDto);
    }
}
