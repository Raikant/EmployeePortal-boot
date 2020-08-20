package com.example.employee.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.stereotype.Service;

import com.example.employee.model.Employee;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtUtility {
	private RSAPrivateKey privateRSAKey;
	private RSAPublicKey publicRSAKey;
	private static final String ID = "39dffe42-9f06-44a8-a3b0-0f6927d308ac";
	private static String SUBJECT = "1234567890";
	private static final String SECRET = "secret";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String AUTHTOKEN = "authtoken";
	private static final String NAME = "name";
	
	@PostConstruct
	public void init() throws NoSuchAlgorithmException, InvalidKeySpecException {
		java.security.Security.addProvider(
		         new org.bouncycastle.jce.provider.BouncyCastleProvider()
		);
		String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2ADxn35BuLzTXUyzwteRpyKF0uiAPYoeGbbVbzHI7VAe2nV3uwfDASu23taNx0EKJuWGbaK978IriGOULx8PwJIo1tTGrJYGAL6tveaNQNE9MIO0t5SG47TCdh+x9Mvm9axcE8seW1+ljUxcE7U8Ca37eAxafNuIic3zROxe9XQod7iJ5OFt30kb8XKy1NArkIvqjhDoZh7q63WH21nZ99pSxr2z8Vy03pVFZEobQzIoX9pqN0EBRYevA5efylWboUFOcZ54Fu6+cWWxXwnbgp/PHA/eQblKxSOtPr236OOTbXVQIsDvYOZjXiLGxVQrvjb0AYIfMClFlEc9z0rhuwIDAQAB";
		String privKey="MIIEowIBAAKCAQEA2ADxn35BuLzTXUyzwteRpyKF0uiAPYoeGbbVbzHI7VAe2nV3uwfDASu23taNx0EKJuWGbaK978IriGOULx8PwJIo1tTGrJYGAL6tveaNQNE9MIO0t5SG47TCdh+x9Mvm9axcE8seW1+ljUxcE7U8Ca37eAxafNuIic3zROxe9XQod7iJ5OFt30kb8XKy1NArkIvqjhDoZh7q63WH21nZ99pSxr2z8Vy03pVFZEobQzIoX9pqN0EBRYevA5efylWboUFOcZ54Fu6+cWWxXwnbgp/PHA/eQblKxSOtPr236OOTbXVQIsDvYOZjXiLGxVQrvjb0AYIfMClFlEc9z0rhuwIDAQABAoIBAFCncZFDXJj1cNrapBix04Ib4upQxGm2I+0oFuQRK9xo2UxrJkzg5hCO2Ra1+HSZNYg2lg5kARYAY67mt9Msfv5B1Rv6kUCqOA2ZJVjOOJomRvnRg+40eHWX7jCbPyCP7mp3dF4zrt8hhLiQ9aSqPIPEwiweOL98XaxPCXIH7KErkcAun8pJAyKFbim2+1FAmHdBEvrV6GS9moWEE2w01qXEU71rYKJZljdTdV6wbgdHCkYtWVcFrDeTNoWm4AXCOfNadY19vnzIz48Jp07YQH93x76wjV8dMc+EEkuzxb/9H4LKOYSkBuKLGMCaKht8Yv8GrImkTgLTaW07pENRR5kCgYEA6407Z5ylE91TM1ykJdAdQLmvXO0wlzRDOx+v/C8alM9h3xVoIVP9YgxDTUPaOqzfkwKAmY8WMz+G3lW3aXHsZShEm8+DcKISgxXp9ykgdbHNlbAKQGsGBEpkvMXZOI7VyunM7yQQYN6RWfEs1aCYARU6k8aWvYQkusCFrXSOjVcCgYEA6sFJvM7SwA17q7ncBlYZg86SC4LNDi7VuJJihNwK2YvShNochr5y6WdAZbfowDZcG6T5KDmfMgVhp6wNKF9wPEMbVxDAl0NoA1PwPg912S/BUQdBRu3dxSi9j3dlWGgYJS6UBz+ODLLUzGBTAmCEPDMRnfWX+gpSj3x3Pb3R7D0CgYEAmKxr6pZfcOatfMb5JgEkVMWF4mdibT7UrMe3G2+0fOuF9kA7xA5aV2i3XqcyFYHjT7f5fo59xy3PzuAkB1Zt8IsPHSZ9fU2vTgOwKCEiBub95524p1Q3DCTj8m8SglQiQmkUCZSIgSEBeRiXBMgYfesykoHPRnsWC22pt2mq0/UCgYB6B12gMtaklJ2rx6JS/OCofnG2YBjdfYUFO7rQm11cdm1JN+PKOyeaolx8Q5xOhjNc/2Ww/jr9koSP10TDrtC10qBcos2qE5T23eQtLnYxHrzbtOy+xRkZyrK25/abfQjIEy1Jk0Tw3uj1EEE2JBu4+0b2GFsW09TU13tYcdq0dQKBgHASNdpXc9bf8M+naEETRWcKPX2t7jufNTgpt1X3MoYX9bv5QYHvrVAmADXwbFWc640Je3/3dGlYnxl9RPN9tAEliUiKuPEhuPI0FD679WkhelEIbbiPeGeJdbgdGPZmLNSlxgvirIHh3DkCsMYcTxolxgLEQ3/P/5LEXe9f81cQ";
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(Base64.getDecoder().decode(pubKey.getBytes()));
		publicRSAKey = (RSAPublicKey) keyFactory.generatePublic(pubSpec);
		PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privKey.getBytes()));
		privateRSAKey = (RSAPrivateKey) keyFactory.generatePrivate(privSpec);
	}
	
	public Employee parseJWT(String jwt) {
		long id = 0;
		String name = null;
		String username = null;
		boolean authenticate;
		String authToken = null;
		
		log.info("Claims:{}",Jwts.parser()
				.setSigningKey(SECRET.getBytes(StandardCharsets.UTF_8))
				.parseClaimsJws(jwt));
		Jws<Claims> claims = Jwts.parser()
				.setSigningKey(SECRET.getBytes(StandardCharsets.UTF_8))
				.parseClaimsJws(jwt);
		if (null != claims.getBody().get("id")) {
			id = Long.valueOf(claims.getBody().get("id").toString());
		}
		
		username = (String) claims.getBody().get(USERNAME);
		name = (String) claims.getBody().get(NAME);
		authToken = (String) claims.getBody().get(AUTHTOKEN);
		authenticate = true;
		 
		return Employee.builder().authToken(authToken).employeeId(id).name(name).userName(username)
				.authenticate(authenticate).build();
	}
	
	public String getJWT(Employee employee) {
		String jwt = null;
		jwt = Jwts
				.builder()
				.setSubject(SUBJECT)
				.setId(ID)
				.setExpiration(Date.from(Instant.ofEpochSecond(1518770392)))
				.setIssuedAt(new Date())
				.setExpiration(
						Date.from(Instant.ofEpochSecond(System
								.currentTimeMillis() + 1000 * 60 * 10)))
				.claim("id", employee.getEmployeeId())
				.claim(USERNAME, employee.getUserName())
				.signWith(SignatureAlgorithm.HS256,
						SECRET.getBytes(StandardCharsets.UTF_8)).compact();
		return jwt;
	}
	
	public String decryptMessage(String encryptedText) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
		cipher.init(Cipher.DECRYPT_MODE, privateRSAKey);
		return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
	}
	
	public String encryptMessage(String plainText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException  {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, publicRSAKey);
		return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
	}
}
