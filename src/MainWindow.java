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
import javax.swing.JTabbedPane;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

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
		frame.setBounds(100, 100, 424, 358);
		frame.setMinimumSize(new Dimension(400, 200));
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
		FormLayout formLayout = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1px"),
				ColumnSpec.decode("53px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("53px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("95px"),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("39px"),
				ColumnSpec.decode("61px"),
				ColumnSpec.decode("pref:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("50px"),
				FormFactory.NARROW_LINE_GAP_ROWSPEC,
				RowSpec.decode("fill:default:grow"),});
		frame.getContentPane().setLayout(formLayout);
		frame.getContentPane().add(btnStart, "2, 2, center, center");

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
		frame.getContentPane().add(btnStop, "4, 2, center, center");

		JLabel lblIntervalInMin = new JLabel("Interval in Min: ");
		lblIntervalInMin.setBorder(paddingBorder);
		frame.getContentPane().add(lblIntervalInMin, "6, 2, center, center");

		spinnerInterval = new JSpinner();
		spinnerInterval.setModel(new SpinnerNumberModel(15, 5, 60, 5));
		frame.getContentPane().add(spinnerInterval, "8, 2, center, center");

		lblStatus = new JLabel(
				new ImageIcon(
						((new ImageIcon(MainWindow.class
								.getResource("/images/stopped-icon.png")))
								.getImage()).getScaledInstance(30, 30,
								java.awt.Image.SCALE_SMOOTH)));
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblStatus.setOpaque(true);
		lblStatus.setBorder(paddingBorder);
		frame.getContentPane().add(lblStatus, "10, 2, left, center");
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		frame.getContentPane().add(tabbedPane, "2, 4, 9, 1, default, fill");

		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("Status", null, scrollPane, null);

		textAreaStatus = new JTextArea();
		scrollPane.setViewportView(textAreaStatus);

		JScrollPane scrollPane_1 = new JScrollPane();
		tabbedPane.addTab("News", null, scrollPane_1, null);

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
		
		JMenuItem mntmTest = new JMenuItem("test");
		mntmTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
			}
		});
		mnBearbeiten.add(mntmTest);

		JMenu mnHilfe = new JMenu("Hilfe");
		menuBar.add(mnHilfe);

		JMenuItem menuItem = new JMenuItem("?");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			About.main(null);
			
			}
		});
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
