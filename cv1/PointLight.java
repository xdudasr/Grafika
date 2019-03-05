package cv1;

import org.joml.Vector3f;

public class PointLight {
    private Vector3f position;
    private Vector3f ambient = new Vector3f(0.05f, 0.05f, 0.05f);
    private Vector3f diffuse = new Vector3f(0.8f, 0.8f, 0.8f);
    private Vector3f specular = new Vector3f(1.0f, 1.0f, 1.0f);
    private float constant = 1f;
    private float linear = 0.09f;
    private float quadratic = 0.032f;

    public PointLight(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getAmbient() {
        return ambient;
    }

    public void setAmbient(Vector3f ambient) {
        this.ambient = ambient;
    }

    public Vector3f getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(Vector3f diffuse) {
        this.diffuse = diffuse;
    }

    public Vector3f getSpecular() {
        return specular;
    }

    public void setSpecular(Vector3f specular) {
        this.specular = specular;
    }

    public float getConstant() {
        return constant;
    }

    public void setConstant(float constant) {
        this.constant = constant;
    }

    public float getLinear() {
        return linear;
    }

    public void setLinear(float linear) {
        this.linear = linear;
    }

    public float getQuadratic() {
        return quadratic;
    }

    public void setQuadratic(float quadratic) {
        this.quadratic = quadratic;
    }
}
