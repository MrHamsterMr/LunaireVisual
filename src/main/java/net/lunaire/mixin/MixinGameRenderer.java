@Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
private void onGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> info) {
    Module m = ModuleManager.getModule("Zoom");
    if (m != null && m.isEnabled()) {
        float originalFov = info.getReturnValue();
        info.setReturnValue(originalFov / 4.0f);
    }
}
