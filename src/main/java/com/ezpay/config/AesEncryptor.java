package com.ezpay.config;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.SerializationUtils;

import jakarta.persistence.AttributeConverter;
import lombok.SneakyThrows;

public class AesEncryptor implements AttributeConverter<Object, String>{
	
	@Value("${aes.encryption.key}")
	private String encryptionKey;
	
	private static final String encryptionCipher = "AES";
	
	private Key key;
	private Cipher cipher;
	
	

	public Key getKey() {
		if(key==null) {
			key = new SecretKeySpec(encryptionKey.getBytes(), encryptionCipher);
		}
		return key;
	}
	
	private void initCipher(int encryptMode) throws GeneralSecurityException {
		getCipher().init(encryptMode, getKey());
	}

	public Cipher getCipher() throws GeneralSecurityException {
		if(cipher == null) {
			cipher = Cipher.getInstance(encryptionCipher);
		}
		return cipher;
	}

	@SneakyThrows
	@Override
	public String convertToDatabaseColumn(Object attribute) {
	    if (attribute == null) {
	        return null;
	    }

	    try {
			initCipher(Cipher.ENCRYPT_MODE);
		} catch (GeneralSecurityException e) {
			
//			e.printStackTrace();
		}
	    byte[] bytes = SerializationUtils.serialize(attribute);
	    byte[] encryptedBytes = null;
		try {
			encryptedBytes = getCipher().doFinal(bytes);
		} catch (IllegalBlockSizeException e) {
			
//			e.printStackTrace();
		} catch (BadPaddingException e) {
			
//			e.printStackTrace();
		} catch (GeneralSecurityException e) {
		
//			e.printStackTrace();
		}
	    return Base64.getEncoder().encodeToString(encryptedBytes);
	}



	@Override
	public Object convertToEntityAttribute(String dbData) {
		
		if(dbData == null)
			return null;
		 try {
			initCipher(Cipher.DECRYPT_MODE);
		} catch (GeneralSecurityException e) {
			
//			e.printStackTrace();
		}
		byte[] bytes = null;
		try {
			bytes = getCipher().doFinal(Base64.getDecoder().decode(dbData));
		} catch (IllegalBlockSizeException e) {
			
//			e.printStackTrace();
		} catch (BadPaddingException e) {
			
//			e.printStackTrace();
		} catch (GeneralSecurityException e) {
		
//			e.printStackTrace();
		}
		 return SerializationUtils.deserialize(bytes);
	}

}
