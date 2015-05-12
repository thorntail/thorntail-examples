package org.wildfly.swarm.jaxrs.cdi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Yoshimasa Tanabe
 */
@Data
@AllArgsConstructor
public class Employee implements Serializable {

  private Long id;
  private String name;

}
