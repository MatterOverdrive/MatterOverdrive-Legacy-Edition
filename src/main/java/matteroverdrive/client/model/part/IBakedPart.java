package matteroverdrive.client.model.part;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;

import java.util.List;

public interface IBakedPart<P extends IBakedPart<P>> {

    List<BakedQuad> addToList(List<BakedQuad> list, EnumFacing facing);

    P setNoCull();

    P setNoCull(EnumFacing face);

}
