package fr.wastemart.maven.javaclient.services.Details;

import fr.wastemart.maven.javaclient.models.Warehouse;

public class WarehouseDetail implements Detail {
    private Warehouse warehouse;

    public WarehouseDetail(Warehouse warehouseDetail) {
        warehouse = warehouseDetail;
    }

    public Warehouse getValue(){
        return warehouse;
    }


}