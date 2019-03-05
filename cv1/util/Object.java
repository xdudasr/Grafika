package cv1.util;

import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Object {

    private String path;

    private List<float[]> vertices;
    private List<float[]> normals;
    private List<float[]> texcoords;
    private List<int[]> vertexIndices;
    private List<int[]> normalIndices;
    private List<int[]> texcoordIndices;

    public Object(String path) {
        this.path = path;
        try {
            this.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() throws IOException {
        /* Mesh containing the loaded object */
        vertices = new ArrayList<>();
        normals = new ArrayList<>();
        texcoords = new ArrayList<>();
        vertexIndices = new ArrayList<>();
        normalIndices = new ArrayList<>();
        texcoordIndices = new ArrayList<>();
        
        InputStream is = Object.class.getResourceAsStream(path);
        if (is == null) {
            throw new IOException("File not found " + path);
        }
        
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
            
            String line;
            while ((line = in.readLine()) != null) {
                
                if (line.startsWith("v ")) {
                    
                    String[] vertStr = line.split("\\s+");
                    float[] vertex = new float[3];
                            
                    vertex[0] = Float.parseFloat(vertStr[1]);
                    vertex[1] = Float.parseFloat(vertStr[2]);
                    vertex[2] = Float.parseFloat(vertStr[3]);
                    vertices.add(vertex);
                    
                } else if (line.startsWith("vn ")) {
                    
                    String[] normStr = line.split("\\s+");
                    float[] normal = new float[3];
                    
                    normal[0] = Float.parseFloat(normStr[1]);
                    normal[1] = Float.parseFloat(normStr[2]);
                    normal[2] = Float.parseFloat(normStr[3]);
                    normals.add(normal);
                    
                } else if (line.startsWith("vt ")) {
                    
                    String[] texcoordStr = line.split("\\s+");
                    float[] texcoord = new float[2];
                    
                    texcoord[0] = Float.parseFloat(texcoordStr[1]);
                    texcoord[1] = Float.parseFloat(texcoordStr[2]);
                    texcoords.add(texcoord);
                    
                } else if (line.startsWith("f ")) {
                    
                    String[] faceStr = line.split("\\s+");
                    int[] faceVert = new int[3];

                    faceVert[0] = Integer.parseInt(faceStr[1].split("/")[0]) - 1;
                    faceVert[1] = Integer.parseInt(faceStr[2].split("/")[0]) - 1;
                    faceVert[2] = Integer.parseInt(faceStr[3].split("/")[0]) - 1;
                    vertexIndices.add(faceVert);
                    
                    int[] faceTexcoord = new int[3];
                    faceTexcoord[0] = Integer.parseInt(faceStr[1].split("/")[1]) - 1;
                    faceTexcoord[1] = Integer.parseInt(faceStr[2].split("/")[1]) - 1;
                    faceTexcoord[2] = Integer.parseInt(faceStr[3].split("/")[1]) - 1;
                    texcoordIndices.add(faceTexcoord);
                    
                    if (faceStr[1].split("/").length >= 3) {
                        int[] faceNorm = new int[3];

                        faceNorm[0] = Integer.parseInt(faceStr[1].split("/")[2]) - 1;
                        faceNorm[1] = Integer.parseInt(faceStr[2].split("/")[2]) - 1;
                        faceNorm[2] = Integer.parseInt(faceStr[3].split("/")[2]) - 1;
                        normalIndices.add(faceNorm);
                    }
                }
            }
        }
    }

    public List<float[]> getVertices() {
        return vertices;
    }

    public List<float[]> getNormals() {
        return normals;
    }
    
    public List<float[]> getTexcoords() {
        return texcoords;
    }

    public List<int[]> getVertexIndices() {
        return vertexIndices;
    }

    public List<int[]> getNormalIndices() {
        return normalIndices;
    }
    
    public List<int[]> getTexcoordIndices() {
        return texcoordIndices;
    }

    public FloatBuffer getData() {
        int length = 3 * 8 * getTriangleCount();
        FloatBuffer bb = BufferUtils.createFloatBuffer(length);

        for (int f = 0; f < getTriangleCount(); f++) {
            int[] pi = getVertexIndices().get(f);
            int[] ni = getNormalIndices().get(f);
            int[] ti = getTexcoordIndices().get(f);
            for (int i = 0; i < 3; i++) {
                float[] position = getVertices().get(pi[i]);
                float[] normal = getNormals().get(ni[i]);
                float[] texcoord = getTexcoords().get(ti[i]);

                bb.put(position);
                bb.put(normal);
                bb.put(texcoord);
            }
        }
        bb.rewind();
        return bb;
    }

    public int getTriangleCount() {
        return vertexIndices.size();
    }
    
}
