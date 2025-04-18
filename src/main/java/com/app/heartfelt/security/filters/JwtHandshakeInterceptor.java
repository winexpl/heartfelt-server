package com.app.heartfelt.security.filters;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.server.HandshakeInterceptor;


public class JwtHandshakeInterceptor  implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                org.springframework.web.socket.WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // Получаем заголовок Authorization
        
        String token = request.getHeaders().getFirst("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Убираем "Bearer "
            attributes.put("token", token); // Сохраняем токен для использования в WebSocket
            System.out.println("Получен токен из WebSocket: " + token);
            return true;
        }
        System.out.println("Отсутствует токен!");
        return false; // Прервать соединение, если токен отсутствует или некорректен
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                            org.springframework.web.socket.WebSocketHandler wsHandler, Exception exception) {
        // Дополнительная логика после завершения рукопожатия
    }
}
