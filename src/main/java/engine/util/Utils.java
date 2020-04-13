package engine.util;

import engine.domain.Completion;
import engine.domain.Quiz;
import engine.dto.CompletionDto;
import engine.dto.QuizDto;

public class Utils {

    public static QuizDto convertQuizEntityToDto(Quiz quiz) {
        QuizDto quizDto = new QuizDto();
        quizDto.setId(quiz.getId());
        quizDto.setTitle(quiz.getTitle());
        quizDto.setText(quiz.getText());
        quizDto.setOptions(quiz.getOptions());
        return quizDto;
    }

    public static Quiz convertQuizDtoToEntity(QuizDto quizDto) {
        Quiz quiz = new Quiz();
        quiz.setId(quizDto.getId());
        quiz.setTitle(quizDto.getTitle());
        quiz.setText(quizDto.getText());
        quiz.setOptions(quizDto.getOptions());
        quiz.setAnswer(quizDto.getAnswer());
        return quiz;
    }

    public static CompletionDto convertCompletionEntityToDto(Completion completion) {
        CompletionDto completionDto = new CompletionDto();
        completionDto.setId(completion.getQuiz().getId());
        completionDto.setCompletedAt(completion.getCompletedAt());
        return completionDto;
    }
}
