# 카카오페이 코딩 테스트

## 카카오페이 뿌리기 기능 구현하기

### 요구 사항
- 뿌리기, 받기, 조회 기능을 수행하는 REST API 를 구현합니다.
  - 요청한 사용자의 식별값은 숫자 형태이며 "X-USER-ID" 라는 HTTP Header로 전달됩니다.
  - 요청한 사용자가 속한 대화방의 식별값은 문자 형태이며 "X-ROOM-ID" 라는 HTTP Header로 전달됩니다.
  - 모든 사용자는 뿌리기에 충분한 잔액을 보유하고 있다고 가정하여 별도로 잔액에 관련된 체크는 하지 않습니다.
  - 작성하신 어플리케이션이 다수의 서버에 다수의 인스턴스로 동작하더라도 기능에 문제가 없도록 설계되어야 합니다.
  - 각 기능 및 제약사항에 대한 단위테스트를 반드시 작성합니다.

### 상세 구현 요건 및 제약사항
1. 뿌리기 API
- 다음 조건을 만족하는 뿌리기 API를 만들어 주세요.
  - 뿌릴 금액, 뿌릴 인원을 요청값으로 받습니다.
  - 뿌리기 요청건에 대한 고유 token을 발급하고 응답값으로 내려줍니다.
  - 뿌릴 금액을 인원수에 맞게 분배하여 저장합니다. (분배 로직은 자유롭게 구현해 주세요.)
  - token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다.
  
2. 받기 API
- 다음 조건을 만족하는 받기 API를 만들어 주세요.
  - 뿌리기 시 발급된 token을 요청값으로 받습니다.
  - token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를 API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.
  - 뿌리기 당 한 사용자는 한번만 받을 수 있습니다.
  - 자신이 뿌리기한 건은 자신이 받을 수 없습니다.
  - 뿌린기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.
  - 뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 합니다
  
3. 조회 API
- 다음 조건을 만족하는 조회 API를 만들어 주세요.
  - 뿌리기 시 발급된 token을 요청값으로 받습니다.
  - token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. 현재 상태는 다음의 정보를 포함합니다.
  - 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은 사용자 아이디] 리스트)
  - 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지 않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
  - 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.
  
기술 제약사항
  - 개발 언어는 Java, kotlin, scala 중 익숙한 개발 언어를 선택하여 과제를 진행해주시면 됩니다.
  - 핵심 문제해결 전략을 간단하게 작성하여 readme.md 파일에 첨부 해 주세요.
  - 데이터베이스 사용에는 제약이 없습니다.
  - API 의 HTTP Method들 (GET | POST | PUT | DEL) 은 자유롭게 선택하세요.
  - 에러응답, 에러코드는 자유롭게 정의해주세요.  