package name.emu.monitoring;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuitApplicationAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
