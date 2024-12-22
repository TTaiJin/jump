package com.mysite.jump.question;

import com.mysite.jump.DataNotFoundException;
import com.mysite.jump.answer.Answer;
import com.mysite.jump.user.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private Specification<Question> search(String kw) {
        return new Specification<Question>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true); //중복 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("content"), "%" + kw + "%"), //제목
                cb.like(q.get("content"), "%" + kw + "%"), //내용
                cb.like(u1.get("username"), "%" + kw + "%"), //질문 작성자
                cb.like(a.get("content"), "%" + kw + "%"), //답변 내용
                cb.like(u2.get("username"), "%" + kw + "%")); //답변 작성자
            }
        };
    }

    private final QuestionRepository questionRepository;

    public List<Question> getList() {
        return this.questionRepository.findAll();
    }


    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content, SiteUser user) {
        Question question = new Question();
        question.setSubject(subject);
        question.setContent(content);
        question.setCreateDate(LocalDateTime.now());
        question.setAuthor(user);
        this.questionRepository.save(question);
    }

    public Page<Question> getList(int page, String kw, String sortOption) {
        Sort sort;
        if (sortOption.equals("oldest")) {
            sort = Sort.by(Sort.Order.asc("createDate"));
        } else if (sortOption.equals("recommend")) {
            sort = Sort.by(Sort.Order.desc("recommend"));
        } else {
            sort = Sort.by(Sort.Order.desc("createDate"));
        }
        Pageable pageable = PageRequest.of(page, 10, sort);
        Specification<Question> specification = search(kw);
        return this.questionRepository.findAll(specification ,pageable);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        question.setRecommend(question.getVoter().size());
        this.questionRepository.save(question);
    }
}
