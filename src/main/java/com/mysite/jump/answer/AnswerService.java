package com.mysite.jump.answer;

import com.mysite.jump.DataNotFoundException;
import com.mysite.jump.question.Question;
import com.mysite.jump.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
        return answer;
    }

    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

    public void vote(Answer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser);
        answer.setRecommend(answer.getVoter().size());
        this.answerRepository.save(answer);
    }

    public Page<Answer> getList(Question question, int page, int size, String sortOption) {
        Sort sort;
        if (sortOption.equals("oldest")) {
            sort = Sort.by(Sort.Order.asc("createDate"));
        } else if (sortOption.equals("recommend")) {
            sort = Sort.by(Sort.Order.desc("recommend"));
        } else {
            sort = Sort.by(Sort.Order.desc("createDate"));
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return answerRepository.findByQuestion(question, pageable);  // Question에 따른 페이징된 답변 리스트 반환
    }
}
