package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.exception.comment.CommentNotFoundException;
import ru.practicum.exception.comment.InvalidCommentException;
import ru.practicum.exception.post.InvalidPostException;
import ru.practicum.exception.post.PostNotFoundException;
import ru.practicum.exception.tag.InvalidTagException;
import ru.practicum.exception.tag.TagNotFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(Throwable throwable, Model model) {
        String reason = throwable.getMessage();
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), reason, throwable.getMessage(), LocalDateTime.now());
        model.addAttribute("error", error);
        return "error";
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String exceptionNotFound(CommentNotFoundException e, Model model) {
        String reason = "Комментарий с таким uuid не найден." + e.getMessage();
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.toString(), reason, e.getMessage(), LocalDateTime.now());
        model.addAttribute("error", error);
        return "error";
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String exceptionNotFound(PostNotFoundException e, Model model) {
        String reason = "Пост с таким uuid не найден.";
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.toString(), reason, e.getMessage(), LocalDateTime.now());
        model.addAttribute("error", error);
        return "error";
    }

    @ExceptionHandler(TagNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String exceptionNotFound(TagNotFoundException e, Model model) {
        String reason = "Тег с таким uuid не найден.";
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.toString(), reason, e.getMessage(), LocalDateTime.now());
        model.addAttribute("error", error);
        return "error";
    }

    @ExceptionHandler(InvalidTagException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String exceptionNotFound(InvalidTagException e, Model model) {
        String reason = "Неверный формат тега.";
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.toString(), reason, e.getMessage(), LocalDateTime.now());
        model.addAttribute("error", error);
        return "error";
    }

    @ExceptionHandler(InvalidPostException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String exceptionNotFound(InvalidPostException e, Model model) {
        String reason = "Неверный формат поста.";
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.toString(), reason, e.getMessage(), LocalDateTime.now());
        model.addAttribute("error", error);
        return "error";
    }

    @ExceptionHandler(InvalidCommentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String exceptionNotFound(InvalidCommentException e, Model model) {
        String reason = "Неверный формат комментария.";
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.toString(), reason, e.getMessage(), LocalDateTime.now());
        model.addAttribute("error", error);
        return "error";
    }
}
