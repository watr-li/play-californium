package actors;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import actors.messages.*;

public class MyWebSocketActor extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final ActorRef out;

    // TODO: wat
    public static Props props(ActorRef out) {
        return Props.create(MyWebSocketActor.class, out);
    }

    public MyWebSocketActor(ActorRef out) {
        this.out = out;
        global.Global.getCaliforniumActor().tell(new SendMeCoapMessages(), getSelf());
    }

    public void onReceive(Object message) throws Exception {
        if(message instanceof CoapMessageReceived) {
            CoapMessageReceived msg = (CoapMessageReceived) message;
            out.tell("Received via CoAP: " + msg.getMessage(), getSelf());
        } else {
            log.info("Unhandled WebSocket message: {}", message);
            unhandled(message);
        }
    }
}
