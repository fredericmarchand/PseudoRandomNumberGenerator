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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

public class pseudoRandomNumberGenerator {
		
	private byte[] key;
	private BigInteger counter;
	private Cipher AES;
	private MessageDigest sha256;
	
	public pseudoRandomNumberGenerator() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		byte[] k =   {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		byte[] ctr = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		
		//Initialize Counter;
		counter = new BigInteger(ctr);
		key = k;
		Key tempKey = new SecretKeySpec(key, "AES");
		
		//Initialize SHA-256
		sha256 = MessageDigest.getInstance("SHA-256");
		
		//Initialize AES
		AES = Cipher.getInstance("AES");	
		AES.init(Cipher.ENCRYPT_MODE, tempKey);
	}
	
	public void Reseed(String seed) throws UnsupportedEncodingException, InvalidKeyException {
		String text = new String(key) + seed;
		sha256.update(text.getBytes());
		byte[] digest = sha256.digest();
		key = digest;
		Key tempKey = new SecretKeySpec(key, "AES");
		AES.init(Cipher.ENCRYPT_MODE, tempKey);
		counter.add(new BigInteger("1"));
	}
	
	public String GenerateBlocks(int numBlocks) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		assert(counter.equals(new BigInteger("0")) == false);
		String r = "";
		for (int i = 0; i < numBlocks; ++i)
		{
			r = r + new String(AES.doFinal(counter.toByteArray()));
			counter.add(new BigInteger("1"));
		}
		return r;
	}
	
	public String GenerateData(int numBytes) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		assert(numBytes >= 0 && numBytes <= Math.pow(2, 20));
		String r = GenerateBlocks((int)Math.ceil((double)numBytes/16.0)).substring(0, numBytes);
		key = GenerateBlocks(2).getBytes();
		Key tempKey = new SecretKeySpec(key, "AES");
		AES.init(Cipher.ENCRYPT_MODE, tempKey);
		return r;
	}
	
	public int generate32BitInteger() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		String data = this.GenerateData(4);
		assert (data.length() == 4);
		ByteBuffer wrapped = ByteBuffer.wrap(data.getBytes());
		return wrapped.getInt();
	}
	
	public long generate64BitInteger() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		String data = this.GenerateData(8);
		assert (data.length() == 8);
		ByteBuffer wrapped = ByteBuffer.wrap(data.getBytes());
		return wrapped.getLong();
	}
	
	public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchProviderException {
		pseudoRandomNumberGenerator prng = new pseudoRandomNumberGenerator();
		prng.Reseed("ASDKLJFHLASDHkjsdhflkasdasdaa");
		
		System.out.println(prng.generate32BitInteger());
		System.out.println(prng.generate64BitInteger());
	}
}
