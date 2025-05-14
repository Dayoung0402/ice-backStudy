# advICE code

- main > exception > BusinessException + GlobalExceptionHandler

---

### 1. code info

- BusinessException

```java
@Getter
public class BusinessException extends RuntimeException {

	private final String code;         // 응답용 에러 코드 (예: "NE", "DE", "DBE")
	private final HttpStatus status;   // HTTP 상태 코드 (예: 400, 403, 500)

	public BusinessException(String code, String message, HttpStatus status) {
		super(message);
		this.code = code;
		this.status = status;
	}
}
```

- GlobalExceptionHandler

```java
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ResponseDto<Void>> handleBusinessException(BusinessException e) {
		log.warn("[비즈니스 로직 에러 발생] {}", e.getMessage());
		return ResponseEntity
			.status(e.getStatus())
			.body(ResponseDto.fail(e.getCode(), e.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseDto<Void>> handleUnexpectedException(Exception e) {
		log.warn("[비즈니스에서 잡지 못하는 에러 발생] {}", e.getMessage());
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR) // ex.getStatus()는 HttpStatus 반환하도록 정의
			.body(ResponseDto.fail("IE", "서버 내부 오류가 발생했습니다."));
	}

	@ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
	public ResponseEntity<ResponseDto<Void>> handleValidationException(Exception e){
		log.warn("[요청 HTTP BODY 검증 에러] {}", e.getMessage());

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ResponseDto.fail("VF", "Validation failed."));
	}

	@ExceptionHandler({ DataAccessException.class, SQLException.class })
	public ResponseEntity<ResponseDto<Void>> handleDatabaseException(Exception e) {
		log.warn("[DB 에러] {}", e.getMessage());
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ResponseDto.fail("DBE", "Database error."));
	}
}
```

### 2. 어노테이션 정보

- @Slf4j → log 찍는 거를 가능하게 해주는 클래스 선언 어노테이션

⇒ 즉, 이 어노테이션을 입력함으로써 로그 정보를 찍을 수 있음.

- @RestControllerAdvice → @RestResponse + @ControllerAdvice

⇒ 즉, json  형식으로 http 응답 메시지가 클라이언트로 갈 것인데 이때 스프링이 알아서 ResponseDto 객체 정보들을 json에 맞춰서 메시지 바디에 데이터를 넣어주는 것

### 3. 예외처리 설명에서 추가해서 알아야 하는 내용

- 전역 예외 처리 메소드가 존재하는 핸들러 클래스의 전체적인 코드 구조는 동일
- 다만, 예외 처리 클래스가 설명의 예시코드보다 다양함

→ Exception.class 뿐만 아니라

1. MethodArgumentNotVaildException
2. HttpmessageNotReadableException
3. DataAccessException
4. SQLExcpetion

→ 이렇게 4개의 예외 처리 클래스가 사용됨

- 이 4개의 클래스들은 모두 Exception을 상속받은 하위 클래스
- 1번 ~ 3번은 Exception > RuntimeExcpetion > 여기 위치
- 4번은 Exception > 여기 위치
- 즉, 이 4개들은 Excpetion 클래스를 예외 처리 객체로 선언했을 때, 놓칠 수 있는 예외 처리 기능을 가능하게 한다.
- 4개는 1번부터 4번까지 우선순위 순서를 갖는다. 다시 말해서

⇒ 	@ExceptionHandler() 의 () 내부에 2개의 예외 처리 클래스를 넣을 경우(1번과 2번을 넣는 다고 가정)에 로직상 오류가 없게 되는 것임

🚨 예외가 발생 → 해당 예외 처리 객체가 생성이 됨 → 해당 예외 처리 클래스에 가장 맞는 예외 처리 메서드를 실행시키는 흐름