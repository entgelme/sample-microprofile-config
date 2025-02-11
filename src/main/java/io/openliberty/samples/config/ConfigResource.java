// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
// end::copyright[]
package io.openliberty.config;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.MediaType;

import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.Json;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigSource;

@RequestScoped
@Path("/")
public class ConfigResource {

  // tag::config[]
  @Inject
  private Config config;
  // end::config[]

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public JsonObject getAllConfig() {
    JsonObjectBuilder builder = Json.createObjectBuilder();
    return builder.add("ConfigSources", sourceJsonBuilder()).build();
  }

  public JsonObject sourceJsonBuilder() {
    JsonObjectBuilder sourcesBuilder = Json.createObjectBuilder();

    for (ConfigSource source : config.getConfigSources()) {
      System.out.println(source.getName() + ": \n");
      JsonObjectBuilder propertyBuilder = Json.createObjectBuilder();

      for (String name : source.getPropertyNames()) {
        String val = source.getValue(name);
        propertyBuilder.add(name, (val == "")? "-" : val);
      }
      sourcesBuilder.add(source.getName(), Json.createObjectBuilder()
        .add("Ordinal", source.getOrdinal())
        .add("Properties", propertyBuilder));
    }
    return sourcesBuilder.build();
  }
}
