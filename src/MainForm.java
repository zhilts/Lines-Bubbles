import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MainForm extends JFrame {
	public JTextField nC = new JTextField("10");
	public Scribble pane = new Scribble(this);

	public MainForm(String s) {

		super(s);
		setBounds(0, 0, 500, 700);

		JPanel p = new JPanel();
		add(p, BorderLayout.SOUTH);

		pane = new Scribble(this);

		add(pane);
		nC.setColumns(5);
		p.add(nC);

		JButton newGame = new JButton("Новая");
		newGame.setActionCommand("new");
		p.add(newGame);
		newGame.addActionListener(pane);

		JButton zoomDown = new JButton("-");
		zoomDown.setActionCommand("zD");
		p.add(zoomDown);
		zoomDown.addActionListener(pane);

		JButton zoomUp = new JButton("+");
		zoomUp.setActionCommand("zU");
		p.add(zoomUp);
		zoomUp.addActionListener(pane);

		JButton zoomNorm = new JButton("Norm");
		zoomNorm.setActionCommand("pB");
		p.add(zoomNorm);
		zoomNorm.addActionListener(pane);

		JButton replay = new JButton("Заново");
		replay.setActionCommand("rep");
		p.add(replay);
		replay.addActionListener(pane);

		JButton bsolv = new JButton("Решить");
		bsolv.setActionCommand("solv");
		p.add(bsolv);
		bsolv.addActionListener(pane);

		JButton ret = new JButton("Вернуть");
		ret.setActionCommand("ret");
		p.add(ret);
		ret.addActionListener(pane);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		new MainForm("Кружки и линии");
	}

}

@SuppressWarnings("serial")
class Scribble extends JPanel implements ActionListener, MouseListener,
		MouseMotionListener {

	private int lastX, lastY;
	private List<Circle> circles = new ArrayList<Circle>();
	private Set<Line> lines = new HashSet<Line>();
	private Set<Line> crossLines = new HashSet<Line>();
	private Circle cur = null;
	private int numCir;
	private static int RADIUS = 10;
	private boolean win = false;
	private long step = 0;
	private MainForm f;
	private double zoom = 0;

	public Scribble(MainForm frame) {
		f = frame;
		enableEvents(AWTEvent.MOUSE_EVENT_MASK
				| AWTEvent.MOUSE_MOTION_EVENT_MASK);
		addMouseListener(this);
		setOpaque(true);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(900, 500));
		setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
	}

	public void actionPerformed(ActionEvent event) {
		String s = event.getActionCommand();

		if (s.equals("new")) {
			try {
				newGame();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(f, e.getMessage());
			}
			optimize();
			repaint();
			return;
		}
		if (s.equals("zU")) {
			changeZoom(1.3);
			return;
		}
		if (s.equals("zD")) {
			changeZoom(1 / 1.3);
			return;
		}
		if (s.equals("pB")) {
			optimize();
			repaint();
			return;
		}
		if (s.equals("rep")) {
			replay();
			repaint();
			return;
		}
		if (s.equals("solv")) {

			for (Circle c : circles) {
				c.solv();
			}
			optimize();
			solv();
			repaint();
			return;
		}
		if (s.equals("ret")) {
			for (Circle c : circles) {
				c.load();
			}
			optimize();
			solv();
			repaint();
			return;
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int cx = f.getWidth() / 2, cy = (f.getHeight() - 60) / 2;
		for (Line l : lines) {
			if (crossLines.contains(l))
				g.setColor(Color.RED);
			else
				g.setColor(Color.GREEN);
			g.drawLine(cx + (int) (l.getFirst().getPos().x * zoom), cy
					- (int) (l.getFirst().getPos().y * zoom), cx
					+ (int) (l.getSecond().getPos().x * zoom), cy
					- (int) (l.getSecond().getPos().y * zoom));
			g.drawLine(cx + (int) (l.getFirst().getPos().x * zoom) + 1, cy
					- (int) (l.getFirst().getPos().y * zoom), cx
					+ (int) (l.getSecond().getPos().x * zoom) + 1, cy
					- (int) (l.getSecond().getPos().y * zoom));
			g.drawLine(cx + (int) (l.getFirst().getPos().x * zoom) - 1, cy
					- (int) (l.getFirst().getPos().y * zoom), cx
					+ (int) (l.getSecond().getPos().x * zoom) - 1, cy
					- (int) (l.getSecond().getPos().y * zoom));
			g.drawLine(cx + (int) (l.getFirst().getPos().x * zoom), cy
					- (int) (l.getFirst().getPos().y * zoom) + 1, cx
					+ (int) (l.getSecond().getPos().x * zoom), cy
					- (int) (l.getSecond().getPos().y * zoom) + 1);
			g.drawLine(cx + (int) (l.getFirst().getPos().x * zoom), cy
					- (int) (l.getFirst().getPos().y * zoom - 1), cx
					+ (int) (l.getSecond().getPos().x * zoom), cy
					- (int) (l.getSecond().getPos().y * zoom) - 1);
		}

		for (Circle c : circles) {
			g.setColor(Color.RED);
			int x = cx + (int) ((c.getPos().x) * zoom - RADIUS);
			int y = cy - (int) ((c.getPos().y) * zoom + RADIUS);
			g.fillOval(x, y, 2 * RADIUS, 2 * RADIUS);
			g.setColor(Color.WHITE);
			g.fillOval(x + 1, y + 1, 2 * RADIUS - 2, 2 * RADIUS - 2);
			char[] ms = Integer.toString(c.num + 1).toCharArray();
			g.setColor(Color.RED);
			g.drawChars(ms, 0, ms.length, x + RADIUS - 3 * ms.length, y
					+ RADIUS + 5);
		}

		if (win) {
			g.setColor(Color.BLUE);
			g.setFont(new Font("Arial", Font.ITALIC, 30));
			g.drawChars("You win!".toCharArray(), 0, 8, cx - 60, cy - 10);
		}
	}

	private void solv() {
		Set<Line> nLines = new HashSet<Line>();
		for (Line l : lines)
			for (Line l2 : lines)
				if (l != l2 && (!nLines.contains(l) || !nLines.contains(l2)))
					if (l.cross(l2)) {
						nLines.add(l);
						nLines.add(l2);
					}
		crossLines = nLines;
		if (crossLines.size() == 0)
			win = true;
		else
			win = false;
	}

	public void mousePressed(MouseEvent e) {
		int cx = f.getWidth() / 2, cy = (f.getHeight() - 60) / 2;
		for (Circle c : circles)
			if (Math.pow(cx + (int) (c.getPos().x * zoom) - e.getX(), 2)
					+ Math.pow(cy - e.getY() - (int) (c.getPos().y * zoom), 2) <= RADIUS
					* RADIUS
					&& (cur == null || cur.compareTo(c) < 0)) {
				cur = c;
			}
		if (cur != null) {
			cur.last = step;
			step++;
			Collections.sort(circles);
			repaint();
		}
		lastX = e.getX();
		lastY = e.getY();
	}

	public void mouseDragged(MouseEvent e) {
		int cx = f.getWidth() / 2, cy = (f.getHeight() - 60) / 2;
		if (cur != null) {
			cur.move((e.getX() - cx) / zoom, (cy - e.getY()) / zoom);
		} else
			for (Circle c : circles)
				c.move(c.getPos().x + (e.getX() - lastX) / zoom, c.getPos().y
						+ (-e.getY() + lastY) / zoom);

		solv();
		repaint();
		lastX = e.getX();
		lastY = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
		cur = null;
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void optimize() {
		Circle fc = circles.get(0);
		double minx = fc.getPos().x, miny = fc.getPos().y, maxx = fc.getPos().x, maxy = fc
				.getPos().y;
		for (Circle c : circles) {
			if (c.getPos().x > maxx)
				maxx = c.getPos().x;
			if (c.getPos().x < minx)
				minx = c.getPos().x;
			if (c.getPos().y > maxy)
				maxy = c.getPos().y;
			if (c.getPos().y < miny)
				miny = c.getPos().y;
		}
		double dx = (maxx + minx) / 2, dy = (maxy + miny) / 2;
		for (Circle c : circles)
			c.move(c.getPos().x - dx, c.getPos().y - dy);
		int mx = f.getWidth(), my = f.getHeight() - 60;
		zoom = Math.min(mx / (maxx - minx), my / (maxy - miny)) * 0.8;
	}

	private void replay() {
		int r = Math.min(f.getHeight(), f.getWidth()) / 2 - 70;
		for (Circle c : circles)
			c.move((double) (r * Math.cos(2 * Math.PI * c.num / numCir)),
					(double) (r * Math.sin(2 * Math.PI * c.num / numCir)));
		optimize();
		win = false;
		solv();
	}

	private void newGame() throws Exception {
		numCir = Integer.parseInt(f.nC.getText());
		step = numCir;
		if (numCir < 4)
			throw new Exception("Нельзя задавать меньше четырёх вершин!");
		do {
			win = false;
			circles = new ArrayList<Circle>();
			lines = new HashSet<Line>();
			for (int i = 0; i < numCir; i++)
				try {
					circles.add(new Circle(new mPoint(Math.random() * 600, Math
							.random() * 600), i));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			Set<Circle> newTree = new HashSet<Circle>();
			newTree.add(circles.get(0));
			while (newTree.size() < circles.size()) {
				Line bestLine = null;
				for (Circle fc : newTree)
					for (Circle sc : circles)
						if (!newTree.contains(sc)) {
							Line newLine = new Line(fc, sc);
							if ((bestLine == null || (newLine.length() < bestLine
									.length())))
								bestLine = newLine;
						}

				lines.add(bestLine);
				bestLine.A.neighbours++;
				bestLine.B.neighbours = 1;
				newTree.add(bestLine.B);
			}

			boolean fl = true;
			while (fl) {
				fl = false;
				Iterator<Circle> i = circles.iterator();
				while (i.hasNext()) {
					Circle c = i.next();
					if (c.neighbours < 2) {
						fl = true;
						for (Circle nc : circles)
							if (!c.equals(nc)) {
								Line newLine = new Line(c, nc);
								if (!lines.contains(newLine)) {
									boolean per = false;
									for (Line l : lines)
										if (newLine.cross(l))
											per = true;
									if (!per) {
										lines.add(newLine);
										newLine.A.neighbours++;
										newLine.B.neighbours++;
									}
								}
							}
					}
				}
			}

			for (Circle c : circles)
				c.save();
			replay();
			solv();
		} while (win);
	}

	private void changeZoom(double d) {
		double fZoom = zoom, z = (zoom * d - zoom);
		int time = 300;
		double t = 0, step = 0.1;
		while (t < 1) {
			try {
				
				//double t1=(1-t);
				zoom = fZoom + z * (3*(1-t)+t)*t*t;
				paint(this.getGraphics());
				t += step;
				Thread.sleep((int) (time * step));

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//System.out.println(zoom/fZoom);
	}
}
