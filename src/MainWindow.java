import helper.Config;
import helper.MinimizeToTrayJFrame;

import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.border.Border;

import net.miginfocom.swing.MigLayout;

import java.awt.Font;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.ImageIcon;

public class MainWindow {

	private MinimizeToTrayJFrame frame;
	private JSpinner spinnerInterval;
	private JLabel lblStatus;
	private Timer timer;
	private JTextArea textAreaStatus;
	private JTextArea textAreaNews;
	static public String dataFileName = "data.dat";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new MinimizeToTrayJFrame();
		frame.setTitle("HoMe Downloader");
		frame.setBounds(100, 100, 450, 434);
		frame.setMinimumSize(new Dimension(350, 200));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Border paddingBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

		// JButton btnStart = new JButton("Start");
		JButton btnStart = new JButton(
				new ImageIcon(
						((new ImageIcon(MainWindow.class
								.getResource("/images/continue-icon.png")))
								.getImage()).getScaledInstance(20, 20,
								java.awt.Image.SCALE_SMOOTH)));
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startDownload();
			}
		});
		frame.getContentPane().setLayout(
				new MigLayout("", "[76px][44px][44px][75px][200\r\npx]",
						"[23px][][217px][][217px][]"));
		frame.getContentPane().add(btnStart,
				"cell 0 0,alignx center,aligny center");

		JButton btnStop = new JButton(
				new ImageIcon(
						((new ImageIcon(MainWindow.class
								.getResource("/images/pause-icon.png")))
								.getImage()).getScaledInstance(20, 20,
								java.awt.Image.SCALE_SMOOTH)));
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stopDownload();
			}
		});
		frame.getContentPane().add(btnStop,
				"cell 1 0,alignx center,aligny center");

		JLabel lblIntervalInMin = new JLabel("Interval in Min: ");
		lblIntervalInMin.setBorder(paddingBorder);
		frame.getContentPane().add(lblIntervalInMin,
				"cell 2 0,alignx center,aligny center");

		spinnerInterval = new JSpinner();
		spinnerInterval.setModel(new SpinnerNumberModel(15, 5, 60, 5));
		frame.getContentPane().add(spinnerInterval,
				"cell 3 0,alignx center,aligny center");

		lblStatus = new JLabel(
				new ImageIcon(
						((new ImageIcon(MainWindow.class
								.getResource("/images/stopped-icon.png")))
								.getImage()).getScaledInstance(30, 30,
								java.awt.Image.SCALE_SMOOTH)));
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblStatus.setOpaque(true);
		lblStatus.setBorder(paddingBorder);
		frame.getContentPane().add(lblStatus,
				"cell 4 0,alignx center,aligny center");

		JLabel lblStatus_1 = new JLabel("Status:");
		frame.getContentPane().add(lblStatus_1, "cell 0 1,alignx center");

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, "cell 0 2 5 1,grow");

		textAreaStatus = new JTextArea();
		scrollPane.setViewportView(textAreaStatus);

		JLabel lblNews = new JLabel("News:");
		frame.getContentPane().add(lblNews, "cell 0 3,alignx center");

		JScrollPane scrollPane_1 = new JScrollPane();
		frame.getContentPane().add(scrollPane_1, "cell 0 4 5 1,grow");

		textAreaNews = new JTextArea();
		scrollPane_1.setViewportView(textAreaNews);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);

		JMenuItem mntmBeenden = new JMenuItem("Beenden");
		mntmBeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		JMenuItem mntmLogLschen = new JMenuItem("Log L\u00F6schen");
		mntmLogLschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textAreaStatus.setText("");
				textAreaNews.setText("");
				saveToFile();
			}
		});
		mnDatei.add(mntmLogLschen);
		mnDatei.add(mntmBeenden);

		JMenu mnBearbeiten = new JMenu("Bearbeiten");
		menuBar.add(mnBearbeiten);

		JMenu mnHilfe = new JMenu("Hilfe");
		menuBar.add(mnHilfe);

		JMenuItem menuItem = new JMenuItem("?");
		mnHilfe.add(menuItem);

		timer = new Timer();
		loadDataFromFile();
	}

	public JTextArea getTextAreaStatus() {
		return textAreaStatus;
	}

	class myWorker extends SwingWorker<String, Void> {
		JTextArea txtStatus;
		JTextArea txtNews;

		public myWorker(JTextArea txtStatus, JTextArea txtNews) {
			this.txtStatus = txtStatus;
			this.txtNews = txtNews;

		}

		@Override
		protected String doInBackground() throws Exception {
			NewsDownloader loader = new NewsDownloader(txtStatus, txtNews);
			loader.los();
			return null;
		}

	}

	public JSpinner getSpinnerInterval() {
		return spinnerInterval;
	}

	public JLabel getLblStatus() {
		return lblStatus;
	}

	public void startDownload() {
		int interval = (int) getSpinnerInterval().getValue();
		lblStatus.setIcon(new ImageIcon(((new ImageIcon(MainWindow.class
				.getResource("/images/started-icon.png"))).getImage())
				.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				myWorker worker = new myWorker(getTextAreaStatus(),
						getTextAreaNews());
				worker.execute();

			}
		};
		timer = new Timer();
		timer.scheduleAtFixedRate(task, 0, TimeUnit.MINUTES.toMillis(interval));
	}

	public void stopDownload() {
		lblStatus.setIcon(new ImageIcon(((new ImageIcon(MainWindow.class
				.getResource("/images/stopped-icon.png"))).getImage())
				.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));

		timer.cancel();
	}

	public JTextArea getTextAreaNews() {
		return textAreaNews;
	}

	public void loadDataFromFile() {
		ObjectInputStream inputStream = null;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(
					dataFileName));
			Object obj = null;
			while ((obj = inputStream.readObject()) != null) {
				if (obj instanceof Config) {
					Config config = (Config) obj;
					textAreaStatus.setText(config.getTxtStatus());
					textAreaNews.setText(config.getTxtNews());
				}
			}

		} catch (EOFException ex) { // This exception will be caught when EOF is
									// reached
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// Close the ObjectInputStream
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void saveToFile() {

		ObjectOutputStream outputStream = null;
		try {

			// Construct the LineNumberReader object
			outputStream = new ObjectOutputStream(new FileOutputStream(
					MainWindow.dataFileName));

			Config config = new Config("", "");

			outputStream.writeObject(config);

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// Close the ObjectOutputStream
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
}
