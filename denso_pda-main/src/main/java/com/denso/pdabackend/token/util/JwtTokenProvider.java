package com.denso.pdabackend.token.util;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.denso.pdabackend.token.dto.UserDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.header}")
    private String header;

    //토큰 만료시간
    @Value("${jwt.token.access-expire-time}")
    private long accessExpireTime;


    
    /**
     * 초기화
     * 생성자가 호출되는 시점은 빈이 초기화되기전 이므로 @postconstruct 사용하여 딱한번만 초기화
     * SecretKey Base64로 인코딩 초기화
     */
    @PostConstruct  //DI 이후 실행되는 초기화 어노테이션 
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes()); 
    }

    /**
     * Jwt 토큰에서 인증정보 조회
     * @param token
     * @param url
     * @return
     */
    public Authentication getAuthentication(String token) {
    	
        //토큰에서 사용자 정보 받아와서 사용함
        UserDetails userDetails = getUserInfo(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "",userDetails.getAuthorities());

    }

    /**
     * DB 접속 없이 토큰에서 사용자 정보 뽑아오기
     * @param token
     * @return
     */
    private UserDto getUserInfo(String token){
    	
    	UserDto userInfo = new UserDto();
    	
		String userId = Jwts.parser()
							.setSigningKey(secretKey)
							.parseClaimsJws(token)
							.getBody()
							.getSubject();
		
		String userCompany = Jwts.parser()
								 .setSigningKey(secretKey)
								 .parseClaimsJws(token)
								 .getBody()
								 .get("company").toString();

		String userFactory = Jwts.parser()
								.setSigningKey(secretKey)
								.parseClaimsJws(token)
								.getBody()
								.get("factory").toString();

		userInfo.setUserId(userId);
		userInfo.setCompany(userCompany);
		userInfo.setFactory(userFactory);
		
		return userInfo;
    }

	/**
	 * Request header에서 token 꺼내오기
	 * @param request
	 * @return
	 */
	public String resolveToken(HttpServletRequest request) {
		
		String token = null;
		
		/*cookie에서 토큰산출*/
		if(request.getCookies()!=null) {
			
			token = Arrays.stream(request.getCookies())
				.filter(c->c.getName().equals("denso-pda"))
				.findFirst()
				.map(Cookie::getValue)
				.orElse(null);
		}else {
			//local storage에서 산출할경우
			token = request.getHeader(header);
			//헤더가 'Bearer'로 시작하는지 검사.
			if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
				return token.substring(7); //'Bearer'을 제외한 문자열만 반환해주도록 처리
			}
		}
		
		return token;
	}

	
	/**
	 * token validation
     * 예외가 발생하면 AuthenticationEntryPoint
	 * @param token
	 * @return
	 */
	public boolean validateToken(String token) {
		
		try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException exception) {
            log.info("잘못된 Jwt 토큰입니다");
        } catch (ExpiredJwtException exception) {
            log.info("만료된 Jwt 토큰입니다");
        } catch (UnsupportedJwtException exception) {
            log.info("지원하지 않는 Jwt 토큰입니다");
        }
		
		return false;
	}

    public String createAccessToken(UserDto userDto) {
    	
    	Claims claims = Jwts.claims().setSubject(userDto.getUserId());	//로그인 id를 토큰subject로 넣음.
    	
    	//회사코드, 공정코드등 정보를 토큰의 playload에 넣어 보관
    	claims.put("company", userDto.getCompany());	
    	claims.put("factory", userDto.getFactory());
    	
    	Date now = new Date();
    	
    	return Jwts.builder()
    			.setHeaderParam("typ", "JWT")  //header
    			.setClaims(claims)	//playload (데이터)
    			.setIssuedAt(now)	//토큰발행일자
    			.setExpiration(new Date(now.getTime()+accessExpireTime))	//토큰 만료일 설정
    			.signWith(SignatureAlgorithm.HS256, secretKey)		//암호화
    			.compact();
    }
    

}
