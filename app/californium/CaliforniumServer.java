/*******************************************************************************
 * Copyright (c) 2014 Institute for Pervasive Computing, ETH Zurich and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 *
 * Contributors:
 *    Matthias Kovatsch - creator and main architect
 ******************************************************************************/
package californium;

import java.net.SocketException;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;


import play.Logger;



import akka.actor.*;
import static akka.pattern.Patterns.ask;


public class CaliforniumServer extends CoapServer {

    final static Logger.ALogger logger = Logger.of("CaliforniumServer");

    // This is the actor that will be notified if a request is received.
    ActorRef serverActor;

    public static CaliforniumServer initialize(ActorRef serverActor) {
        CaliforniumServer server = null;

        try {
            server = new CaliforniumServer(serverActor);
            server.start();
            logger.info("Server initialized");

        } catch (SocketException e) {
            logger.error("Failed to initialize server: " + e.getMessage());
        }

        return server;
    }

    /*
     * Constructor for a new Hello-World server. Here, the resources
     * of the server are initialized.
     */
    public CaliforniumServer(ActorRef serverActor) throws SocketException {
        this.serverActor = serverActor;

        // provide an instance of a Hello-World resource
        add(new HelloWorldResource());
    }

    /*
     * Definition of the Hello-World Resource
     */
    class HelloWorldResource extends CoapResource {

        public HelloWorldResource() {

            // set resource identifier
            super("helloWorld");

            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            serverActor.tell("Received message " + exchange.getRequestText(), null);

            // respond to the request
            exchange.respond("Hello World!");
        }

        public void handlePUT(CoapExchange exchange) {
            serverActor.tell("Received put message " + exchange.getRequestText(), null);
            exchange.respond(ResponseCode.CHANGED);
            //changed(); // notify all observers
        }
    }
}
