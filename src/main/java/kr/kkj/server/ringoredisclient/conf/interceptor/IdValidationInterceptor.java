package kr.kkj.server.ringoredisclient.conf.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.kkj.server.ringoredisclient.connection.AccountManager;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

public class IdValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String id = request.getParameter("id"); // ID는 URL 파라미터에서 가져옵니다.
        if (id == null || !AccountManager.getInstance().containContext(id)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
