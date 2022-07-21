package com.example.demo;



import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class SessionManger {
	
	 private static final String SESSION_COOKIE_NAME = "mySessionId";
	 
	 private Map<String, Object> sessionStore = new HashMap<>();
	 
	    public void createSession(Object value, HttpServletResponse response){
	        // 세션 id를 생성하고, 값을 세션에 저장
	        String sessionId = UUID.randomUUID().toString();
	        sessionStore.put(sessionId, value);
	
	        // 쿠키 생성
	        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
	        //이렇게 해야 리다이렉트해도 세션 유지됨
	        mySessionCookie.setPath("/");
	        response.addCookie(mySessionCookie);
	    }
	    
	    //쿠기 정보 얻기.
	    public Object getSession(HttpServletRequest request){

	        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
	        
	        System.out.println("쿠키 get: " + sessionCookie);
	        
	        if (sessionCookie == null){
	            return null;
	        }
	        return sessionStore.get(sessionCookie.getValue());
	    }
	    
	    //쿠키 삭제
	    public void expire(HttpServletRequest request){
	        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
	        
	        if (sessionCookie != null){
	        	System.out.println("세션쿠키 확인 지점" + sessionCookie.getValue());
	            sessionStore.remove(sessionCookie.getValue());
	           
	        }
	        System.out.println("쿠키 expire: " + sessionCookie);
	    }
	    
	    //쿠키에서 세션 id 검색 찾기
		private Cookie findCookie(HttpServletRequest request, String cookieName) {
				if(request.getCookies()==null){
					return null;
					
				}

				return Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(cookieName))
						.findAny().orElse(null);
			}
}
