package PRNG;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

public class pseudoRandomNumberGenerator {
	
	private static final int keyLength = 256;
	
	private static Key key;
	private BigInteger counter;
	private Cipher AES;
	private MessageDigest sha256;
	
	public pseudoRandomNumberGenerator() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		byte[] k =   {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		byte[] ctr = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);
		key = keyGenerator.generateKey();
		counter = new BigInteger(ctr);
		//key = new SecretKeySpec(k, 0, k.length, "AES");
		sha256 = MessageDigest.getInstance("SHA-256");
		AES = Cipher.getInstance("AES");	
		AES.init(Cipher.ENCRYPT_MODE, key);
	}
	
	public void Reseed(int seed) throws UnsupportedEncodingException {
		String text = key.getEncoded() + Integer.toString(seed);
		sha256.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
		byte[] digest = sha256.digest();
		key = new SecretKeySpec(digest, 0, digest.length, "AES");
		counter.add(new BigInteger("1"));
	}
	
	public String GenerateBlocks(int numBlocks) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		assert(counter.equals(new BigInteger("0")) == false);
		String r = "";
		for (int i = 1; i < numBlocks; ++i)
		{
			r = r + AES.doFinal(r.getBytes());
			counter.add(new BigInteger("1"));
		}
		return r;
	}
	
	public String GenerateData(int numBytes) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		assert(numBytes >= 0 && numBytes <= Math.pow(2, 20));
		System.out.println((int)Math.ceil(numBytes/16));
		String r = GenerateBlocks((int)Math.ceil(numBytes/16));
		//k = GenerateBlocks(2);
		return r;
	}
	
	public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchProviderException {
		pseudoRandomNumberGenerator prng = new pseudoRandomNumberGenerator();
		prng.Reseed(2);
		System.out.println(prng.GenerateData(160));
		
		
		
		//KeyGenerator keygen = KeyGenerator.getInstance("AES");
		//keygen.init(256);
		
	    //SecretKey aesKey = keygen.generateKey();
		//Cipher AES = Cipher.getInstance("AES");
		//AES.init(Cipher.ENCRYPT_MODE, aesKey);
		//System.out.println(aesKey.getEncoded());

		/*MessageDigest md = MessageDigest.getInstance("SHA-256");
		String text = "This is some text";

		md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
		byte[] digest = md.digest();
		System.out.println(digest);*/
	}
}
