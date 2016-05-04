/*
 * #%L
 * Wildfly Swarm :: Examples :: Camel Mail
 * %%
 * Copyright (C) 2016 RedHat
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.wildfly.swarm.examples.camel.mail;

import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.mail.MailFraction;

public class Main {
    public static void main(String... args) throws Exception {
        MailFraction fraction = MailFraction.defaultFraction();
        Container container = new Container();

        // Configure mock mail server SMTP session
        container.fraction(fraction.smtpServer("greenmail", s -> s.host("localhost").port("10110").username("user1@localhost").password("password")));

        // Deploy mock mail server
        container.start(true);
        container.deploy(Swarm.artifact("com.icegreen:greenmail-webapp:war:1.4.1"));
        container.deploy();
    }
}
