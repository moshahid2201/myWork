public class BruteForce {

	public int array[];
	public String password;
	public static String passTest;
	public static String randomDigits;
	public static int Counter;
	public static long Factor;
	public boolean Match;

	public BruteForce() {

		array = new int[10];
		password = new String();
		passTest = new String("0");
		Counter = 1;
		Factor = 10;
		Match = false;
		randomDigits = new String();

	}

	public static boolean bruteForceKeyGenerator(int iterationCount,
			String plainText, String cipherText, PasswordBasedEncrypDecrypt atk)
			throws Exception {

		randomDigits = "";
		long mult = Factor / 10;
		boolean Match = false;
		String t1 = new String();
		String t2;
		for (int l = 0; l < Counter; l++)
			t1 += "0";
		// System.out.println(t1);
		for (int i = 0; i < 10; i++) {
			passTest = Integer.toString(i) + t1;
			mult = Factor / 10;
			while (mult < Factor) {

				for (long j = 0; j < Factor; j++) {
					randomDigits = passTest + String.valueOf(j);

					if (randomDigits.length() > passTest.length()) {

						while (randomDigits.length() != passTest.length()) {
							randomDigits = randomDigits.substring(0, 1)
									+ randomDigits.substring(2,
											randomDigits.length());
						}

					}
					System.out.println(randomDigits);

					// System.out.println(plainText+"Random Digits->"+randomDigits+"Iteration count:"+iterationCount+"  Cipher produced:"+t2);
					if (atk.encryption(plainText, randomDigits, iterationCount)
							.equals(cipherText)) {
						Match = true;
						System.out.println("pasword cracked");
						System.out.println("Password was: " + randomDigits);
						break;
					}

				}

				if (!Match) {
					mult *= 10;
					// Counter++;
					passTest += Integer.toString(0);
				} else
					return true;
			}
		}
		if (Match)
			return true;
		else
			return false;

	}

	public boolean BruteForceAlgo(String plainText, String cipher, int count,
			int passLength) throws Exception {

		PasswordBasedEncrypDecrypt attack = new PasswordBasedEncrypDecrypt();

		String test;

		for (int i = 0; i < 10; i++) {
			array[i] = i;
		}
		// for single character password
		for (int l = 0; l < 10; l++) {
			// test=attack.encryption(plainText,Integer.toString(l),2048);
			// System.out.println("Im generated cipher "+test
			// +" for iteration :"+Integer.toString(l));
			if (attack.encryption(plainText, Integer.toString(l), count)
					.equals(cipher)) {
				Match = true;
				System.out.println("pasword cracked");
				System.out.println("Password was: " + Integer.toString(l));
				return true;
			}
		}

		if (!Match) {
			// length of the keys
			for (int i = 0; i < passLength - 1; i++) {
				Factor *= 10;
				Counter++;

			}
			Match = bruteForceKeyGenerator(count, plainText, cipher, attack);

		}

		return Match;
	}
}
