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


# html 실행순서

1. <head> 태그 파싱 및 처리:
  - <title>, <style>, <script> 등이 실행됨.
  - <script>는 순서대로 실행되며, 이후 코드는 바로 실행됩니다.

2. HTML <body> 태그 파싱:
  - <body>의 HTML 요소가 렌더링되고 DOM에 추가됨.

3. $(function () { ... })와 $(document).ready(function () { ... }) 실행
   - DOM 트리가 완전히 구성된 후 실행됨.

$(function() { ... })
축약형 표현으로, jQuery가 제공하는 $(document).ready()의 단축 문법