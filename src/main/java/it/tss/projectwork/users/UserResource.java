/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.projectwork.users;

import it.tss.projectwork.posts.PostsResource;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author alfonso
 */
public class UserResource {

    @Context
    ResourceContext resource;

    @Inject
    UserStore store;

    private Long id;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("users")
    public User find() {
        User found = store.find(id);
        if (found == null) {
            throw new NotFoundException();
        }
        return found;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("users")
    public User update(User u) {
        if (u.getId() == null || !u.getId().equals(id)) {
            throw new BadRequestException();
        }
        return store.update(u);
    }

    @PATCH
    @Path("firstname")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("users")
    public User updateFirstName(JsonObject json) {
        User found = store.find(id);
        found.setFirstName(json.getString("firstName"));
        return store.update(found);
    }

    @DELETE
    @RolesAllowed("users")
    public Response delete() {
        User found = store.find(id);
        if (found == null) {
            throw new NotFoundException();
        }
        store.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Path("posts")
    @RolesAllowed("users")
    public PostsResource posts() {
        User found = store.find(id);
        if (found == null) {
            throw new NotFoundException();
        }
        PostsResource sub = resource.getResource(PostsResource.class);
        sub.setUserId(id);
        return sub;
    }

    /*
    getter/setter
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
