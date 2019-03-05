package cv1;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int program;

    public Shader(String vertexPath, String fragmentPath) {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        compileShader(vertexShader, vertexPath);
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        compileShader(fragmentShader, fragmentPath);
        createProgram(vertexShader, fragmentShader);
    }

    public void use() {
        glUseProgram(this.program);
    }

    public void setVector4f(String name, float v0, float v1, float v2, float v3) {
        int vertexLoc = glGetUniformLocation(this.program, name);
        glUniform4f(vertexLoc, v0, v1, v2, v3);
    }

    public void setVector3f(String name, float v0, float v1, float v2) {
        int vertexLoc = glGetUniformLocation(this.program, name);
        glUniform3f(vertexLoc, v0, v1, v2);
    }

    public void setVector3f(String name, Vector3f val) {
        setVector3f(name, val.x, val.y, val.z);
    }

    public void setInt(String name, int value) {
        int vertexLoc = glGetUniformLocation(this.program, name);
        glUniform1i(vertexLoc, value);
    }

    public void setFloat(String name, float val) {
        int vertexLoc = glGetUniformLocation(this.program, name);
        glUniform1f(vertexLoc, val);
    }

    public void setMat4(String name, Matrix4f value) {
        int vertexLoc = glGetUniformLocation(this.program, name);
        glUniformMatrix4fv(vertexLoc, false, value.get(BufferUtils.createFloatBuffer(16)));
    }

    private void createProgram(int vertexShader, int fragmentShader) {
        this.program = glCreateProgram();
        glAttachShader(this.program, vertexShader);
        glAttachShader(this.program, fragmentShader);
        glLinkProgram(this.program);

        int status = glGetProgrami(this.program, GL_LINK_STATUS);
        if (status == GL_FALSE) {
            throw new RuntimeException(glGetProgramInfoLog(this.program));
        }
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    private void compileShader(int shaderId, String path) {
        String source = null;
        try {
            source = readAllFromResource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        glShaderSource(shaderId, source);
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException(glGetShaderInfoLog(shaderId));
        }
    }

    private String readAllFromResource(String resource) throws IOException {
        InputStream is = Shader.class.getResourceAsStream(resource);
        if (is == null) {
            throw new IOException("Resource not found: " + resource);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        int c;
        while ((c = reader.read()) != -1) {
            sb.append((char) c);
        }

        return sb.toString();
    }


}
