package yiyan.research.service;

import org.springframework.stereotype.Service;

@Service
public interface RocketMQService {
    String getUserId(String token);

    boolean getUserIsAdmin(String token);

    boolean getWorkIsLikeByUser(String token, String workId);

    boolean sendAuthorApplication(String token, String email);

    boolean authCode(String token, String email, String code);

    boolean  authorAuthenticate(String userId, String authorId);
}
