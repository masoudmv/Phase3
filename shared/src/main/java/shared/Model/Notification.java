package shared.Model;

public class Notification {
    private NotificationType notificationType;
    private String macAddress;
    private String username;
    private String message;


    public Notification(NotificationType notificationType, String message) {
        this.notificationType = notificationType;
        this.message = message;
    }

    public Notification(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Notification() {
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
