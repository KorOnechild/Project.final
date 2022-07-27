package com.project.cafesns.service.oauth;

import com.project.cafesns.model.dto.oauth.NaverOAuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NaverService {

    @Value("${naver_client_id}")
    private String clientid;

    @Value("${naver_client_secret}")
    private String clientsecret;

    // AccessToken 받기
    public HttpEntity<MultiValueMap<String,String>> generateAuthCodeRequest(String code, String state){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientid);
        params.add("client_secret", clientsecret);
        params.add("code",code);
        params.add("state",state);
        return new HttpEntity<>(params, headers);
    }

    public String requestAccessToken(HttpEntity request){
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<NaverOAuthDto> responseEntity = restTemplate.exchange("https://nid.naver.com/oauth2.0/token", HttpMethod.POST, request, NaverOAuthDto.class);
        return responseEntity.getBody().getAccess_token();
    }

    public ResponseEntity<String> requestProfile(HttpEntity request){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.GET, request, String.class);
    }

    public String generateProfileRequest(String accessToken){
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/x-www-form-urlencoded");
        headers.add("Authorization","Bearer "+accessToken);
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<NaverInfoOAuthDto> responseEntity = rest.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.GET, requestEntity, NaverInfoOAuthDto.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();

        int status = httpStatus.value();

        NaverInfoOAuthDto response = responseEntity.getBody();

        String nickname = response.getResponse().getNickname();
        String profileImage = response.getResponse().getProfile_image();
        String email = response.getResponse().getEmail();

        System.out.println("Response status: "+status);
        System.out.println(nickname);
        System.out.println(profileImage);
        System.out.println(email);

        return nickname;
    }

    static class NaverInfoOAuthDto {
        public Response getResponse() {
            return response;
        }

        private Response response;

        static class Response{
            String nickname;

            String profile_image;

            String email;

            public String getNickname() {
                return nickname;
            }

            public String getProfile_image() {
                return profile_image;
            }

            public String getEmail() {
                return email;
            }
        }
    }
}