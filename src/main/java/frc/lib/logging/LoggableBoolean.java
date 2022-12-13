package frc.lib.logging;

import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.BooleanSubscriber;
import edu.wpi.first.networktables.BooleanTopic;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.datalog.BooleanLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;

public class LoggableBoolean {
    BooleanTopic topic;
    BooleanPublisher publisher;
    BooleanSubscriber subscriber;
    BooleanLogEntry logger;
    boolean defaultValue;
    boolean override = Constants.competitionMode;

    /**
     * @param path The full name of the double, e.g. "/MySubsystem/MyThing"
     * @param defaultValue
     */
    public LoggableBoolean(String path, boolean defaultValue) {
        this.defaultValue = defaultValue;

        topic = NetworkTableInstance.getDefault().getBooleanTopic(path);
        logger = new BooleanLogEntry(DataLogManager.getLog(), path);
    }

    public LoggableBoolean(String path, boolean defaultValue, boolean override) {
        this.defaultValue = defaultValue;

        topic = NetworkTableInstance.getDefault().getBooleanTopic(path);
        logger = new BooleanLogEntry(DataLogManager.getLog(), path);
        this.override = override;
    }

    public void set(boolean value) {
        // Lazily create a publisher
    if (publisher == null) publisher = topic.publish();

        if (!override) {
            publisher.set(value);
        }

        logger.append(value);
    }

    public boolean get() {
        // Lazily create a subscriber
        if (subscriber == null) subscriber = topic.subscribe(defaultValue);
        
        var value = subscriber.get();

        logger.append(value);

        return value;
    }
}
