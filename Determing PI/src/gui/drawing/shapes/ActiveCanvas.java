package gui.drawing.shapes;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.util.Random;

@SuppressWarnings("serial")
public class ActiveCanvas extends Canvas implements Runnable {
	
	private Color arcColor;
	private Color pointColor;
	private long sleepTime = 1;
	private Thread thread;
	private Label currentPI;
	private boolean active = true;
	private int r = 500;
	
	private int total = 0;
	private int inside = 0;
	
	public ActiveCanvas() {
		arcColor = Color.BLACK;
		pointColor = Color.YELLOW;
		setBackground(Color.GRAY);
		currentPI = new Label("PI ESTIMATION");
		currentPI.setFont(new Font("Arial", Font.BOLD, 15));
	}
	
	public Label getLabel() {
		return currentPI;
	}
	
	public int getR() {
		return r;
	}
	
	public synchronized void setSpeed(int speed) {
		sleepTime = speed;
	}
	
	@Override
	public void paint(Graphics g) {
		
		finish();
		
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		total = 0;
		inside = 0;
		
		Graphics g = getGraphics();
		
		g.setColor(arcColor);
		g.drawArc(-r - 1, -1, r * 2, r * 2, 0, 90);
		g.fillArc(-r - 1, -1, r * 2, r * 2, 0, 90);
		
		Random rnd = new Random();
		int x = rnd.nextInt(r + 1);
		int y = rnd.nextInt(r + 1);
		
		g.setColor(pointColor);
		
		try {
			for(int i = 0; i < 100000; i++) {
				Thread.sleep(sleepTime);
				if(Thread.interrupted())
					break;
				
				if(!active)
					continue;
				
				x = rnd.nextInt(r + 1);
				y = rnd.nextInt(r + 1);
				
				total++;
				if(isInside(x, y))
					inside++;
				if(i % 1000 == 0) {
					currentPI.setText(String.format("PI: %.6f", 4.0 * inside / total));
				}
				
				g.drawOval(x - 1, y - 1, 1, 1);
				g.fillOval(x - 1, y - 1, 1, 1);
			}
		} catch (InterruptedException e) {}
		
		synchronized (this) {
			thread = null;
			notify();
		}
	}
	
	public void stop() {
		thread.interrupt();
	}
	
	public synchronized void pause(Button pauseButton) {
		active = !active;
		if(active) {
			pauseButton.setLabel("Pause");
		} else {
			pauseButton.setLabel("Continue");
		}
	}
	
	private boolean isInside(int x, int y) {
		return Math.sqrt((x * x + (y - r) * (y - r))) <= r;
	}
	
	public synchronized void setColor(Color color) {
		arcColor = color;
	}
	
	public synchronized void setPointColor(Color color) {
		pointColor = color;
	}
	
	public synchronized void setBgColor(Color color) {
		setBackground(color);
	}
	
	public synchronized void finish() {
		if(thread != null)
			thread.interrupt();
		while(thread != null)
			try {
				wait();
			} catch (InterruptedException e) {}
	}
}
