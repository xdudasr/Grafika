package cv1;

import cv1.util.VectorUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    public enum Movement {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
    }

    private Vector3f position = new Vector3f(0f, 0f, 0f);
    private Vector3f front = new Vector3f(0f, 0f, -1f);
    private Vector3f up = new Vector3f(0f, 1f, 0f);
    private Vector3f right;
    private Vector3f worldUp;

    private float yaw = -90.0f;
    private float pitch = 0f;

    private float movementSpeed = 2.5f;
    private float mouseSensitivity = 0.1f;
    private float zoom = 45f;

    public Camera(Vector3f position, Vector3f up) {
        this.position = position;
        this.worldUp = up;
        updateCameraVectors();
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(this.position, VectorUtils.add(this.position, this.front), this.up);
    }

    public void processKeyboard(Movement direction, float deltaTime) {
        float velocity = this.movementSpeed * deltaTime;
        switch (direction) {
            case FORWARD: {
                this.position.add(VectorUtils.mul(this.front, velocity));
                break;
            }
            case BACKWARD: {
                this.position.sub(VectorUtils.mul(this.front, velocity));
                break;
            }
            case LEFT: {
                this.position.sub(VectorUtils.mul(this.right, velocity));
                break;
            }
            case RIGHT: {
                this.position.add(VectorUtils.mul(this.right, velocity));
            }
        }
    }

    public void processMouseMovement(float xOffset, float yOffset, boolean constrainPitch) {
        this.yaw = this.yaw + (xOffset * this.mouseSensitivity);
        this.pitch = this.pitch + (yOffset * this.mouseSensitivity);

        if (constrainPitch) {
            if (this.pitch > 89.0f) {
                this.pitch = 89.0f;
            }
            else if (this.pitch < -89.0f) {
                this.pitch = 89.0f;
            }
        }
        updateCameraVectors();
    }

    public void processMouseMovement(float xOffset, float yOffset) {
        processMouseMovement(xOffset, yOffset, true);
    }

    public void processMouseScroll(float yOffset) {
        if (this.zoom >= 1.0f && this.zoom <= 45.0f) {
            this.zoom = this.zoom - yOffset;
        }
        if (this.zoom <= 1.0f) {
            this.zoom = 1.0f;
        }
        if (this.zoom >=  45.0f) {
            this.zoom = 45.0f;
        }
    }

    public float getZoom() {
        return zoom;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getFront() {
        return front;
    }

    public Vector3f getUp() {
        return up;
    }

    public Vector3f getRight() {
        return right;
    }

    public Vector3f getWorldUp() {
        return worldUp;
    }

    private void updateCameraVectors() {
        Vector3f front = new Vector3f();
        double pitchRad = Math.toRadians((double)this.pitch);
        double yawRad = Math.toRadians((double)this.yaw);

        front.x = (float)(Math.cos(yawRad) * Math.cos(pitchRad));
        front.y = (float)(Math.sin(pitchRad));
        front.z = (float)(Math.sin(yawRad) * Math.cos(pitchRad));

        this.front = front.normalize();
        this.right = VectorUtils.cross(this.front, this.worldUp).normalize();
        this.up = VectorUtils.cross(this.right, this.front).normalize();
    }
}
