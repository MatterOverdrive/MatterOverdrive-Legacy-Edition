package matteroverdrive.container;

import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 6/15/2015.
 */
public class ContainerStarMap extends ContainerMachine<TileEntityMachineStarMap> {
    public ContainerStarMap() {
        super();
    }

    public ContainerStarMap(InventoryPlayer inventory, TileEntityMachineStarMap machine) {
        super(inventory, machine);
    }

    @Override
    protected void init(InventoryPlayer inventory) {

        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 270, true, true);
    }
}
