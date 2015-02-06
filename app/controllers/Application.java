package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import akka.actor.*;
import play.libs.F.*;
import play.mvc.WebSocket;

import actors.MyWebSocketActor;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

     public static WebSocket<String> websocketHandler() {
        return WebSocket.withActor(new Function<ActorRef, Props>() {
            public Props apply(ActorRef out) throws Throwable {
                return MyWebSocketActor.props(out);
            }
        });
    }

}
