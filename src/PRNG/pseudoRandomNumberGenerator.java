package PRNG;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class pseudoRandomNumberGenerator {
	
	private static BigInteger key;
	private BigInteger counter;
	private Cipher AES;
	private MessageDigest sha256;
	
	public pseudoRandomNumberGenerator() throws NoSuchAlgorithmException, NoSuchPaddingException {
		byte[] k = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		byte[] ctr = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		key = new BigInteger(k);
		counter = new BigInteger(ctr);
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		Cipher AES = Cipher.getInstance("AES");
	}
	
	public void Reseed(int seed) throws UnsupportedEncodingException {
		String text = key.toString() + Integer.toString(seed);
		sha256.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
		byte[] digest = sha256.digest();
		key = new BigInteger(digest);
		counter.add(new BigInteger("1"));
	}
	
	public String GenerateBlocks(int numBlocks) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		assert(counter.equals(new BigInteger("0")) == false);
		Cipher AES = Cipher.getInstance("AES");
		AES.init(Cipher.ENCRYPT_MODE, (Key) key);
		String r = "";
		for (int i = 1; i < numBlocks; ++i)
		{
			//numBlocks = numBlocks + AES.(key, "plaintext");
			counter.add(new BigInteger("1"));
		}
		return r;
	}
	
	public void GenerateData(int numBytes) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		assert(numBytes >= 0 && numBytes <= Math.pow(2, 20));
		String r = GenerateBlocks((int)Math.ceil(numBytes/16)).substring(0, numBytes);
		//k = GenerateBlocks(2);
	}
	
	public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException {
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		keygen.init(128);
	    SecretKey aesKey = keygen.generateKey();
		Cipher AES = Cipher.getInstance("AES");
		AES.init(Cipher.ENCRYPT_MODE, aesKey);


		/*MessageDigest md = MessageDigest.getInstance("SHA-256");
		String text = "This is some text";

		md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
		byte[] digest = md.digest();
		System.out.println(digest);*/
	}
}
