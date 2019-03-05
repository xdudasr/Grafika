package cv1.model;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Model {
    private float[] vertices;
    private int VAO;
    private int VBO;
    private List<Integer> textures = new ArrayList<Integer>();

    public Model(float[] vertices) {
        this.vertices = vertices;
        this.VAO = glGenVertexArrays();
        this.VBO = glGenBuffers();
        glBindVertexArray(this.VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, this.vertices, GL_STATIC_DRAW);
    }

    public void setAttrib(int index, int size, int stride, int offset) {
        glVertexAttribPointer(index, size, GL_FLOAT, false, stride * Float.BYTES, offset);
        glEnableVertexAttribArray(index);
    }

    public void bindTextures() {
        glBindVertexArray(this.VAO);
        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i);
            glBindTexture(GL_TEXTURE_2D, textures.get(i));
        }
    }

    public void setTexture(int texture) {
        this.textures.add(texture);
    }

    public void destroy() {
        glDeleteVertexArrays(this.getVAO());
        glDeleteBuffers(this.getVBO());
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public int getVAO() {
        return VAO;
    }

    public void setVAO(int VAO) {
        this.VAO = VAO;
    }

    public int getVBO() {
        return VBO;
    }

    public void setVBO(int VBO) {
        this.VBO = VBO;
    }

    public List<Integer> getTextures() {
        return textures;
    }
}
