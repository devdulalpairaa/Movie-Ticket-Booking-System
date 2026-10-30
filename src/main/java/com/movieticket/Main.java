ckage com.movieticket;

import com.movieticket.gui.MainFrame;
import com.movieticket.gui.UIConstants;
import com.movieticket.util.FileManager;
import com.movieticket.util.Logger;

import javax.swing.*;

/**
 * Application entry point.
 * Initialises the file-based data store, then launches the Swing GUI
 * on the Event Dispatch Thread as per Swing best practice.
 */
public class Main {

    public static void main(String[] args) {
        Logger.info("Starting Movie Ticket Booking System...");
        FileManager.initDataFiles();

        SwingUtilities.invokeLater(() -> {
            applyLookAndFeel();
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            Logger.info("Application window ready.");
        });
    }

    /**
     * Nimbus renders combo boxes, tables, tabs and scrollbars with the app's own
     * colours instead of each OS's flat native chrome, so it's used as the base
     * look-and-feel; the system L&F is kept as a fallback if Nimbus isn't available.
     */
    private static void applyLookAndFeel() {
        try {
            UIManager.put("nimbusBase", UIConstants.PRIMARY);
            UIManager.put("nimbusBlueGrey", UIConstants.BACKGROUND);
            UIManager.put("control", UIConstants.BACKGROUND);
            UIManager.put("nimbusFocus", UIConstants.ACCENT);
            UIManager.put("nimbusSelectionBackground", UIConstants.ACCENT);
            UIManager.put("text", UIConstants.TEXT_DARK);
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            Logger.warn("Could not set a look and feel, using default.");
        }
    }
}
