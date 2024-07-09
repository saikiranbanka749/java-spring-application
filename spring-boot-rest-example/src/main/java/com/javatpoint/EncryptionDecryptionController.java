package com.javatpoint;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/encryptionORdecryption")
public class EncryptionDecryptionController {
	private static byte[] sharedvector = { 0x01, 0x02, 0x03, 0x05, 0x07, 0x0B, 0x0D, 0x11 };

	@GetMapping(value = "/gettest")
	public String getTest() {
		return "hiii";
	}

	@GetMapping(value = "/getEncryptionDecryption")
	public String getEncryptionDecryption(@RequestParam("rawtext") String RawText,
			@RequestParam("encryptionordecryption") String encryptionordecryption) {
		System.out.println("receved the request");
		if (encryptionordecryption.equalsIgnoreCase("encryption")) {
			String EncText = "";
			byte[] keyArray = new byte[24];
			String key = "vensaiVtrack";
			byte[] toEncryptArray = null;
			try {
				toEncryptArray = RawText.getBytes("UTF-8");
				MessageDigest m = MessageDigest.getInstance("MD5");
				byte[] temporaryKey = m.digest(key.getBytes("UTF-8"));
				if (temporaryKey.length < 24) {
					int index = 0;
					for (int i = temporaryKey.length; i < 24; i++) {
						keyArray[i] = temporaryKey[index];
					}
				}
				Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
				c.init(1, new SecretKeySpec(keyArray, "DESede"), new IvParameterSpec(sharedvector));
				byte[] encrypted = c.doFinal(toEncryptArray);
				EncText = Base64.encodeBase64String(encrypted);
			} catch (Exception e) {
			}

			return EncText;

		} else if (encryptionordecryption.equalsIgnoreCase("decryption")) {

			byte[] keyArray = new byte[24];
			byte[] temporaryKey;
			String key = "vensaiVtrack";
			try {
				MessageDigest m = MessageDigest.getInstance("MD5");
				temporaryKey = m.digest(key.getBytes("UTF-8"));
				System.out.println(":::" + temporaryKey);
				if (temporaryKey.length < 24) {
					int index = 0;
					for (int i = temporaryKey.length; i < 24; i++) {
						keyArray[i] = temporaryKey[index];
					}
				}
				Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
				c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyArray, "DESede"), new IvParameterSpec(sharedvector));
				byte[] decrypted = c.doFinal(Base64.decodeBase64(RawText));
				RawText = new String(decrypted, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("=======RawText====>" + RawText);
			return RawText;
		}

		return null;

	}

}
