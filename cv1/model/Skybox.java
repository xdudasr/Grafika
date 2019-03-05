package cv1.model;

import cv1.util.TextureFactory;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Skybox  extends Model {
    private static final String[] skyboxResurces = {
            "/resources/textures/skybox/right.jpg",
            "/resources/textures/skybox/left.jpg",
            "/resources/textures/skybox/top.jpg",
            "/resources/textures/skybox/bottom.jpg",
            "/resources/textures/skybox/front.jpg",
            "/resources/textures/skybox/back.jpg",
    };


    public Skybox() {
        super(skyboxVertices);
        this.setAttrib(0, 3, 3, 0);

        try {
            setTexture(TextureFactory.loadCubeMap(skyboxResurces));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindTextures() {
        glBindVertexArray(this.getVAO());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, this.getTextures().get(0));

    }

    public Matrix4f getViewMatrix(Matrix4f camera) {
        Matrix3f trimmedView = new Matrix3f();
        Matrix4f result = new Matrix4f();
        camera.get3x3(trimmedView);
        trimmedView.get(result);
        return result;
    }

    public void draw() {
        glDepthFunc(GL_LEQUAL);
        this.bindTextures();
        glDrawArrays(GL_TRIANGLES, 0, 36);
        glBindVertexArray(0);
        glDepthFunc(GL_LESS);
    }

    private static final float[] skyboxVertices = {
        //pos
        -1.0f, 1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,

                -1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, 1.0f,
                -1.0f, -1.0f, 1.0f,

                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,

                -1.0f, -1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, 1.0f,

                -1.0f, 1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                1.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, -1.0f,

                -1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, 1.0f
    };
}
