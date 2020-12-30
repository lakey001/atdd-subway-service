package nextstep.subway.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.BaseTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;

public class MemberAcceptanceTest extends BaseTest {
	public static final String EMAIL = "email@email.com";
	public static final String PASSWORD = "password";
	public static final String NEW_EMAIL = "newemail@email.com";
	public static final String NEW_PASSWORD = "newpassword";
	public static final int AGE = 20;
	public static final int NEW_AGE = 21;
	public static final String 잘못된_토큰 = "abcdef";

	@DisplayName("회원 정보를 관리한다.")
	@Test
	void manageMember() {
		// when
		ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
		// then
		회원_생성됨(createResponse);

		// when
		ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);
		// then
		회원_정보_조회됨(findResponse, EMAIL, AGE);

		// when
		ExtractableResponse<Response> updateResponse = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
		// then
		회원_정보_수정됨(updateResponse);

		// when
		ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
		// then
		회원_삭제됨(deleteResponse);
	}

	@DisplayName("나의 정보를 관리한다.")
	@Test
	void manageMyInfo() {
		//given
		회원_생성을_요청(EMAIL, PASSWORD, AGE);
		TokenResponse tokenResponse = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
		String accessToken = tokenResponse.getAccessToken();

		//when
		ExtractableResponse<Response> getResponse = 내_정보_조회(accessToken);

		//then
		내_정보_조회됨(getResponse);

		//when
		ExtractableResponse<Response> wrongResponse = 잘못된_토큰으로_내_정보_조회(잘못된_토큰);
		정보_조회_거부됨(wrongResponse);

		//when
		ExtractableResponse<Response> updateResponse = 내_정보_수정요청(accessToken, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

		//then
		내_정보_수정됨(updateResponse);

		//when
		TokenResponse reAuthTokenResponse = AuthAcceptanceTest.로그인_요청(NEW_EMAIL, NEW_PASSWORD).as(TokenResponse.class);
		String newToken = reAuthTokenResponse.getAccessToken();
		ExtractableResponse<Response> deleteResponse = 내_정보_삭제요청(newToken);

		//then
		내_정보_삭제됨(deleteResponse);
	}

	public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, Integer age) {
		MemberRequest memberRequest = new MemberRequest(email, password, age);

		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(memberRequest)
			.when().post("/members")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
		String uri = response.header("Location");

		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get(uri)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response, String email,
		String password, Integer age) {
		String uri = response.header("Location");
		MemberRequest memberRequest = new MemberRequest(email, password, age);

		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(memberRequest)
			.when().put(uri)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
		String uri = response.header("Location");
		return RestAssured
			.given().log().all()
			.when().delete(uri)
			.then().log().all()
			.extract();
	}

	public static void 회원_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
		MemberResponse memberResponse = response.as(MemberResponse.class);
		assertThat(memberResponse.getId()).isNotNull();
		assertThat(memberResponse.getEmail()).isEqualTo(email);
		assertThat(memberResponse.getAge()).isEqualTo(age);
	}

	public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 회원_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private ExtractableResponse<Response> 내_정보_조회(String accessToken) {

		return RestAssured
			.given().log().all().auth().oauth2(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/members/me")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> 잘못된_토큰으로_내_정보_조회(String accessToken) {
		return 내_정보_조회(accessToken);
	}

	private ExtractableResponse<Response> 내_정보_수정요청(String accessToken, String email, String password, int age) {
		return RestAssured
			.given().log().all().auth().oauth2(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new MemberRequest(email, password, age))
			.when().put("/members/me")
			.then().log().all()
			.extract();
	}

	private ExtractableResponse<Response> 내_정보_삭제요청(String accessToken) {
		return RestAssured
			.given().log().all().auth().oauth2(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/members/me")
			.then().log().all()
			.extract();
	}

	private void 내_정보_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 정보_조회_거부됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	private void 내_정보_수정됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 내_정보_삭제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}