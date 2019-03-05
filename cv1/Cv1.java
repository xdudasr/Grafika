package cv1;

import cv1.model.Skybox;
import cv1.util.ObjLoader;
import cv1.util.Object;
import cv1.util.TextureFactory;
import cv1.util.VectorUtils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.opengl.*;

import java.awt.*;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.NULL;




import cv1.SoundRunnable;
public class Cv1 {

    Sound sound =new Sound();
    //settings
    int SCR_WIDTH = (int)(1280);
    int SCR_HEIGHT = (int)(720);
    float heightScale = 0.1f;

    //camera
    Camera camera = new Camera(new Vector3f(0f, 0f, 3f), new Vector3f(0f, 1f, 0f));
    float lastX = SCR_WIDTH / 2.0f;
    float lastY = SCR_HEIGHT / 2.0f;
    boolean firstMouse = true;

    // timing
    float deltaTime = 0.0f;    // time between current frame and last frame
    float lastFrame = 0.0f;

    private boolean light=true;

    public static void main(String[] args) {
        new Cv1().run();
    }

    private void run() {

        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        long window = init();


        Timer timer = new Timer();

        timer.schedule( new TimerTask() {
            public void run() {
                //   sound.play("/Metronome.wav");
                sound.play("E:\\Project_final_graphic5\\src\\cv1\\ocean1.wav");
            }
        }, 0, 10*1000);

        /*
         * configure global opengl state
         */
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);













        /*
         * SHADERS
         */
        Shader modelShader = new Shader("/resources/shaders/model.vs.glsl", "/resources/shaders/model.fs.glsl");
        Shader modelShader1 = new Shader("/resources/shaders/model.vs.glsl", "/resources/shaders/model.fs.glsl");
        Shader lampShader = new Shader("/resources/shaders/lamp.vs.glsl", "/resources/shaders/lamp.fs.glsl");
        Shader lampShader1 = new Shader("/resources/shaders/lamp.vs.glsl", "/resources/shaders/lampp.fs.glsl");
        Shader lampShader2 = new Shader("/resources/shaders/lamp.vs.glsl", "/resources/shaders/lamppp.fs.glsl");
        Shader skyboxShader = new Shader("/resources/shaders/skybox.vs.glsl", "/resources/shaders/skybox.fs.glsl");
        Shader basicShader = new Shader("/resources/shaders/basic.vs.glsl", "/resources/shaders/basic.fs.glsl");
        Shader parallaxShader = new Shader("/resources/shaders/parallax.vs.glsl", "/resources/shaders/parallax.fs.glsl");
        Shader procShader = new Shader("/resources/shaders/procmodel.vs.glsl", "/resources/shaders/procmodel.fs.glsl");

        float vertices[] = {
                //position (3) + normal(3) + tex coord (2)
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,

                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,

                -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

                0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,

                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f
        };

        float houseData[] = {
                //position (3) + normal(3) + tex coord (2)
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f,
                0.5f, 0.25f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
                0.5f, 0.25f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
                -0.5f, 0.25f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,

                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                0.5f, 0.25f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                0.5f, 0.25f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                -0.5f, 0.25f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,

                -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

                0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,

                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f
        };

        float[] transperentVertices = {
                // pos + text coord
                0.0f, 0.5f, 0.0f, 0.0f, 0.0f,
                0.0f, -0.5f, 0.0f, 0.0f, 1.0f,
                1.0f, -0.5f, 0.0f, 1.0f, 1.0f,

                0.0f, 0.5f, 0.0f, 0.0f, 0.0f,
                1.0f, -0.5f, 0.0f, 1.0f, 1.0f,
                1.0f, 0.5f, 0.0f, 1.0f, 0.0f
        };

        float[] quadVertices = getQuadVertices();

        Vector3f[] cubePositions = {
                new Vector3f(3.0f, 0.0f, 2.0f),
                new Vector3f(3.6f, 5.0f, -15.0f),
                new Vector3f(-3.5f, -2.2f, -2.5f),
                new Vector3f(-3.8f, -2.0f, -12.3f),
                new Vector3f(3.7f, -0.4f, -3.5f),
                new Vector3f(-3.7f, 3.0f, -7.5f),
                new Vector3f(3.9f, -2.0f, -2.5f),
                new Vector3f(3.5f, 2.0f, -2.5f),
                new Vector3f(3.5f, 0.2f, -2.5f),
                new Vector3f(-3.3f, 3.0f, -2.5f),
        };

        Vector3f[] pointLightPositions1 = {
                new Vector3f(2.0f, 0.0f, 1.5f),
                new Vector3f(2.0f, 0.0f, -0.5f),
                new Vector3f(2.0f, 0.0f, 0.5f),
                new Vector3f(2.0f, 0.0f, -1.5f),
        };


        Vector3f[] pointLightPositions = {
                new Vector3f(-2.0f, 0.0f, 1.5f),
                new Vector3f(-2.0f, 0.0f, -0.5f),
                new Vector3f(-2.0f, 0.0f, 0.5f),
                new Vector3f(-2.0f, 0.0f, -1.5f),
                new Vector3f(-2.0f, 2.0f, 1.5f),
                new Vector3f(-2.0f, 2.0f, -0.5f),
                new Vector3f(-2.0f, 2.0f, 0.5f),
                new Vector3f(-2.0f, 2.0f, -1.5f),
                new Vector3f(2.0f, 0.0f, 1.5f),
                new Vector3f(2.0f, 0.0f, -0.5f),
                new Vector3f(2.0f, 0.0f, 0.5f),
                new Vector3f(2.0f, 0.0f, -1.5f),
        };


        Vector3f[] pointLightPositions2 = {
                new Vector3f(2.0f, 1.0f, 1.5f),
                new Vector3f(2.0f, 1.0f, -0.5f),
                new Vector3f(2.0f, 1.0f, 0.5f),
                new Vector3f(2.0f, 1.0f, -1.5f),
        };

        //TODO blednding

        Vector3f[] windowPositions = {
               // first side
                new Vector3f(0.0f, 2.78f, -2.0f),
                new Vector3f(-1.0f, 2.78f, -2.0f),
                new Vector3f(1.0f, 2.78f, -2.0f),
                new Vector3f(-2.0f, 2.78f, -2.0f),
               // secod side
                new Vector3f(0.0f, 2.78f, 2.0f),
                new Vector3f(-1.0f, 2.78f, 2.0f),
                new Vector3f(1.0f, 2.78f, 2.0f),
                new Vector3f(-2.0f, 2.78f, 2.0f),
        };

        Vector3f housePosition = new Vector3f(0f, 1.28f, 0f);



        int cubeVAO = glGenVertexArrays();
        int houseVAO = glGenVertexArrays();
        int lightVAO = glGenVertexArrays();
        int lightVAO1 = glGenVertexArrays();
        int transperentVAO = glGenVertexArrays();
        int teapotVAO = glGenVertexArrays();
        int quadVAO = glGenVertexArrays();
        int floorVAO = glGenVertexArrays();
        int vaseVAO = glGenVertexArrays();
        int cubeeVAO = glGenVertexArrays();


        int teapothighVAO = glGenVertexArrays();
        int libraryVAO = glGenVertexArrays();
        int earthVAO = glGenVertexArrays();
        int boxVAO = glGenVertexArrays();
        int binVAO = glGenVertexArrays();

        int bedVAO = glGenVertexArrays();

        int VBO = glGenBuffers();
        int teapotVBO = glGenBuffers();
        int transperentVBO = glGenBuffers();
        int quadVBO = glGenBuffers();
        int houseVBO = glGenBuffers();
        int vaseVBO = glGenBuffers();

        int teapothighVBO = glGenBuffers();
        int libraryVBO = glGenBuffers();
        int earthVBO = glGenBuffers();
        int boxVBO = glGenBuffers();
        int binVBO = glGenBuffers();

        int cubeeVBO = glGenBuffers();
        // quad binding

        glBindVertexArray(quadVAO); // generuje pole kde su vrcholy
        glBindBuffer(GL_ARRAY_BUFFER, quadVBO); // kontainer
        glBufferData(GL_ARRAY_BUFFER, quadVertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 14 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 14 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 14 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(3, 3, GL_FLOAT, false, 14 * Float.BYTES, 8 * Float.BYTES);
        glEnableVertexAttribArray(3);
        glVertexAttribPointer(4, 3, GL_FLOAT, false, 14 * Float.BYTES, 11 * Float.BYTES);
        glEnableVertexAttribArray(4);

        // cube binding
        glBindVertexArray(cubeVAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0); //pozicie
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES); //normaly
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES); //cordinatt textury
        glEnableVertexAttribArray(2);

        // house binding
        glBindVertexArray(houseVAO);
        glBindBuffer(GL_ARRAY_BUFFER, houseVBO);
        glBufferData(GL_ARRAY_BUFFER, houseData, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);

        // light binding
        glBindVertexArray(lightVAO);
       glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // skybox binding
        Skybox skybox = new Skybox();

        // transperent binding
        glBindVertexArray(transperentVAO);
        glBindBuffer(GL_ARRAY_BUFFER, transperentVBO);
        glBufferData(GL_ARRAY_BUFFER, transperentVertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // floor binding
        glBindVertexArray(floorVAO);
        glBindBuffer(GL_ARRAY_BUFFER, quadVBO);
        glBufferData(GL_ARRAY_BUFFER, getQuadVertices(), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 14 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 14 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 14 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);

        // load vase and fill buffer with vase data
        ObjLoader vase = new ObjLoader("/resources/objects/vase.obj");
        try {
            vase.load();
        } catch (IOException ex) {
            Logger.getLogger(Cv1.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        // vase binding
        int length = 3 * 8 * vase.getTriangleCount();
        FloatBuffer vaseBuff = BufferUtils.createFloatBuffer(length);
        for (int f = 0; f < vase.getTriangleCount(); f++) {
            int[] pi = vase.getVertexIndices().get(f);
            int[] ni = vase.getNormalIndices().get(f);
            int[] ti = vase.getTexcoordIndices().get(f);
            for (int i = 0; i < 3; i++) {
                float[] position = vase.getVertices().get(pi[i]);
                float[] normal = vase.getNormals().get(ni[i]);
                float[] texCoord = vase.getTexcoords().get(ti[i]);
                vaseBuff.put(position);
                vaseBuff.put(normal);
                vaseBuff.put(texCoord);
            }
        }

        vaseBuff.rewind();
        glBindVertexArray(vaseVAO);
        glBindBuffer(GL_ARRAY_BUFFER, vaseVBO);
        glBufferData(GL_ARRAY_BUFFER, vaseBuff, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);


        // load teoapot and fill buffer with tepapot data
        ObjLoader earth = new ObjLoader("/resources/objects/earth.obj");
        try {
            earth.load();
        } catch (IOException ex) {
            Logger.getLogger(Cv1.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        // vase binding
        int lengtht = 3 * 8 * earth.getTriangleCount();
        FloatBuffer earthhBuff = BufferUtils.createFloatBuffer(lengtht);
        for (int f = 0; f < earth.getTriangleCount(); f++) {
            int[] pi = earth.getVertexIndices().get(f);
            int[] ni = earth.getNormalIndices().get(f);
            int[] ti = earth.getTexcoordIndices().get(f);
            for (int i = 0; i < 3; i++) {
                float[] position = earth.getVertices().get(pi[i]);
                float[] normal = earth.getNormals().get(ni[i]);
                float[] texCoord = earth.getTexcoords().get(ti[i]);
                earthhBuff.put(position);
                earthhBuff.put(normal);
                earthhBuff.put(texCoord);
            }
        }

        earthhBuff.rewind();
        glBindVertexArray(earthVAO);
        glBindBuffer(GL_ARRAY_BUFFER, earthVBO);
        glBufferData(GL_ARRAY_BUFFER, earthhBuff, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);


        // load teoapot and fill buffer with tepapot data
        ObjLoader box = new ObjLoader("/resources/objects/box.obj");
        try {
            box.load();
        } catch (IOException ex) {
            Logger.getLogger(Cv1.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        // vase binding
        int length3 = 3 * 8 * box.getTriangleCount();
        FloatBuffer boxBuff = BufferUtils.createFloatBuffer(length3);
        for (int f = 0; f < box.getTriangleCount(); f++) {
            int[] pi = box.getVertexIndices().get(f);
            int[] ni = box.getNormalIndices().get(f);
            int[] ti = box.getTexcoordIndices().get(f);
            for (int i = 0; i < 3; i++) {
                float[] position = box.getVertices().get(pi[i]);
                float[] normal = box.getNormals().get(ni[i]);
                float[] texCoord = box.getTexcoords().get(ti[i]);
                boxBuff.put(position);
                boxBuff.put(normal);
                boxBuff.put(texCoord);
            }
        }

        boxBuff.rewind();
        glBindVertexArray(boxVAO);
        glBindBuffer(GL_ARRAY_BUFFER, boxVBO);
        glBufferData(GL_ARRAY_BUFFER, boxBuff, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);


        // load teoapot and fill buffer with tepapot data
        ObjLoader bin = new ObjLoader("/resources/objects/bin.obj");
        try {
            bin.load();
        } catch (IOException ex) {
            Logger.getLogger(Cv1.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        // vase binding
        int length4 = 3 * 8 * bin.getTriangleCount();
        FloatBuffer binBuff = BufferUtils.createFloatBuffer(length4);
        for (int f = 0; f < bin.getTriangleCount(); f++) {
            int[] pi = bin.getVertexIndices().get(f);
            int[] ni = bin.getNormalIndices().get(f);
            int[] ti = bin.getTexcoordIndices().get(f);
            for (int i = 0; i < 3; i++) {
                float[] position = bin.getVertices().get(pi[i]);
                float[] normal = bin.getNormals().get(ni[i]);
                float[] texCoord = bin.getTexcoords().get(ti[i]);
                binBuff.put(position);
                binBuff.put(normal);
                binBuff.put(texCoord);
            }
        }

        binBuff.rewind();
        glBindVertexArray(binVAO);
        glBindBuffer(GL_ARRAY_BUFFER, binVBO);
        glBufferData(GL_ARRAY_BUFFER, binBuff, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);


        // load teoapot and fill buffer with tepapot data
        ObjLoader teapothigh = new ObjLoader("/resources/objects/teapot-high.obj");
        try {
            teapothigh.load();
        } catch (IOException ex) {
            Logger.getLogger(Cv1.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        // vase binding
        int length5 = 3 * 8 * teapothigh.getTriangleCount();
        FloatBuffer teapothighBuff = BufferUtils.createFloatBuffer(length5);
        for (int f = 0; f < teapothigh.getTriangleCount(); f++) {
            int[] pi = teapothigh.getVertexIndices().get(f);
            int[] ni = teapothigh.getNormalIndices().get(f);
            int[] ti = teapothigh.getTexcoordIndices().get(f);
            for (int i = 0; i < 3; i++) {
                float[] position = teapothigh.getVertices().get(pi[i]);
                float[] normal = teapothigh.getNormals().get(ni[i]);
                float[] texCoord = teapothigh.getTexcoords().get(ti[i]);
                teapothighBuff.put(position);
                teapothighBuff.put(normal);
                teapothighBuff.put(texCoord);
            }
        }

        teapothighBuff.rewind();
        glBindVertexArray(teapothighVAO);
        glBindBuffer(GL_ARRAY_BUFFER, teapothighVBO);
        glBufferData(GL_ARRAY_BUFFER, teapothighBuff, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);

        // load teoapot and fill buffer with tepapot data
        ObjLoader cubee = new ObjLoader("/resources/objects/cube.obj");
        try {
            cubee.load();
        } catch (IOException ex) {
            Logger.getLogger(Cv1.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        // vase binding
        int length6 = 3 * 8 * cubee.getTriangleCount();
        FloatBuffer cubeeBuff = BufferUtils.createFloatBuffer(length6);
        for (int f = 0; f < cubee.getTriangleCount(); f++) {
            int[] pi = cubee.getVertexIndices().get(f);
            int[] ni = cubee.getNormalIndices().get(f);
            int[] ti = cubee.getTexcoordIndices().get(f);
            for (int i = 0; i < 3; i++) {
                float[] position = cubee.getVertices().get(pi[i]);
                float[] normal = cubee.getNormals().get(ni[i]);
                float[] texCoord = cubee.getTexcoords().get(ti[i]);
                cubeeBuff.put(position);
                cubeeBuff.put(normal);
                cubeeBuff.put(texCoord);
            }
        }

        cubeeBuff.rewind();
        glBindVertexArray(cubeeVAO);
        glBindBuffer(GL_ARRAY_BUFFER, cubeeVBO);
        glBufferData(GL_ARRAY_BUFFER, cubeeBuff, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);


        // load bed and fill buffer with bed data
        ObjLoader library = new ObjLoader("/resources/objects/library.obj");
        try {
            library.load();
        } catch (IOException ex) {
            Logger.getLogger(Cv1.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        // bed binding
        int lengthh = 3 * 8 * library.getTriangleCount();
        FloatBuffer libraryBuff = BufferUtils.createFloatBuffer(lengthh);
        for (int f = 0; f < library.getTriangleCount(); f++) {
            int[] pi = library.getVertexIndices().get(f);
            int[] ni = library.getNormalIndices().get(f);
            int[] ti = library.getTexcoordIndices().get(f);
            for (int i = 0; i < 3; i++) {
                float[] position = library.getVertices().get(pi[i]);
                float[] normal = library.getNormals().get(ni[i]);
                float[] texCoord = library.getTexcoords().get(ti[i]);
                libraryBuff.put(position);
                libraryBuff.put(normal);
                libraryBuff.put(texCoord);
            }
        }

        libraryBuff.rewind();
        glBindVertexArray(libraryVAO);
        glBindBuffer(GL_ARRAY_BUFFER, libraryVBO);
        glBufferData(GL_ARRAY_BUFFER, libraryBuff, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);






        int containerDiffuseMap = 0, containerSpecularMap = 0, windowTexture = 0;
        int diffuseMap = 0, normalMap = 0, heightMap = 0, metal = 0, metalSpec = 0;
        int globeTexture=0;
        int wood2=0;
        int wood2Spec=0;
        int woodTexture=0;
        int steelTexture=0;
        int paraquetTexture=0;
        int globeTextureSpec=0;
        int  woodTextureSpec=0;
        int crystalTexture =0;
        int crystalTextureSpec=0;
        int goldTexture=0;
        int goldTextureSpec=0;
        int wood3Texture=0;
        int wood3TextureSpec=0;
        int meatal1Texture=0;
        int  metal1TextureSpec=0;
        int wood4Texture=0;
        int wood4TextureSpec=0;
        int wood5Texture=0;
        int wood5TextureSpec=0;
        int floor = 0, floorSpec = 0, house = 0, houseSpec = 0;
        try {
            diffuseMap = TextureFactory.loadTexture("/resources/textures/grass/Grass_003_COLOR.jpg");
            normalMap = TextureFactory.loadTexture("/resources/textures/grass/Grass_003_NRM.jpg");
            heightMap = TextureFactory.loadTexture("/resources/textures/grass/Grass_003_DISP.png");
            containerDiffuseMap = TextureFactory.loadTexture("/resources/textures/container2.png");
            containerSpecularMap = TextureFactory.loadTexture("/resources/textures/container2_specular.png");
            metal = TextureFactory.loadTexture("/resources/textures/metal/Metal_dented_001_COLOR.jpg");
            metalSpec = TextureFactory.loadTexture("/resources/textures/metal/Metal_dented_001_SPEC.jpg");
            floor = TextureFactory.loadTexture("/resources/textures/wood-floor/Wood_Floor_007_COLOR.jpg");
            floorSpec = TextureFactory.loadTexture("/resources/textures/wood-floor/Wood_Floor_007_ROUGH.jpg");
            house = TextureFactory.loadTexture("/resources/textures/Concrete_panels_001_SD/Concrete_Panels_001_COLOR.jpg");
            houseSpec = TextureFactory.loadTexture("/resources/textures/Concrete_panels_001_SD/Concrete_Panels_001_ROUGH.jpg");
            windowTexture = TextureFactory.loadTexture("/resources/textures/Window_256.png");



            globeTexture = TextureFactory.loadTexture("/resources/textures/Aspen_bark_001_SD/Aspen_bark_001_COLOR.jpg");
            globeTextureSpec = TextureFactory.loadTexture("/resources/textures/Aspen_bark_001_SD/Aspen_bark_001_SPEC.jpg");


            crystalTexture = TextureFactory.loadTexture("/resources/textures/Crystal_Metal_001_SD/Crystal_Metal_001_COLOR.jpg");
            crystalTextureSpec = TextureFactory.loadTexture("/resources/textures/Crystal_Metal_001_SD/Crystal_Metal_001_ROUGH.jpg");



            goldTexture = TextureFactory.loadTexture("/resources/textures/Gold_Nugget_001_SD/Gold_Nugget_001_COLOR.jpg");
            goldTextureSpec = TextureFactory.loadTexture("/resources/textures/Gold_Nugget_001_SD/Gold_Nugget_001_ROUGH.jpg");


            meatal1Texture = TextureFactory.loadTexture("/resources/textures/Metal_Mesh_001_SD/Metal_Mesh_001_COLOR.jpg");
            metal1TextureSpec = TextureFactory.loadTexture("/resources/textures/Metal_Mesh_001_SD/Metal_Mesh_001_SPEC.jpg");


            wood3Texture = TextureFactory.loadTexture("/resources/textures/Particle Board 001_SD/Particle_Board_001_COLOR.jpg");
            wood3TextureSpec = TextureFactory.loadTexture("/resources/textures/Particle Board 001_SD/Particle_Board_001_ROUGH.jpg");

            wood4Texture = TextureFactory.loadTexture("/resources/textures/Wood Floor_006_SD/Wood_Floor_006_COLOR.jpg");
            wood4TextureSpec = TextureFactory.loadTexture("/resources/textures/Wood Floor_006_SD/Wood_Floor_006_ROUGH.jpg");


            wood5Texture = TextureFactory.loadTexture("/resources/textures/Wood_005_SD/Wood_005_COLOR.jpg");
            wood5TextureSpec = TextureFactory.loadTexture("/resources/textures/Wood_005_SD/Wood_005_ROUGH.jpg");



        } catch (IOException e) {
            e.printStackTrace();
        }
//on the floor
        parallaxShader.use();
        parallaxShader.setInt("diffuseMap", 0);
        parallaxShader.setInt("normalMap", 1);
        parallaxShader.setInt("depthMap", 2);

        modelShader.use();
        modelShader.setInt("material.diffuse", 0);
        modelShader.setInt("material.specular", 1);

        skyboxShader.use();
        skyboxShader.setInt("skybox", 0);

        while (!glfwWindowShouldClose(window)) {
            /*
             * time logic for camera
             */
            float currentFrame = (float) glfwGetTime();
            deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;

            /*
             * input
             */
            processInput(window);

            /*
             * render
             */
            glClearColor(0.2f, 0.3f, 0.3f, 1f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


            /*model shader*/
            modelShader.use();
            modelShader.setVector3f("viewPos", camera.getPosition());
            modelShader.setFloat("material.shininess", 32.0f);

            /*
             * directional light
             */
            modelShader.setVector3f("dirLight.direction", -0.2f, -1.0f, -0.3f);
            modelShader.setVector3f("dirLight.ambient", 0.5f, 0.5f, 0.5f);
            modelShader.setVector3f("dirLight.diffuse", 0.4f, 0.4f, 0.4f);
            modelShader.setVector3f("dirLight.specular", 0.5f, 0.5f, 0.5f);


            /*
             * point lights
             */


            for (int i = 0; i < pointLightPositions.length; i++) {
                PointLight pointLight = new PointLight(pointLightPositions[i]);
                modelShader.setVector3f("pointLights[" + i + "].position", pointLight.getPosition());
                modelShader.setVector3f("pointLights[" + i + "].ambient", pointLight.getAmbient());
                modelShader.setVector3f("pointLights[" + i + "].diffuse", pointLight.getDiffuse());
                modelShader.setVector3f("pointLights[" + i + "].specular", pointLight.getSpecular());
                modelShader.setFloat("pointLights[" + i + "].constant", pointLight.getConstant());
                modelShader.setFloat("pointLights[" + i + "].linear", pointLight.getLinear());
                modelShader.setFloat("pointLights[" + i + "].quadratic", pointLight.getQuadratic());
            }



            for (int i = 0; i < pointLightPositions1.length; i++) {
                PointLight pointLight1 = new PointLight(pointLightPositions1[i]);
                modelShader.setVector3f("pointLights1[" + i + "].position", pointLight1.getPosition());
                modelShader.setVector3f("pointLights1[" + i + "].ambient", pointLight1.getAmbient());
                modelShader.setVector3f("pointLights1[" + i + "].diffuse", pointLight1.getDiffuse());
                modelShader.setVector3f("pointLights1[" + i + "].specular", pointLight1.getSpecular());
                modelShader.setFloat("pointLights1[" + i + "].constant", pointLight1.getConstant());
                modelShader.setFloat("pointLights1[" + i + "].linear", pointLight1.getLinear());
                modelShader.setFloat("pointLights1[" + i + "].quadratic", pointLight1.getQuadratic());
            }



            for (int i = 0; i < pointLightPositions2.length; i++) {
                PointLight pointLight2 = new PointLight(pointLightPositions[i]);
                modelShader.setVector3f("pointLights2[" + i + "].position", pointLight2.getPosition());
                modelShader.setVector3f("pointLights2[" + i + "].ambient", pointLight2.getAmbient());
                modelShader.setVector3f("pointLights2[" + i + "].diffuse", pointLight2.getDiffuse());
                modelShader.setVector3f("pointLights2[" + i + "].specular", pointLight2.getSpecular());
                modelShader.setFloat("pointLights2[" + i + "].constant", pointLight2.getConstant());
                modelShader.setFloat("pointLights2[" + i + "].linear", pointLight2.getLinear());
                modelShader.setFloat("pointLights2[" + i + "].quadratic", pointLight2.getQuadratic());
            }

            /*
             * conical light
             */

            modelShader.setVector3f("spotLight.position", camera.getPosition());
            modelShader.setVector3f("spotLight.direction", camera.getFront());
            modelShader.setVector3f("spotLight.ambient", 0.0f, 0.0f, 0.0f);
            modelShader.setVector3f("spotLight.diffuse", 1.0f, 1.0f, 1.0f);
            modelShader.setVector3f("spotLight.specular", 1.0f, 1.0f, 1.0f);
            modelShader.setFloat("spotLight.constant", 1.0f);
            modelShader.setFloat("spotLight.linear", 0.09f);
            modelShader.setFloat("spotLight.quadratic", 0.032f);
            modelShader.setFloat("spotLight.cutOff", (float) Math.cos(Math.toRadians(12.5)));
            modelShader.setFloat("spotLight.outerCutOff", (float) Math.cos(Math.toRadians(15)));

            /*
             * bind textures
             */
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, containerDiffuseMap);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, containerSpecularMap);
            /*
             * view/world/projection matrix
             */
            float zoomRad = (float) Math.toRadians((double) camera.getZoom());
            Matrix4f projection = new Matrix4f().perspective(zoomRad, (float) SCR_WIDTH / (float) SCR_HEIGHT, 0.1f, 100f);
            modelShader.setMat4("projection", projection);
            modelShader.setMat4("view", camera.getViewMatrix());
            modelShader.setMat4("model", new Matrix4f());

            glBindVertexArray(cubeVAO);
            double time = glfwGetTime();
            for (int i = 0; i < 10; i++) {
                Matrix4f model = new Matrix4f().translate(cubePositions[i]);
                double angle = 5 * i * time;

                model = model.rotate((float) Math.toRadians(angle), new Vector3f(1.0f, 0.3f, 0.5f));

                //modelShader.setMat4("model", new Matrix4f().translate(0f, 3.4f, 0f).scale(0.15f));
                modelShader.setMat4("model", model);
                glDrawArrays(GL_TRIANGLES, 0, 36);
            }


            /*
             * VASE
             */
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, metal);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, metalSpec);
            glBindVertexArray(vaseVAO);
            modelShader.setMat4("model", new Matrix4f().translate(0f, 3.4f, 0f).scale(0.15f));
            modelShader.setFloat("material.shininess", 5.0f);
            glDrawArrays(GL_TRIANGLES, 0, vase.getTriangleCount() * 3);
//
//
            /*
             * Cube
             */
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, wood5Texture);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, wood5TextureSpec);
            glBindVertexArray(cubeeVAO);
            modelShader.setMat4("model", new Matrix4f().translate(-1.8f, -0.6f, 0f).scale(0.15f));
            modelShader.setFloat("material.shininess", 5.0f);
            glDrawArrays(GL_TRIANGLES, 0, cubee.getTriangleCount() * 3);




            /*
             * teapothigh
             */
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, goldTexture);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, goldTextureSpec);
            glBindVertexArray(teapothighVAO);
            modelShader.setMat4("model", new Matrix4f().translate(0.9f, 0.55f, -1.55f).scale(0.15f));
            modelShader.setFloat("material.shininess", 5.0f);
            glDrawArrays(GL_TRIANGLES, 0, teapothigh.getTriangleCount() * 3);



            /*
             * library
             */
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, wood4Texture);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, wood4TextureSpec);
            glBindVertexArray(libraryVAO);
            modelShader.setMat4("model", new Matrix4f().translate(1.0f, -0.8f, -1.6f).scale(0.015f));
            modelShader.setFloat("material.shininess", 5.0f);
            glDrawArrays(GL_TRIANGLES, 0, library.getTriangleCount() * 3);



            /*
             * earth
             */
            double timee = glfwGetTime();
            double angle = 5*timee;
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, globeTexture);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, globeTextureSpec);

            glBindVertexArray(earthVAO);
            modelShader.setMat4("model", new Matrix4f().translate(-1.8f, -0.36f, 0f).scale(0.2f).rotate((float) Math.toRadians(angle), new Vector3f(0.0f, 1.0f, 0.0f)));
            modelShader.setFloat("material.shininess", 5.0f);
            glDrawArrays(GL_TRIANGLES, 0, earth.getTriangleCount() * 3);





            /*
             * box
             */
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, wood3Texture);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, wood3TextureSpec);
            glBindVertexArray(boxVAO);
            modelShader.setMat4("model", new Matrix4f().translate(1f, -0.7f, 1f).scale(0.005f));
            modelShader.setFloat("material.shininess", 5.0f);
            glDrawArrays(GL_TRIANGLES, 0, box.getTriangleCount() * 3);


            /*
             * VASE
                    */

            double time2 = glfwGetTime();
            double angle2 = 8*time2;
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, crystalTexture);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, crystalTextureSpec);
            glBindVertexArray(vaseVAO);
            modelShader.setMat4("model", new Matrix4f().translate(1f, -0.4f, 1f).scale(0.01f).rotate((float) Math.toRadians(angle2), new Vector3f(0.0f, 1.0f, 0.0f)));
            modelShader.setFloat("material.shininess", 5.0f);
            glDrawArrays(GL_TRIANGLES, 0, vase.getTriangleCount() * 3);

            /*
             * bin
             */
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, meatal1Texture);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, metal1TextureSpec);
            glBindVertexArray(binVAO);
            modelShader.setMat4("model", new Matrix4f().translate(-1f, -0.65f, -1f).scale(0.005f));
            modelShader.setFloat("material.shininess", 5.0f);
            glDrawArrays(GL_TRIANGLES, 0, bin.getTriangleCount() * 3);




            /*
             * FLOOR
             */
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, floor);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, floorSpec);
            glBindVertexArray(quadVAO);
            modelShader.setMat4("model", new Matrix4f().translate(0,-0.7f,0).scale(2f));
            modelShader.setFloat("material.shininess", 5.0f);
            glDrawArrays(GL_TRIANGLES, 0, 6);

            /*
             * HOUSE
             */

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, house);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, houseSpec);
            glBindVertexArray(houseVAO);
            modelShader.setMat4("model", new Matrix4f().translate(housePosition).scale(4f));
            glDrawArrays(GL_TRIANGLES, 0, 36);

            glDisable(GL_CULL_FACE);

            /*
             * parallax
             */
            parallaxShader.use();
            parallaxShader.setMat4("projection", projection);
            parallaxShader.setMat4("view", camera.getViewMatrix());
            parallaxShader.setMat4("model", new Matrix4f().translate(0,-0.8f,0).scale(8f));
            parallaxShader.setVector3f("viewPos", camera.getPosition());
            parallaxShader.setVector3f("lightPos", new Vector3f(1f, 1.0f, 0.3f));
            parallaxShader.setFloat("heightScale", heightScale);


            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, diffuseMap);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, normalMap);
            glActiveTexture(GL_TEXTURE2);
            glBindTexture(GL_TEXTURE_2D, heightMap);

            glBindVertexArray(quadVAO);
            glDrawArrays(GL_TRIANGLES, 0, 6);
            glBindVertexArray(0);

            /*
             * LAMPS
             */

            lampShader.use();

            lampShader.setMat4("projection", projection);
            lampShader.setMat4("view", camera.getViewMatrix());

            glBindVertexArray(lightVAO);

            for (int i = 0; i < 12; i++) {
                Matrix4f model = new Matrix4f().translate(pointLightPositions[i]).scale(0.1f);
                lampShader.setMat4("model", model);
                glDrawArrays(GL_TRIANGLES, 0, 36);
            }


            lampShader1.use();

            lampShader1.setMat4("projection", projection);
            lampShader1.setMat4("view", camera.getViewMatrix());

            glBindVertexArray(lightVAO);

            for (int i = 0; i < 4; i++) {
                Matrix4f model = new Matrix4f().translate(pointLightPositions1[i]).scale(0.1f);
                lampShader1.setMat4("model", model);
                glDrawArrays(GL_TRIANGLES, 0, 36);
            }


            lampShader2.use();

            lampShader2.setMat4("projection", projection);
            lampShader2.setMat4("view", camera.getViewMatrix());

            glBindVertexArray(lightVAO);
            if(light==true)
            for (int i = 0; i < 4; i++) {
                Matrix4f model = new Matrix4f().translate(pointLightPositions2[i]).scale(0.1f);
                lampShader2.setMat4("model", model);
                glDrawArrays(GL_TRIANGLES, 0, 36);
            }

            /*
             * SKYBOX
             */
            skyboxShader.use();
            skyboxShader.setMat4("view", skybox.getViewMatrix(camera.getViewMatrix()));
            skyboxShader.setMat4("projection", projection);

            // skybox cube
            skybox.draw();


            /*
             * windows
             */

            basicShader.use();
            basicShader.setMat4("projection", projection);
           basicShader.setMat4("view", camera.getViewMatrix());
           basicShader.setMat4("model", new Matrix4f());

            glBindVertexArray(transperentVAO);
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, windowTexture);

            Arrays.stream(windowPositions).sorted((winPos1, winPos2) -> {
              float dist1 = VectorUtils.sub(camera.getPosition(), winPos1).length();
              float dist2 = VectorUtils.sub(camera.getPosition(), winPos2).length();
               return Float.compare(dist2, dist1);
            }).forEach(winPos -> {
                Matrix4f model = new Matrix4f().translate(winPos);
               modelShader.setMat4("model", model);
                glDrawArrays(GL_TRIANGLES, 0, 6);
           });

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        glDeleteVertexArrays(cubeVAO);
        glDeleteBuffers(VBO);
        glfwTerminate();
    }

    private long init() {
    /*
     * INIT
     */
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        /*
         * WINDOW
         */
        long window = glfwCreateWindow(SCR_WIDTH, SCR_HEIGHT, "LearnOpenGL", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glfwSetWindowSizeCallback(window, (win, width, height) -> glViewport(0, 0, width, height));
        glfwSetCursorPosCallback(window, (win, xPos, yPos) -> mouseCallback((float) xPos, (float) yPos));
        glfwSetScrollCallback(window, (win, xOff, yOff) -> camera.processMouseScroll((float) yOff));
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        return window;
    }
    //private SoundRunnable soundRunnable = new TickSoundRunnable("/sounds/tick.wav");
    private void processInput(long window) {
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetWindowShouldClose(window, true);
        }
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            camera.processKeyboard(Camera.Movement.FORWARD, deltaTime);
        }
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            camera.processKeyboard(Camera.Movement.BACKWARD, deltaTime);
        }
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            camera.processKeyboard(Camera.Movement.LEFT, deltaTime);
        }
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            camera.processKeyboard(Camera.Movement.RIGHT, deltaTime);
        }
        if (glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS) {

         sound.play("E:\\Project_final_graphic5\\src\\cv1\\ocean.wav");

        }


        if (glfwGetKey(window, GLFW_KEY_T) == GLFW_PRESS) {

            sound.play("E:\\Project_final_graphic5\\src\\cv1\\switch1.wav");
            light=false;
        }

        if (glfwGetKey(window, GLFW_KEY_G) == GLFW_PRESS) {

            sound.play("E:\\Project_final_graphic5\\src\\cv1\\switch.wav");
            light=true;

        }


    }

    private void mouseCallback(float xPos, float yPos) {
        if (firstMouse) {
            lastX = xPos;
            lastY = yPos;
            firstMouse = false;
        }
        float xOffset = xPos - lastX;
        float yOffset = lastY - yPos;

        lastX = xPos;
        lastY = yPos;

        camera.processMouseMovement(xOffset, yOffset);
    }

    private float[] getQuadVertices() {
        Vector3f pos1 = new Vector3f(-1.0f, 0.0f, 1.0f);
        Vector3f pos2 = new Vector3f(-1.0f, 0.0f, -1.0f);
        Vector3f pos3 = new Vector3f(1.0f, 0.0f, -1.0f);
        Vector3f pos4 = new Vector3f(1.0f, 0.0f, 1.0f);

        Vector2f uv1 = new Vector2f(0.0f, 1.0f);
        Vector2f uv2 = new Vector2f(0.0f, 0.0f);
        Vector2f uv3 = new Vector2f(1.0f, 0.0f);
        Vector2f uv4 = new Vector2f(1.0f, 1.0f);

        Vector3f nm = new Vector3f(0.0f, 1.0f, 0.0f);

        Vector3f tangent1 = new Vector3f();
        Vector3f tangent2 = new Vector3f();
        Vector3f bitangent1 = new Vector3f();
        Vector3f bitangent2 = new Vector3f();

        //triangle 1
        Vector3f edge1 = VectorUtils.sub(pos2, pos1);
        Vector3f edge2 = VectorUtils.sub(pos3, pos1);
        Vector2f deltaUV1 = VectorUtils.sub(uv2, uv1);
        Vector2f deltaUV2 = VectorUtils.sub(uv3, uv1);

        float f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y);

        tangent1.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
        tangent1.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
        tangent1.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
        tangent1 = tangent1.normalize();

        bitangent1.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
        bitangent1.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
        bitangent1.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
        bitangent1 = bitangent1.normalize();

        // triangle 2
        edge1 = VectorUtils.sub(pos3, pos1);
        edge2 = VectorUtils.sub(pos4, pos1);
        deltaUV1 = VectorUtils.sub(uv3, uv1);
        deltaUV2 = VectorUtils.sub(uv4, uv1);

        f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y);

        tangent2.x = f * (deltaUV2.y * edge1.x - deltaUV1.y * edge2.x);
        tangent2.y = f * (deltaUV2.y * edge1.y - deltaUV1.y * edge2.y);
        tangent2.z = f * (deltaUV2.y * edge1.z - deltaUV1.y * edge2.z);
        tangent2 = tangent2.normalize();


        bitangent2.x = f * (-deltaUV2.x * edge1.x + deltaUV1.x * edge2.x);
        bitangent2.y = f * (-deltaUV2.x * edge1.y + deltaUV1.x * edge2.y);
        bitangent2.z = f * (-deltaUV2.x * edge1.z + deltaUV1.x * edge2.z);
        bitangent2 = bitangent2.normalize();
        return new float[]{
// positions            // normal         // texcoords  // tangent                          // bitangent
                pos1.x, pos1.y, pos1.z, nm.x, nm.y, nm.z, uv1.x, uv1.y, tangent1.x, tangent1.y, tangent1.z, bitangent1.x, bitangent1.y, bitangent1.z,
                pos2.x, pos2.y, pos2.z, nm.x, nm.y, nm.z, uv2.x, uv2.y, tangent1.x, tangent1.y, tangent1.z, bitangent1.x, bitangent1.y, bitangent1.z,
                pos3.x, pos3.y, pos3.z, nm.x, nm.y, nm.z, uv3.x, uv3.y, tangent1.x, tangent1.y, tangent1.z, bitangent1.x, bitangent1.y, bitangent1.z,

                pos1.x, pos1.y, pos1.z, nm.x, nm.y, nm.z, uv1.x, uv1.y, tangent2.x, tangent2.y, tangent2.z, bitangent2.x, bitangent2.y, bitangent2.z,
                pos3.x, pos3.y, pos3.z, nm.x, nm.y, nm.z, uv3.x, uv3.y, tangent2.x, tangent2.y, tangent2.z, bitangent2.x, bitangent2.y, bitangent2.z,
                pos4.x, pos4.y, pos4.z, nm.x, nm.y, nm.z, uv4.x, uv4.y, tangent2.x, tangent2.y, tangent2.z, bitangent2.x, bitangent2.y, bitangent2.z
        };
    }
}
