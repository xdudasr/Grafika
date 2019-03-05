package cv1;

import cv1.util.VectorUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Floor {
    private float[] vertices = getQuadVertices();
    private int VAO;
    private int VBO;

    private float[] getQuadVertices() {
        Vector3f pos1 = new Vector3f(-1.0f, 1.0f, 0.0f);
        Vector3f pos2 = new Vector3f(-1.0f, -1.0f, 0.0f);
        Vector3f pos3 = new Vector3f(1.0f, -1.0f, 0.0f);
        Vector3f pos4 = new Vector3f(1.0f, 1.0f, 0.0f);

        Vector2f uv1 = new Vector2f(0.0f, 1.0f);
        Vector2f uv2 = new Vector2f(0.0f, 0.0f);
        Vector2f uv3 = new Vector2f(1.0f, 0.0f);
        Vector2f uv4 = new Vector2f(1.0f, 1.0f);

        Vector3f nm = new Vector3f(0.0f, 0.0f, 1.0f);

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
