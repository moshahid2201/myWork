import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class PasswordBasedEncrypDecrypt {
	public PBEKeySpec pbeKeySpec;
	public PBEParameterSpec pbeParamSpec;
	public SecretKeyFactory keyFac;
	public Cipher pbeCipher;

	public byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
			(byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };
	// public int count;
	public char[] password;
	public byte[] cleartext;
	public static byte[] ciphertext;

	public String encryption(String text, String Password, int count)
			throws Exception {
		pbeParamSpec = new PBEParameterSpec(salt, count);

		password = Password.toCharArray();

		pbeKeySpec = new PBEKeySpec(password);

		// Create instance of SecretKeyFactory for password-based encryption
		// using DES and MD5
		keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

		// Generate a key
		Key pbeKey = keyFac.generateSecret(pbeKeySpec);

		// Create PBE Cipher
		Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

		// Initialize PBE Cipher with key and parameters
		pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

		cleartext = text.getBytes();

		// Encrypt the plaintext

		ciphertext = pbeCipher.doFinal(cleartext);
		// System.out.println("cipher : " + Utils.toHex(ciphertext));

		return Utils.toHex(ciphertext);
		// return ciphertext.toString() ;

	}

	public String decryption(String text, String Password, int count)
			throws Exception {
		pbeParamSpec = new PBEParameterSpec(salt, count);

		password = Password.toCharArray();

		pbeKeySpec = new PBEKeySpec(password);

		// Create instance of SecretKeyFactory for password-based encryption
		// using DES and MD5
		keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

		// Generate a key
		Key pbeKey = keyFac.generateSecret(pbeKeySpec);

		// Create PBE Cipher
		Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

		// Initialize PBE Cipher with key and parameters
		pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
		// decrypt the ciphertext
		byte[] plaintext = pbeCipher.doFinal(ciphertext);
		String StringPlaintext = new String(plaintext);
		// return ciphertext.toString() ;
		return StringPlaintext;

	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		BruteForce test = new BruteForce();
		String cipher;
		String decodedtext;
		String inputText;
		String iterationCount;
		String password;
		String choice;
		String passwordDecrypt;
		PasswordBasedEncrypDecrypt PBE = new PasswordBasedEncrypDecrypt();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter Text To Encrypt: ");
		System.out.println();
		inputText = br.readLine();

		System.out.print("Enter Iteration Count: ");
		System.out.println();
		iterationCount = br.readLine();

		System.out.print("Enter Password: ");
		System.out.println();
		password = br.readLine();

		System.out.println();
		System.out.println("*************************************");

		cipher = (PBE.encryption(inputText, password,
				Integer.parseInt(iterationCount)));
		// decodedtext = (PBE.decryption(cipher, "1230", 2048));
		// System.out.println(decodedtext);
		System.out.println("Generated Cipher Text: " + cipher);
		System.out.println();
		System.out.println("*************************************");

		System.out.println();
		System.out.println("Enter Password  to Decrypt the Cipher Text:");
		System.out.println();
		passwordDecrypt = br.readLine();
		while (!passwordDecrypt.equals(password)) {
			System.out
					.println("Please Enter Correct Password  to Decrypt the Cipher Text:");
			System.out.println();
			passwordDecrypt = br.readLine();
		}
		System.out.println("*************************************");

		// decodedtext = (PBE.decryption(cipher, "1230", 2048));
		// System.out.println(decodedtext);
		System.out.println("Generated Cipher Text: " + cipher);
		System.out.println();
		decodedtext = (PBE.decryption(cipher, passwordDecrypt,
				Integer.parseInt(iterationCount)));
		System.out.println("Plain Text: " + decodedtext);
		System.out.println();
		System.out.println("*************************************");

		System.out.println();
		System.out.println("***************Attack Mode********************");
		System.out
				.println("Press Y if you know Iteration Count, else press Enter:");
		System.out.println();
		choice = br.readLine();
		if (choice.equalsIgnoreCase("y")) {
			boolean flag = false;
			System.out.println("Please enter Iteration Count");
			System.out.println();
			iterationCount = br.readLine();
			long starttime = System.currentTimeMillis();
			flag=test.BruteForceAlgo(decodedtext, cipher,
					Integer.parseInt(iterationCount), password.length()-1);
			long endtime = System.currentTimeMillis();
			System.out.println("Execution Time in Seconds: "+((endtime - starttime) / 1000));
			System.out.println(flag);

		} else {
			boolean flag = false;
			long starttime = System.currentTimeMillis();
			while (!flag) {
				for (int i = 1; i < 5000; i++) {
                    test=new BruteForce();
					flag = test.BruteForceAlgo(decodedtext, cipher, i,
							password.length()-1);
					if(flag)break;

				}
			}
			long endtime = System.currentTimeMillis();
			System.out.println("Execution Time in Seconds: "+((endtime - starttime) / 1000));
		}
		

	}

}
