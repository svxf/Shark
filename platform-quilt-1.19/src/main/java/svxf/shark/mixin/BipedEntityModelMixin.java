package svxf.shark.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin {

	@Shadow
	public @Final ModelPart rightArm;

	@Shadow
	public @Final ModelPart leftArm;

	@Inject(
		method = {"positionRightArm", "positionLeftArm"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/entity/model/CrossbowPosing;hold(Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;Z)V",
			shift = Shift.AFTER
		),
		cancellable = true
	)
	public void poseArms(LivingEntity entity, CallbackInfo ci) {
		ItemStack mainHandStack = entity.getMainHandStack();
		ItemStack offHandStack = entity.getOffHandStack();

		if (isTotemOfUndying(mainHandStack) || isTotemOfUndying(offHandStack)) {
			this.rightArm.pitch = -0.95F;
			this.rightArm.yaw = (float) (-Math.PI / 8);
			this.leftArm.pitch = -0.90F;
			this.leftArm.yaw = (float) (Math.PI / 8);
			ci.cancel();
		}
	}

	private boolean isTotemOfUndying(ItemStack stack) {
		Item item = stack.getItem();
		return item == Items.TOTEM_OF_UNDYING;
	}
}
