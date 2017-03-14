
/**
 * @author Justin Kuo
 * 
 * Simple Text editor
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GUIFx extends Application {
	
	Button saveButton = new Button("Save");
	Button saveAsButton = new Button("Save as new file");
	Button chooseInitFileButton = new Button("Choose Initial File");
	Button blankButton = new Button("Blank Page");
	
	BorderPane border = new BorderPane();
	
	TextArea writingArea = new TextArea();
	
	HBox buttonHBox = new HBox();
	
	Scene scene;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		String fileSaveName = getDateTime();
		ObservableList<CharSequence> paragraph = writingArea.getParagraphs();
		Iterator<CharSequence> iter = paragraph.iterator();
		
		File fileToSaveTo = new File(
				System.getProperty("user.home") + "/Desktop/Text testing/" + fileSaveName + ".txt");
				
		if (fileToSaveTo.exists()) {
			readFile(fileToSaveTo);
		}
		
		buttonHBox.setSpacing(5);
		buttonHBox.setAlignment(Pos.TOP_LEFT);
		buttonHBox.setStyle("-fx-border-color: lightgray;");
		buttonHBox.getChildren().addAll(saveButton, chooseInitFileButton, blankButton);
		
		border.setTop(buttonHBox);
		border.setCenter(writingArea);
		
		scene = new Scene(border, 600, 600);
		
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				save();
			}
			
		});
		
		saveAsButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				saveAs();
			}
			
		});
		
		chooseInitFileButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				File inFile;
				
				// Extension filter
				FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text doc(*.txt)",
						"*.txt");
				fileChooser.getExtensionFilters().add(extensionFilter);
				
				// Set to user directory or go to default if cannot access
				String userDirectoryString = System.getProperty("user.home");
				File userDirectory = new File(userDirectoryString);
				if (!userDirectory.canRead()) {
					userDirectory = new File("c:/");
				}
				fileChooser.setInitialDirectory(userDirectory);
				File chosenFile = fileChooser.showOpenDialog(null);
				readFile(chosenFile);
			}
			
		});
		
		blankButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				writingArea.clear();
			}
			
		});
		
		// 10 minute autosave
		// Timer timer = new Timer();
		// long tenMinutesToMS = 300000;
		// timer.scheduleAtFixedRate(new TimerTask() {
		// @Override
		// public void run() {
		// save();
		// }
		// }, tenMinutesToMS, tenMinutesToMS);
		
		// save on exit
		Runtime.getRuntime().addShutdownHook(new Thread() {
			
			@Override
			public void run() {
				save();
				primaryStage.close();
			}
			
		});
		
		primaryStage.setTitle("Simple Text Editor");
		// primaryStage.setMaximized(true);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public void readFile(File inFile) {
		
		String filePath;
		if (inFile != null) {
			filePath = inFile.getPath();
		}
		else {
			// default return value
			filePath = null;
		}
		
		try {
			BufferedReader lineReader = new BufferedReader(new FileReader(filePath));
			String str;
			while ((str = lineReader.readLine()) != null) {
				writingArea.appendText(str);
			}
			
			lineReader.close();
			
		}
		catch (IOException ex) {
			System.err.println(ex);
		}
		
	}
	
	public void appendToFile(File file, String appendString) {
		
		try (FileWriter fw = new FileWriter(file, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(appendString);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		String fileSaveName = getDateTime();
		ObservableList<CharSequence> paragraph = writingArea.getParagraphs();
		Iterator<CharSequence> iter = paragraph.iterator();
		
		File fileToSaveTo = new File(
				System.getProperty("user.home") + "/Desktop/Text testing/" + fileSaveName + ".txt");
				
		// if (fileToSaveTo.exists()) {
		// appendToFile(fileToSaveTo, writingArea.getText());
		// return;
		// }
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(fileToSaveTo));
			while (iter.hasNext()) {
				CharSequence seq = iter.next();
				bf.append(seq);
				bf.newLine();
			}
			bf.flush();
			bf.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveAs() {
		FileChooser fileChooser = new FileChooser();
		
		// Extension filter
		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text doc(*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extensionFilter);
		
		// Set to user directory or go to default if cannot access
		String userDirectoryString = System.getProperty("user.home");
		File userDirectory = new File(userDirectoryString);
		if (!userDirectory.canRead()) {
			userDirectory = new File("c:/");
		}
		fileChooser.setInitialDirectory(userDirectory);
		File chosenFile = fileChooser.showOpenDialog(null);
		ObservableList<CharSequence> paragraph = writingArea.getParagraphs();
		Iterator<CharSequence> iter = paragraph.iterator();
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(chosenFile));
			while (iter.hasNext()) {
				CharSequence seq = iter.next();
				bf.append(seq);
				bf.newLine();
			}
			bf.flush();
			bf.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
	
}
