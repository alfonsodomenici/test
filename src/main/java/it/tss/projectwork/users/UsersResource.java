/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.projectwork.users;

import it.tss.projectwork.posts.PostsResource;
import java.security.Principal;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 *
 * @author alfonso
 */
@Path("/users")
@DenyAll
public class UsersResource {

    @Inject
    @Claim(standard = Claims.upn)
    private String upn;

    @Inject
    private Principal principal;

    @Inject
    JsonWebToken jwt;

    @Inject
    UserStore store;

    @PostConstruct
    public void init() {
        logAuth();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("users")
    public Collection<User> all(@QueryParam("search") String search) {
        return search == null ? store.all() : store.search(search);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("users")
    public User find(@PathParam("id") Long id) {
        User found = store.find(id);
        if (found == null) {
            throw new NotFoundException();
        }
        return found;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response create(User u) {
        User saved = store.create(u);
        return Response
                .status(Response.Status.CREATED)
                .entity(saved)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response create(
            @FormParam("firstName") String fname,
            @FormParam("lastName") String lname,
            @FormParam("usr") String usr,
            @FormParam("pwd") String pwd) {

        User user = new User(fname, lname, usr, pwd);
        User saved = store.create(user);
        return Response
                .status(Response.Status.CREATED)
                .entity(saved)
                .build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("users")
    public User update(@PathParam("id") Long id, User u) {
        if (u.getId() == null || !u.getId().equals(id)) {
            throw new BadRequestException();
        }
        return store.update(u);
    }

    @PATCH
    @Path("{id}/firstname")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("users")
    public User updateFirstName(@PathParam("id") Long id, JsonObject json) {
        User found = store.find(id);
        found.setFirstName(json.getString("firstName"));
        return store.update(found);
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed("users")
    public Response delete(@PathParam("id") Long id) {
        User found = store.find(id);
        if (found == null) {
            throw new NotFoundException();
        }
        store.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    
    private void logAuth() {
        System.out.println("************************** UPN ********************************");
        System.out.println("");
        System.out.println("username: " + upn);
        System.out.println("");
        System.out.println("************************** Principal ********************************");
        System.out.println("");
        System.out.println("principal: " + principal);
        System.out.println("");
        System.out.println("************************** JWT Token ********************************");
        System.out.println("");
        System.out.println("jwt: " + jwt);
        System.out.println("************************** JWT Token RAW ********************************");
        System.out.println("");
        System.out.println("jwt: " + jwt.getRawToken());
    }
}
