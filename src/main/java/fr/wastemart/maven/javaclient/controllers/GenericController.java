package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.UserInstance;

public abstract class   GenericController {
    protected UserInstance instance;

    public void init(UserInstance instance) {
        setInstance(instance);
    }

    public void init() {
    }

    protected UserInstance getInstance() {
        return instance;
    }

    protected void setInstance(UserInstance instance) {
        this.instance = instance;
    }
}
