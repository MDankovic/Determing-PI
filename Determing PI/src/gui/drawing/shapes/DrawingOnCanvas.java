package gui.drawing.shapes;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class DrawingOnCanvas extends Frame {
	
	private ActiveCanvas scene = new ActiveCanvas();
	private Button startButton = new Button("Draw");
	private Button pauseButton = new Button("Pause");
	private Dialog helpDialog = new Dialog(this, "Help", false);
	
	private void populateWindow() {
		
		helpDialog.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				helpDialog.setVisible(false);
			}
			
		});
		
		String helpText = "This is PI estimator using Monte Carlo method.\n"
				+ "In the bottom left corner you can see current PI estimation.\n"
				+ "Also you can choose Point and Arc colors and RESTART whole\n"
				+ "process by clicking on DRAW button. You can PAUSE and CONTINUE\n"
				+ "current iteration of program by clicking on PAUSE button."
				+ "\n\n\n\nMilan Dankovic 2018/0096";
		TextArea taHelp = new TextArea(helpText);
		taHelp.setEditable(false);
		helpDialog.setLocationRelativeTo(this);
		helpDialog.add(taHelp);
		helpDialog.pack();
		
		/*
		 * Menu - predstavlja padajuce menije koji mogu sadrzati druge menije. 
		 * Meniji se dodaju u traku menija. Menu klasa je izvedena iz klase MenuItem.
		 * MenuItem - predstavlja pojedinacne stavke menija.
		 */
		Menu speedMenu = new Menu("Speed");		
		MenuItem speedSlow = new MenuItem("Slow");
		MenuItem speedMedium = new MenuItem("Medium");
		MenuItem speedFast = new MenuItem("Fast");
		
		// Dodavanje pojedinacnih stavki menija u padajuci meni. 
		speedMenu.add(speedSlow);
		speedMenu.add(speedMedium);
		speedMenu.add(speedFast);
		
		// Moguce je osluskivati i obradjivati dogadjaje menija.
		speedSlow.addActionListener((ae) -> { 
			scene.setSpeed(5);
		});
		speedMedium.addActionListener((ae) -> { 
			scene.setSpeed(3);
		});
		speedFast.addActionListener((ae) -> { 
			scene.setSpeed(1);
		});
		
		// MenuShortcut - predstavlja precicu menija.
		MenuItem quitMenu = new MenuItem("Quit", new MenuShortcut(KeyEvent.VK_E));
		quitMenu.addActionListener((ae) -> {
			scene.stop();
			dispose();
		});
		
		MenuItem helpMenu = new MenuItem("Help", new MenuShortcut(KeyEvent.VK_H));
		helpMenu.addActionListener((ae) -> {
			helpDialog.setVisible(true);
		});
		
		Menu file = new Menu("File");
		
		file.add(speedMenu);
		file.addSeparator();
		file.add(helpMenu);
		file.addSeparator();
		file.add(quitMenu);
		
		// MenuBar - traka menija, koja se pridruzuje prozoru i kojoj se meniji mogu dodavati.
		MenuBar menuBar = new MenuBar();
		menuBar.add(file);
		setMenuBar(menuBar);
		
		add(scene, BorderLayout.CENTER);
		
		startButton.addActionListener((ae) -> {
			if(pauseButton.getLabel().equals("Continue"))
				scene.pause(pauseButton);
			scene.repaint();
		});
		
		pauseButton.addActionListener((ae) -> {
			scene.pause(pauseButton);
		});
		
		Panel buttonPanel = new Panel();
		
		// List - lista opcija iz koje je moguce odabrati jednu ili vise opcija.
		// Ne mesati sa java.util.List!
		List chooseColor = new List(2);
		chooseColor.add("Black");
		chooseColor.add("Red");
		chooseColor.add("Green");
		
		List choosePointColor = new List(2);
		choosePointColor.add("Yellow");
		choosePointColor.add("Red");
		choosePointColor.add("Green");
		
		// Izbor opcije programskim putem. Podrazumevano je obelezena prva opcija.
		chooseColor.select(0);
		choosePointColor.select(0);
		
		// Osluskivac dogadjaja liste. 
		// Dogadjaj se generise interakcijom korisnika sa komponentom (izborom opcije).
		chooseColor.addItemListener((ie) -> {
			String item = chooseColor.getSelectedItem();
			if("Black".equals(item))
				scene.setColor(Color.BLACK);
			else if("Red".equals(item))
				scene.setColor(Color.RED);
			else if("Green".equals(item))
				scene.setColor(Color.GREEN);
		});
		
		choosePointColor.addItemListener((ie) -> {
			String item = choosePointColor.getSelectedItem();
			if("Yellow".equals(item))
				scene.setPointColor(Color.YELLOW);
			else if("Red".equals(item))
				scene.setPointColor(Color.RED);
			else if("Green".equals(item))
				scene.setPointColor(Color.GREEN);
		});
		
		buttonPanel.add(scene.getLabel());
		buttonPanel.add(choosePointColor);
		buttonPanel.add(chooseColor);
		buttonPanel.add(startButton);
		buttonPanel.add(pauseButton);
		
		add(buttonPanel, BorderLayout.SOUTH);		
	}
	
	public DrawingOnCanvas() {
		
		scene.setSize(scene.getR(), scene.getR());
		setBounds(700, 200, scene.getR(), scene.getR());
	
		setResizable(false);
		
		setTitle("PI estimator");
		
		populateWindow();
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				scene.stop();
				dispose();
			}
		});
		setVisible(true);
	}
	
	public static void main(String[] args) {
		
		new DrawingOnCanvas();
	}
}
