package launchPackage;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class User2 {
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

	static ArrayList<Byte> encryptedMsg2;
	static String decryptedMsg2, encryptedString2;
	static byte[] dSignature2;

	public void initialize() {
		plainTxt.setWrapText(true);
		encryptedTxt.setWrapText(true);
		dsTxt.setWrapText(true);

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
		egPrivateK1.setText(egPrivateK1.getText() + User1.eceg1.private_chunk.key.x);
		egPublicK1.setText(egPublicK1.getText() + "(" + User1.eceg1.public_chunk.key.x + ","
				+ User1.eceg1.public_chunk.key.y + ")");
		egPrivateK2.setText(egPrivateK2.getText() + User1.eceg2.private_chunk.key.x);
		egPublicK2.setText(egPublicK2.getText() + "(" + User1.eceg2.public_chunk.key.x + ","
				+ User1.eceg2.public_chunk.key.y + ")");

		encryptedTxt.setText(User1.encryptedString);
		dsTxt.setText(User1.dSignature.toString());
		dSignature2 = User1.dSignature;
	}

	@FXML
	void encrypt2(ActionEvent event) {
		if (plainTxt.getText() == "") {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error plain text");
			alert.setHeaderText("Invalid plain text!");
			alert.setContentText("Must enter plain text to send.");
			alert.showAndWait();
			return;
		}

		if (Launch.tr1fa2 == false) {
			encryptedMsg2 = Launch.c5.Encrypt(plainTxt.getText().getBytes());
			dSignature2 = User1.eceg1.encrypt(encryptedMsg2.toString());
			Launch.tr1fa2 = true;
		} else {
			encryptedMsg2 = User1.encryptedMsg;
			dSignature2 = User1.dSignature;
		}

		byte[] bytes = new byte[encryptedMsg2.size()];
		for (int i = 0; i < encryptedMsg2.size(); i++)
			bytes[i] = encryptedMsg2.get(i);

		encryptedString2 = new String(bytes);
		encryptedTxt.setText(encryptedString2);
		dsTxt.setText(dSignature2.toString());

		System.out.println("Plain text encrypted. Digital Signature generated.");
		System.out.println("Plain text: " + plainTxt.getText());
		System.out.println("Encrypted text: " + encryptedString2);
		System.out.println("Digital signature: " + dSignature2);
	}

	@FXML
	void decrypt2(ActionEvent event) {
		if (encryptedTxt.getText() == "") {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error encrypted text");
			alert.setHeaderText("Invalid encrypted text!");
			alert.setContentText("Must use encrypted text to decrypt.");
			alert.showAndWait();
			return;
		}

		decryptedMsg2 = Launch.c5.decrypt(User1.encryptedMsg);
		plainTxt.setText(decryptedMsg2);

		System.out.println("Encrypted text has been decrypted.");
		System.out.println("Encrypted text: " + encryptedTxt.getText());
		System.out.println("Decrypted text: " + decryptedMsg2);

		ArrayList<Byte> c5DecTemp2 = new ArrayList<>();

		String enPtTmp2 = User1.eceg2.decrypt(dSignature2);
		String str2 = enPtTmp2.substring(1, enPtTmp2.length() - 1);
		String strtemp2[] = str2.split(", ");

		for (int l = 0; l < strtemp2.length; l++)
			c5DecTemp2.add(l, Byte.valueOf(strtemp2[l]));

		String decPT2 = Launch.c5.decrypt(c5DecTemp2);
		// decPT2="5"; SIMULATE DS FAIL
		if (decPT2.equals(decryptedMsg2)) {
			System.out.println("Digital signature has been decrypted.");
			System.out.println("Digital signature: " + dsTxt.getText());
			System.out.println("Plain text: " + decPT2);
			verify.setText(verify.getText() + "Verified successfully!");
			verify.setStyle("-fx-control-inner-background: #4ec029");
		} else {
			verify.setText(verify.getText() + "Verification FAILED!");
			verify.setStyle("-fx-text-fill: crimson; -fx-control-inner-background: #000");
		}
	}

	@FXML
	void send2(ActionEvent event) throws Exception {
		System.out.println("\nStart User1 SMS window.");
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/launchPackage/user1.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("User1 SMS Screen");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void clearTxt2(ActionEvent event) throws Exception {
		plainTxt.clear();
		encryptedTxt.clear();
		dsTxt.clear();
	}
}