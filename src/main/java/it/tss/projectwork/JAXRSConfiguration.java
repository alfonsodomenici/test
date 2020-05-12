package it.tss.projectwork;

import javax.annotation.security.DeclareRoles;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

/**
 * Configures a JAX-RS endpoint. Delete this class, if you are not exposing
 * JAX-RS resources in your application.
 *
 * @author airhacks.com
 */
@ApplicationScoped
@LoginConfig(authMethod = "MP-JWT", realmName = "")
@ApplicationPath("resources")
@DeclareRoles({"users"})
public class JAXRSConfiguration extends Application {

}
