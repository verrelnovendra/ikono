package dokter;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Schedule {
    private StringProperty dayOfWeek;  
    private StringProperty startTime; 
    private StringProperty endTime;    
    private StringProperty description; 
    private StringProperty location;   

 
    public Schedule(String dayOfWeek, String startTime, String endTime, String description, String location) {
        this.dayOfWeek = new SimpleStringProperty(dayOfWeek);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.description = new SimpleStringProperty(description);
        this.location = new SimpleStringProperty(location);
    }

    public String getDayOfWeek() {
        return dayOfWeek.get();
    }
    public String getStartTime() {
        return startTime.get();
    }
    public String getEndTime() {
        return endTime.get();
    }
    public String getDescription() {
        return description.get();
    }
    public String getLocation() {
        return location.get();
    }
    public StringProperty dayOfWeekProperty() {
        return dayOfWeek;
    }
    public StringProperty startTimeProperty() {
        return startTime;
    }
    public StringProperty endTimeProperty() {
        return endTime;
    }
    public StringProperty descriptionProperty() {
        return description;
    }
    public StringProperty locationProperty() {
        return location;
    }
}
