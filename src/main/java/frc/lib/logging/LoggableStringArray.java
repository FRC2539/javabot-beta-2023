package frc.lib.logging;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringArrayPublisher;
import edu.wpi.first.networktables.StringArraySubscriber;
import edu.wpi.first.networktables.StringArrayTopic;
import edu.wpi.first.util.datalog.StringArrayLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;

public class LoggableStringArray {
    StringArrayTopic topic;
    StringArrayPublisher publisher;
    StringArraySubscriber subscriber;
    StringArrayLogEntry logger;
    String[] defaultValue;
    boolean override = Constants.competitionMode;

    /**
     * @param path The full name of the double, e.g. "/MySubsystem/MyThing"
     * @param defaultValue
     */
    public LoggableStringArray(String path, String[] defaultValue) {
        this.defaultValue = defaultValue;

        topic = NetworkTableInstance.getDefault().getStringArrayTopic(path);
        logger = new StringArrayLogEntry(DataLogManager.getLog(), path);
    }

    public LoggableStringArray(String path, String[] defaultValue, boolean override) {
        this.defaultValue = defaultValue;

        topic = NetworkTableInstance.getDefault().getStringArrayTopic(path);
        logger = new StringArrayLogEntry(DataLogManager.getLog(), path);

        this.override = override;
    }

    public void set(String[] value) {
        // Lazily create a publisher
        if (publisher == null) publisher = topic.publish();

        if (!override) {
            publisher.set(value);
        }

        logger.append(value);
    }

    public String[] get() {
        // Lazily create a subscriber
        if (subscriber == null) subscriber = topic.subscribe(defaultValue);

        var value = subscriber.get();
        logger.append(value);

        return value;
    }
}
