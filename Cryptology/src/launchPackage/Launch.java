package launchPackage;

import java.math.BigInteger;

import cast128.CAST128;
import dh.DiffieHellman;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launch extends Application {

	static boolean firstMsg1 = true;
	static boolean tr1fa2 = true;
	public static CAST128 c5;
	public static DiffieHellman dh;
	static BigInteger user1PubK, user2PubK, user1SecK, user2SecK;

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		dh = new DiffieHellman();
		dh.genPrimeAndPrimitiveRoot(); // initialize p and g for DH key exchange
		user1PubK = dh.getAliceMessage(BigInteger.valueOf(123));
		user2PubK = dh.getBobMessage(BigInteger.valueOf(560));
		System.out.println("D-H Key Exchange:\nGenerated Prime (p): " + dh.getP() + "\nGenerated Primitive Root (g): "
				+ dh.getG());
		System.out.println("User 1 chooses Private Key (a) as 123\nUser 2 chooses Private Key (b) as 560");
		System.out.println("User 1 generated key to share publicly ((g^a) mod p): " + user1PubK);
		System.out.println("User 2 generated key to share publicly ((g^b) mod p): " + user2PubK);
		user1SecK = dh.aliceCalculationOfKey(user2PubK, BigInteger.valueOf(123));
		user2SecK = dh.bobCalculationOfKey(user1PubK, BigInteger.valueOf(560));
		System.out.println("User 1 Secret Shared Key (user2PubK^a mod p): " + user1SecK);
		System.out.println("User 2 Secret Shared Key (user1PubK^b mod p): " + user2SecK);
		System.out.println();
		c5 = new CAST128(user1SecK.toString());
		// c5 = new CAST128(user2SecK.toString()); doesn't matter with which key
		// symmetric key is created
		User1 sb = new User1();
		sb.start(primaryStage);
	}
}