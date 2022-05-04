package name.emu.monitoring;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Main class of the application. This creates the system tray icon and menu.
 */
public class Application {

    private Configuration configuration;

    private Status status = Status.UNKNOWN;  // initial status before uptime robot has been queried

    private Map<Status, Image> imageCache = new HashMap<>();

    private Timer updateStatusTimer;

    private TrayIcon trayIcon;

    /**
     * Static main method.
     * @param args
     */
    public static void main(String[] args) {
        new Application().start();
    }

    public void start() {
        try {
            if (!SystemTray.isSupported()) {
                // system tray is required, so just show this error and exit if it is not available
                throw new StartupException("SystemTray is not supported on this system");
            }

            final PopupMenu popup = new PopupMenu();
            trayIcon = new TrayIcon(getStatusIcon(status));
            final SystemTray tray = SystemTray.getSystemTray();

            // Create a pop-up menu components
            MenuItem quitItem = new MenuItem("Quit");

            popup.add(quitItem);

            quitItem.addActionListener(new QuitApplicationAction());

            trayIcon.setPopupMenu(popup);
            trayIcon.setImageAutoSize(true);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                throw new StartupException("TrayIcon could not be added.");
            }

            loadConfiguration();

            if (configuration.getApiKey() == null || configuration.getApiKey().isEmpty()) {
                // request API Key from the user

                String apiKey = JOptionPane.showInputDialog("Your Uptime Robot API key is not set yet. Please enter it here!");

                if (apiKey == null) {
                    throw new StartupException("Cannot run without API key.");
                }

                configuration.setApiKey(apiKey);
            }

            // preload other icons
            getStatusIcon(Status.UP);
            getStatusIcon(Status.DOWN);

            updateStatusTimer = new Timer();

            updateStatusTimer.schedule(new UpdateStatusTimerTask(this, configuration), 0, configuration.getTimerInterval());

        } catch (StartupException e) {
            showErrorAndExit(e.getMessage());
        }
    }

    private void showErrorAndExit(String errorMsg) {
        System.err.println(errorMsg);  // output on std err first in case there is no GUI at all
        JOptionPane.showMessageDialog(null, errorMsg, "Application failed to start", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    private void loadConfiguration() {
        configuration = new Configuration();
        configuration.init();
    }

    private synchronized Image getStatusIcon(Status status) {
        if (!imageCache.containsKey(status)) {
            try {
                InputStream in = getClass().getClassLoader().getResourceAsStream(status.getImageFilename());
                if (in == null) {
                    throw new StartupException("Image resource missing: "+status.getImageFilename());
                }
                Image statusImage = ImageIO.read(in);
                imageCache.put(status, statusImage);
            } catch (IOException e) {
                throw new StartupException("Unable to read image resource");
            }
        }
        return imageCache.get(status);
    }

    public void updateStatus(Status status) {
        if (status!=this.status) {
            this.status = status;
            trayIcon.setImage(getStatusIcon(status));
            trayIcon.setToolTip(status.getTooltipMessage());
        }
    }
}
