package name.emu.monitoring;

public enum Status {
    UNKNOWN("status-unknown.png", "Uptime Robot not queried yet"),

    UP("status-up.png", "System are up"),

    DOWN("status-down.png", "At least one system is down");

    Status(String imageFilename, String tooltipMessage) {
        this.imageFilename = imageFilename;
        this.tooltipMessage = tooltipMessage;
    }

    private String imageFilename;

    private String tooltipMessage;

    public String getImageFilename() {
        return imageFilename;
    }

    public String getTooltipMessage() {
        return tooltipMessage;
    }
}
