package com.cosain.trilo.auth.application;

import com.cosain.trilo.auth.domain.LogoutAccessToken;
import com.cosain.trilo.auth.domain.repository.TokenRepository;
import com.cosain.trilo.auth.infra.TokenAnalyzer;
import com.cosain.trilo.auth.infra.TokenProvider;
import com.cosain.trilo.auth.presentation.dto.RefreshTokenStatusResponse;
import com.cosain.trilo.common.exception.NotExistRefreshTokenException;
import com.cosain.trilo.common.exception.NotValidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;
    private final TokenAnalyzer tokenAnalyzer;

    @Transactional
    public String reissueAccessToken(String refreshToken){
        checkIfValidTokenOrThrow(refreshToken);
        checkTokenExistenceOrThrow(refreshToken);
        String email = tokenAnalyzer.getEmailFromToken(refreshToken);
        return tokenProvider.createAccessToken(email);
    }
    private void checkIfValidTokenOrThrow(String refreshToken){
        if(!tokenAnalyzer.validateToken(refreshToken)){
            throw new NotValidTokenException();
        }
    }
    private void checkTokenExistenceOrThrow(String refreshToken){
        if(!tokenRepository.existsRefreshTokenById(refreshToken)){
            throw new NotExistRefreshTokenException();
        }
    }

    public RefreshTokenStatusResponse createTokenStatus(String token) {
        boolean availability = tokenAnalyzer.validateToken(token);
        return RefreshTokenStatusResponse.from(availability);
    }

    /**
     * @param authHeaderValue
     * @param refreshToken
     *
     * 해당 기능은 다음과 같은 역할을 합니다.
     *
     * 1. 기존 접근 토큰의 남은 만료 시간 만큼의 생명 주기를 가지는 '로그아웃 접근 토큰' 을 생성해서
     * 토큰 저장소에 저장합니다.
     *
     * 이후 토큰 유효성 검사를 할 때, '로그아웃 접근 토큰'과 비교한다면 해당 토큰이 유효하지 않음을 판별할 수 있습니다.
     *
     * 2. 기존 재발급 토큰을 토큰 저장소에서 삭제 합니다.
     *
     * 이후 재발급 요청을 할 때, 해당 토큰의 존재 유무를 검사하기 때문에
     *
     */

    @Transactional
    public void logout(String authHeaderValue, String refreshToken) {
        String accessToken = getAccessTokenFrom(authHeaderValue);
        Long remainExpiry = tokenAnalyzer.getTokenRemainExpiryFrom(accessToken);
        tokenRepository.saveLogoutAccessToken(LogoutAccessToken.of(accessToken, remainExpiry));
        tokenRepository.deleteRefreshTokenById(refreshToken);
    }

    private String getAccessTokenFrom(String authHeaderValue){
        return authHeaderValue.substring(7);
    }

}
