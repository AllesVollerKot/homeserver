package helper;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.awt.SystemTray.getSystemTray;

public class MinimizeToTrayJFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3336903644621863106L;
	public JFrame frame;
	public JPanel panel;
	public JTextArea ausgabe;

	public MinimizeToTrayJFrame() {
		super("Minimizer");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Image image = Toolkit.getDefaultToolkit().getImage("C:\\icon.png");

		setIconImage(image);
		if (SystemTray.isSupported()) {
			final TrayIcon icon = new TrayIcon(image);
			icon.setToolTip("Task Logger");
			icon.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					MinimizeToTrayJFrame.this.setVisible(true);
					MinimizeToTrayJFrame.this.setExtendedState(MinimizeToTrayJFrame.NORMAL);
					getSystemTray().remove(icon);
				}

			});
			addWindowListener(new WindowAdapter() {

				@Override
				public void windowIconified(WindowEvent e) {
					MinimizeToTrayJFrame.this.setVisible(false);
					try {
						getSystemTray().add(icon);
					} catch (AWTException e1) {
						e1.printStackTrace();
					}
				}

			});
		}
	}
}