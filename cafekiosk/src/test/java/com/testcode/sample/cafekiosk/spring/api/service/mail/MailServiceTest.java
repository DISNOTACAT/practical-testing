package com.testcode.sample.cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

import com.testcode.sample.cafekiosk.spring.api.client.mail.MailSendClient;
import com.testcode.sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import com.testcode.sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
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
//    Mockito.when(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
//        .thenReturn(true);

    // given절에 해당할 수 있도록 Mockito를 한번 더 감싼 BDD 스타일 테스트 코드
    BDDMockito.given(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
        .willReturn(true);

    // when
    boolean result = mailService.sendMail("","","", "");


    // then
    Mockito.verify(mailSendHistoryRepository, times(1))
        .save(any(MailSendHistory.class)); // save 가 한번 동작함을 검증
    assertThat(result).isTrue();
  }

}