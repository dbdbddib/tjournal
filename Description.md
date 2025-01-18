# ajax done responseData
IResponseController 클래스의 makeResponseEntity 메서드는 응답 데이터를 생성하는 공통 메서드

- 입력 매개변수:
    - httpStatus: 응답 HTTP 상태 코드 (e.g., HttpStatus.OK, HttpStatus.BAD_REQUEST).
    - responseCode: 서버에서 정의한 응답 코드 (e.g., R000000).
    - message: 응답에 포함할 메시지 (e.g., "OK", "Error occurred").
    - responseData: 실제로 클라이언트가 사용할 데이터.

- 응답 생성:
  - ResponseDto 객체를 생성합니다.
  - responseCode, message, responseData를 설정합니다.
  - ResponseEntity로 ResponseDto를 감싸서 반환합니다.
  - HTTP 상태 코드와 함께 전송됩니다.


# session, cookies
```
구분	        세션	                          쿠키
저장 위치	서버	                          클라이언트(브라우저)
저장 데이터	사용자 정보 등	                  문자열 데이터
유효 기간	기본적으로 브라우저 종료 시 소멸	  설정된 만료 시간까지 유지
보안	        상대적으로 안전 (서버 관리)	  상대적으로 취약 (클라이언트 관리)
```