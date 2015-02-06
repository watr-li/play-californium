/*******************************************************************************
 * Copyright (c) 2014 Institute for Pervasive Computing, ETH Zurich and others.
 * Modifications (c) 2015 Watr.li
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

import actors.messages.*;

import akka.actor.*;
import static akka.pattern.Patterns.ask;

/**
 * Eclipse Californium server that handles all received CoAP messages and forwards them
 * to the Play application.
 */
public class CaliforniumServer extends CoapServer {

    final static Logger.ALogger logger = Logger.of("CaliforniumServer");

    // Reference to the actor that will be notified if a CoAP message is received.
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
     * The CoAP resources of the server are initialized.
     */
    public CaliforniumServer(ActorRef serverActor) throws SocketException {
        this.serverActor = serverActor;

        // provide an instance of a Hello-World resource
        add(new HelloWorldResource());
    }

    class HelloWorldResource extends CoapResource {

        public HelloWorldResource() {
            // set resource identifier
            super("helloWorld");
            getAttributes().setTitle("Hello-World Resource");
        }

        @Override
        public void handlePUT(CoapExchange exchange) {
            serverActor.tell(new CoapMessageReceived(exchange.getRequestText()), null);
            exchange.respond(ResponseCode.CHANGED);
        }
    }
}
