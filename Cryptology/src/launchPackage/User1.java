package launchPackage;

import java.util.ArrayList;

import eceg.ElGamal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class User1 {

	@FXML
	private Text gp;

	@FXML
	private Text gg;

	@FXML
	private Text privateK1;

	@FXML
	private Text privateK2;

	@FXML
	private Text publicK1;

	@FXML
	private Text publicK2;

	@FXML
	private Text secretK1;

	@FXML
	private Text secretK2;

	@FXML
	private Text castK;

	@FXML
	private Text eccA;

	@FXML
	private Text eccB;

	@FXML
	private Text eccP;

	@FXML
	private Text eccBP;

	@FXML
	private Text egPrivateK1;

	@FXML
	private Text egPublicK1;

	@FXML
	private Text egPrivateK2;

	@FXML
	private Text egPublicK2;

	@FXML
	private TextArea plainTxt;

	@FXML
	private TextArea encryptedTxt;

	@FXML
	private Button sendBtn;

	@FXML
	private Button encBtn;

	@FXML
	private TextArea dsTxt;

	@FXML
	private Button decBtn;

	@FXML
	private Button clr;

	@FXML
	private TextField verify;

	static ArrayList<Byte> encryptedMsg;
	static String decryptedMsg, encryptedString;
	static byte[] dSignature;
	static ElGamal eceg1;
	static ElGamal.DataChunk user2Public;
	static ElGamal eceg2;
	static ElGamal.DataChunk user1Public;

	public void initialize() {
		plainTxt.setWrapText(true);
		encryptedTxt.setWrapText(true);
		dsTxt.setWrapText(true);

		if (Launch.firstMsg1 == true) {
			encryptedTxt.setText("");
			dsTxt.setText("");

			System.out.println("Elliptic Curve - ElGamal:");
			eceg1 = new ElGamal();
			ElGamal.flag = 0;
			eceg2 = new ElGamal();
			user1Public = eceg1.public_chunk;
			user2Public = eceg2.public_chunk;
			ElGamal.flag = 0;
			Launch.firstMsg1 = false;
			System.out.println("Start User1 SMS window.");
		} else {
			encryptedTxt.setText(User2.encryptedString2);
			dsTxt.setText(User2.dSignature2.toString());
			dSignature = User2.dSignature2;
		}

		gp.setText(gp.getText() + Launch.dh.getP().toString());
		gg.setText(gg.getText() + Launch.dh.getG().toString());
		privateK1.setText(privateK1.getText() + String.valueOf(123));
		privateK2.setText(privateK2.getText() + String.valueOf(560));
		publicK1.setText(publicK1.getText() + Launch.user1PubK.toString());
		publicK2.setText(publicK2.getText() + Launch.user2PubK.toString());
		secretK1.setText(secretK1.getText() + Launch.user1SecK.toString());
		secretK2.setText(secretK2.getText() + Launch.user2SecK.toString());
		castK.setText(castK.getText() + Launch.c5.encryptionKey);
		eccA.setText(eccA.getText() + "5");
		eccB.setText(eccB.getText() + "10");
		eccP.setText(eccP.getText() + "8009");
		eccBP.setText(eccBP.getText() + "(1,4)");
		egPrivateK1.setText(egPrivateK1.getText() + eceg1.private_chunk.key.x);
		egPublicK1
				.setText(egPublicK1.getText() + "(" + eceg1.public_chunk.key.x + "," + eceg1.public_chunk.key.y + ")");
		egPrivateK2.setText(egPrivateK2.getText() + eceg2.private_chunk.key.x);
		egPublicK2
				.setText(egPublicK2.getText() + "(" + eceg2.public_chunk.key.x + "," + eceg2.public_chunk.key.y + ")");
	}

	public void start(Stage primaryStage) throws Exception {
		if (Launch.firstMsg1 == false)
			System.out.println("Start User1 SMS window.");
		Parent root = FXMLLoader.load(getClass().getResource("/launchPackage/user1.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("User1 SMS Screen");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void encrypt(ActionEvent event) {
		if (plainTxt.getText() == "") {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error plain text");
			alert.setHeaderText("Invalid plain text!");
			alert.setContentText("Must enter plain text to send.");
			alert.showAndWait();
			return;
		}

		if (Launch.tr1fa2 == true) {
			encryptedMsg = Launch.c5.Encrypt(plainTxt.getText().getBytes());
			dSignature = eceg2.encrypt(encryptedMsg.toString());
			Launch.tr1fa2 = false;
		} else {
			encryptedMsg = User2.encryptedMsg2;
			dSignature = User2.dSignature2;
		}

		byte[] bytes = new byte[encryptedMsg.size()];
		for (int i = 0; i < encryptedMsg.size(); i++)
			bytes[i] = encryptedMsg.get(i);

		encryptedString = new String(bytes);
		encryptedTxt.setText(encryptedString);
		dsTxt.setText(dSignature.toString());

		System.out.println("Plain text encrypted. Digital Signature generated.");
		System.out.println("Plain text: " + plainTxt.getText());
		System.out.println("Encrypted text: " + encryptedString);
		System.out.println("Digital signature: " + dSignature);
	}

	@FXML
	void decrypt(ActionEvent event) {
		if (encryptedTxt.getText() == "") {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error encrypted text");
			alert.setHeaderText("Invalid encrypted text!");
			alert.setContentText("Must use encrypted text to decrypt.");
			alert.showAndWait();
			return;
		}

		decryptedMsg = Launch.c5.decrypt(User2.encryptedMsg2);
		plainTxt.setText(decryptedMsg);

		System.out.println("Encrypted text has been decrypted.");
		System.out.println("Encrypted text: " + encryptedTxt.getText());
		System.out.println("Decrypted text: " + decryptedMsg);

		ArrayList<Byte> c5DecTemp = new ArrayList<>();

		String enPtTmp = eceg1.decrypt(dSignature);
		String str = enPtTmp.substring(1, enPtTmp.length() - 1);
		String strtemp[] = str.split(", ");

		for (int l = 0; l < strtemp.length; l++)
			c5DecTemp.add(l, Byte.valueOf(strtemp[l]));

		String decPT = Launch.c5.decrypt(c5DecTemp);
		// decPT="5"; SIMULATE DS FAIL
		if (decPT.equals(decryptedMsg)) {
			System.out.println("Digital signature has been decrypted.");
			System.out.println("Digital signature: " + dsTxt.getText());
			System.out.println("Plain text: " + decPT);
			verify.setText(verify.getText() + "Verified successfully!");
			verify.setStyle("-fx-control-inner-background: #4ec029");
		} else {
			verify.setText(verify.getText() + "Verification FAILED!");
			verify.setStyle("-fx-text-fill: crimson; -fx-control-inner-background: #000");
		}
	}

	@FXML
	void send(ActionEvent event) throws Exception {
		System.out.println("\nStart User2 SMS window.");
		Stage primaryStage = new Stage();
		Parent root1 = FXMLLoader.load(getClass().getResource("/launchPackage/user2.fxml"));
		Scene scene = new Scene(root1);
		primaryStage.setTitle("User2 SMS Screen");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void clearTxt(ActionEvent event) throws Exception {
		plainTxt.clear();
		encryptedTxt.clear();
		dsTxt.clear();
	}
}