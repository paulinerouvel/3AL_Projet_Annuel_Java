package fr.wastemart.maven.javaclient.controllerGeneration;

import fr.wastemart.maven.javaclient.annotation.AutoImplement;

@AutoImplement(name = "NewController", details = true)
interface NewGeneratedController {
    String getWarehouse();
    String getDestinataire();


}
