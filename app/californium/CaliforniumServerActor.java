package californium;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import californium.messages.ShutdownActor;

public class CaliforniumServerActor extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    CaliforniumServer server;

    public CaliforniumServerActor() {
        super();
        server = CaliforniumServer.initialize(getSelf());
    }

    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            log.info("Received String message: {}", message);
            //getSender().tell(message, getSelf());
        } else if(message instanceof ShutdownActor) {
            log.info("Stopping CaliforniumServer", message);
            server.stop();
            getSelf().tell(akka.actor.PoisonPill.getInstance(), getSelf());
        } else {
            log.info("Unhandled message: {}", message);
            unhandled(message);
        }
    }










}
