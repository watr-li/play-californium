package global;

import play.*;
import play.libs.Akka;

import akka.actor.*;

import actors.CaliforniumServerActor;
import actors.messages.*;


public class Global extends GlobalSettings {

    private static ActorRef californiumActor;

    public void onStart(Application app) {
        californiumActor = Akka.system().actorOf(Props.create(CaliforniumServerActor.class));
    }

    public void onStop(Application app) {
        californiumActor.tell(new ShutdownActor(), null);
    }

    public static ActorRef getCaliforniumActor() {
        return californiumActor;
    }
}
