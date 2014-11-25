package crypto;

import java.util.HashSet;
import java.util.Set;

public class mainClass {

	public static boolean isReasonableValue(int p, int g) {
		Set<Long> set = new HashSet<Long>();
		long temp = g;
		for (int i = 1; i < 71; ++i) {
			temp = (temp * g) % p;
			set.add(temp);
		}
		if (set.size()+1 == p)
			return true;
		else
			return false;
	}
	
	public static int countReasonableValues(int p) {
		Set<Long> set = new HashSet<Long>();
		int count = 0;
		for (int g = 0; g < 71; ++g) {
			long temp = g;
			for (int i = 1; i < 71; ++i) {
				temp = (temp * g) % p;
				set.add(temp);
			}
			if (set.size()+1 == p) {
				count++;
			}
			set.clear();
		}
		return count;
	}
	
	public static int findPrivateKey(int p, int g, int y) {
		int x;
		long temp = g;
		for (x = 1; x < 71; ++x) {
			temp = (temp * g) % p;
			if (temp == y)
				break;
		}
		return x+1;
	}
	
	public static void main(String[] args) {
		boolean res = isReasonableValue(71, 6);
		if (res)
			System.out.println("6 is a reasonable value");
		else
			System.out.println("6 is not a reasonable value");
		
		int resValues = countReasonableValues(71);
		System.out.println("Reasonable values: " + resValues);
		
		int priKey = findPrivateKey(71, 7, 3);
		System.out.println("Private Key: " + priKey);
		
		int priKey2 = findPrivateKey(1048583, 14, 3);
		System.out.println("Private Key: " + priKey2);
	}

}
