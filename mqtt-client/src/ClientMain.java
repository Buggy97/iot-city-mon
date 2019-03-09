import javafx.util.Pair;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.Serializable;

/**
 * @todo: Get config from Git repo
 *
 */
public class ClientMain
{
    private static String _MQTT_BROKER = "tcp://localhost:1883";

    public static void main(String[] args)
    {
        MqttConnectOptions opts = new MqttConnectOptions();
        opts.setCleanSession(true);

        try
        {
            MqttClient client = MqttConnectionSingleton.getClient(_MQTT_BROKER, System.getenv("MQTT_CLIENT_ID"), opts);
            MqttQueue queue = new MqttQueue();

            Thread worker = new Thread(new MqttWorker(client, queue));
            worker.start();

            while (true) {
                try {
                    queue.enqueue(new Pair<String, Serializable>("iot-citymon-test", "ACK"));
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (MqttException e) {
            // @todo: Log failure
            e.printStackTrace();
        }
    }
}