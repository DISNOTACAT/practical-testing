package com.testcode.sample.cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

import com.testcode.sample.cafekiosk.spring.api.client.mail.MailSendClient;
import com.testcode.sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import com.testcode.sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) // @Mock 사용시 필수
class MailServiceTest {

  @Mock
  private MailSendClient mailSendClient;
  @Mock
  private MailSendHistoryRepository mailSendHistoryRepository;

  @InjectMocks
  private MailService mailService;

  @DisplayName("메일 전송 테스트")
  @Test
  void sendMail() {
    // given
    // 어노테이션 없이 직접 작성하는 방법
//    MailSendClient mailSendClient = Mockito.mock(MailSendClient.class);
//    MailSendHistoryRepository mailSendHistoryRepository = Mockito.mock(MailSendHistoryRepository.class);
//    MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

    Mockito.when(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
        .thenReturn(true);
//    Mockito.when(mailSendHistoryRepository.save(any(MailSendHistory.class)));
//    mock  객체에 아무것도 지정안하면 예외가 아닌 기본값을 반환,
//    따라서 이부분 없어도 repository 는 그냥 null 을 반환하고 지나감

    // when
    boolean result = mailService.sendMail("","","", "");


    // then
    Mockito.verify(mailSendHistoryRepository, times(1))
        .save(any(MailSendHistory.class)); // save 가 한번 동작함을 검증
    assertThat(result).isTrue();
  }

}