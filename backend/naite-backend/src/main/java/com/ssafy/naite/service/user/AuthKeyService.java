package com.ssafy.naite.service.user;

import com.ssafy.naite.domain.user.AuthKey;
import com.ssafy.naite.domain.user.AuthKeyRepository;
import com.ssafy.naite.domain.user.User;
import com.ssafy.naite.domain.user.UserRepository;
import com.ssafy.naite.dto.user.AuthKeySaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class AuthKeyService {
    private final AuthKeyRepository authKeyRepository;
    private final UserRepository userRepository;

    @Transactional
    public void save(AuthKeySaveRequestDto authKeySaveRequestDto) {
        authKeyRepository.save(authKeySaveRequestDto.toEntity());
    }

    @Transactional
    public void update(Integer type, String email, String key) throws Exception{
        User user = userRepository.findByUserEmail(email).get();
        AuthKey authKey = authKeyRepository.findByUserAndAuthType(user,type).orElseThrow(() ->
                new Exception("존재하지 않는 회원입니다."));
        authKey.updateKey(key);
    }

    @Transactional
    public void compare(String userEmail, String certified, Integer keyType) throws Exception{
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(() ->
                new Exception("존재하지 않는 회원입니다."));
        AuthKey authKey = authKeyRepository.findByUserAndAuthType(user,keyType).get();
        if (certified.equals(authKey.getAuthKey())) {
            authKey.updateKey("success");
            authKeyRepository.save(authKey);
        } else {
            throw new Exception("인증 키가 일치하지 않습니다.");
        }
    }
}