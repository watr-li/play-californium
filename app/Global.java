import play.*;
import play.libs.Akka;
import akka.actor.*;
import californium.CaliforniumServerActor;


public class Global extends GlobalSettings {

    private ActorRef californiumActor;

    public void onStart(Application app) {
        californiumActor = Akka.system().actorOf(Props.create(CaliforniumServerActor.class));
    }

    public void onStop(Application app) {
        californiumActor.tell(new californium.messages.ShutdownActor(), null);
    }

    public ActorRef getCaliforniumActor() {
        return californiumActor;
    }
}
