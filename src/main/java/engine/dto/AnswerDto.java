package engine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class AnswerDto {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Integer> answer = new HashSet<>();

    private boolean success;

    private String feedback;

    public AnswerDto() {
    }

    public AnswerDto(boolean success, String feedback) {
        this.success = success;
        this.feedback = feedback;
    }

    public Set<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(Set<Integer> answer) {
        this.answer = answer;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public static AnswerDto success() {
        return new AnswerDto(true, "Congratulations, you're right!");
    }

    public static AnswerDto failure() {
        return new AnswerDto(false, "Wrong answer! Please, try again.");
    }
}
