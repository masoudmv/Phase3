//package model.charactersModel.smiley;
//
//// File: RotationState.java
//private class RotationState {
//    private boolean rotating;
//    private double totalRotationAngle;
//    private double angularSpeed = 5;
//    private double targetAngle;
//
//    public void startRotation(double angle) {
//        rotating = true;
//        rotateTo(angle);
//    }
//
//    public void updateRotation() {
//        if (Math.abs(angle - targetAngle) > 0.001) {
//            double angleDifference = (targetAngle - angle) % 360;
//            double rotationStep = Math.min(Math.abs(angleDifference), angularSpeed) * Math.signum(angleDifference);
//            angle += rotationStep;
//            rotate(rotationStep);
//        } else {
//            rotating = false;
//        }
//    }
//
//    public boolean isRotating() {
//        return rotating;
//    }
//
//    public void setTargetAngle(double targetAngle) {
//        this.targetAngle = targetAngle;
//    }
//}