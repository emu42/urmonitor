package name.emu.monitoring.uptimerobot;

import java.util.List;

public class MonitorsResponse {
    private String stat;

    private Pagination pagintation;

    private List<Monitor> monitors;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public Pagination getPagintation() {
        return pagintation;
    }

    public void setPagintation(Pagination pagintation) {
        this.pagintation = pagintation;
    }

    public List<Monitor> getMonitors() {
        return monitors;
    }

    public void setMonitors(List<Monitor> monitors) {
        this.monitors = monitors;
    }
}
