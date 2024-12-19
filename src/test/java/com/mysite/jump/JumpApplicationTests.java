package com.mysite.jump;

import com.mysite.jump.answer.Answer;
import com.mysite.jump.answer.AnswerRepository;
import com.mysite.jump.question.Question;
import com.mysite.jump.question.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JumpApplicationTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void testJpa1() {
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);  // 첫번째 질문 저장

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);  // 두번째 질문 저장
    }

    @Test
    void testJpa2() {
        List<Question> questionList = this.questionRepository.findAll();
        assertThat(questionList.size()).isEqualTo(2);

        Question question = questionList.get(0);
        assertThat(question.getSubject()).isEqualTo("sbb가 무엇인가요?");
    }

    @Test
    void testJpa3() {
        Optional<Question> optionalQuestion = this.questionRepository.findById(1);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            assertThat(question.getSubject()).isEqualTo("sbb가 무엇인가요?");
        }
    }

    @Test
    void testJpa4() {
        Question question = this.questionRepository.findBySubject("sbb가 무엇인가요?");
        assertThat(question.getId()).isEqualTo(1);
    }

    @Test
    void testJpa5() {
        Question question = this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
        assertThat(question.getId()).isEqualTo(1);
    }

    @Test
    void testJpa6() {
        List<Question> questionList = this.questionRepository.findBySubjectLike("sbb%");
        Question question = questionList.get(0);
        assertThat(question.getSubject()).isEqualTo("sbb가 무엇인가요?");
    }

    @Test
    void testJpa7() {
        Optional<Question> optionalQuestion = this.questionRepository.findById(1);
        assertTrue(optionalQuestion.isPresent());
        Question question = optionalQuestion.get();
        question.setSubject("수정된 제목");
        this.questionRepository.save(question);
    }

    @Test
    void testJpa8() {
        assertThat(this.questionRepository.count()).isEqualTo(2);
        Optional<Question> optionalQuestion = this.questionRepository.findById(1);
        assertTrue(optionalQuestion.isPresent());
        Question question = optionalQuestion.get();
        this.questionRepository.delete(question);
        assertThat(this.questionRepository.count()).isEqualTo(1);
    }

    @Test
    void testJpa9() {
        Optional<Question> optionalQuestion = this.questionRepository.findById(2);
        assertTrue(optionalQuestion.isPresent());
        Question question = optionalQuestion.get();

        Answer answer = new Answer();
        answer.setContent("네 자동으로 생성됩니다.");
        answer.setQuestion(question);
        answer.setCrateDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    @Test
    void testJpa10() {
        Optional<Answer> optionalAnswer = this.answerRepository.findById(1);
        assertTrue(optionalAnswer.isPresent());
        Answer answer = optionalAnswer.get();
        assertThat(answer.getQuestion().getId()).isEqualTo(2);
    }

    @Transactional
    @Test
    void testJpa11() {
        Optional<Question> optionalQuestion = this.questionRepository.findById(2);
        assertTrue(optionalQuestion.isPresent());
        Question question = optionalQuestion.get();

        List<Answer> answerList = question.getAnswerList();

        assertThat(answerList.size()).isEqualTo(1);
        assertThat(answerList.get(0).getContent()).isEqualTo("네 자동으로 생성됩니다.");
    }
}
