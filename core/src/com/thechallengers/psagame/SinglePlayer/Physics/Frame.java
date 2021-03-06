package com.thechallengers.psagame.SinglePlayer.Physics;

        import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
        import com.badlogic.gdx.math.Polygon;
        import com.badlogic.gdx.math.Vector2;
        import com.badlogic.gdx.physics.box2d.Body;
        import com.badlogic.gdx.physics.box2d.BodyDef;
        import com.badlogic.gdx.physics.box2d.FixtureDef;
        import com.badlogic.gdx.physics.box2d.PolygonShape;
        import com.badlogic.gdx.physics.box2d.Shape;
        import com.badlogic.gdx.physics.box2d.World;
        import com.badlogic.gdx.utils.Array;
        import com.thechallengers.psagame.SinglePlayer.SinglePlayerGameWorld;
        import com.thechallengers.psagame.helpers.AssetLoader;

        import java.util.HashMap;
import com.badlogic.gdx.math.Intersector;

/**
 * Created by Asus on 9/29/2017.
 */

public class Frame {
    public boolean[][] frame;
    public int size;
//    public Pattern pattern;
    public Polygon pattern;
    public Float patternArea;

    public Frame(int size) {
        this.size = size;
//        this.pattern = new Pattern(size);
        this.frame = new boolean[size][size];
    }

//    public void setPattern(Pattern pattern) {
////        this.pattern = pattern;
//        this.frame = pattern.toMatrix();
//    }
//
//    public void calculatePercentage(Array<Body> bodies, float start_x, float start_y) {
//        float sum = 0;
//        int cnt = 0;
//        for(int i = 0; i < size; i ++) {
//            for(int j = 0; j < size; j ++) {
//                if(frame[i][j]) {
//                    float width = AssetLoader.unitBlockTexture.getWidth();
//                    float height = AssetLoader.unitBlockTexture.getHeight();
//                    float x_coord = start_x + i * AssetLoader.unitBlockTexture.getWidth() + width / 2;
//                    float y_coord = start_y + i * AssetLoader.unitBlockTexture.getHeight() + height / 2;
//                    sum += singleUnitPercent(bodies, x_coord, y_coord,
//                            width,
//                            height);
//                    cnt ++;
//                }
//            }
//        }
//        System.out.println("percentage");
//        System.out.println("Count " + cnt);
//        System.out.println(sum / cnt    );
//    }

//    public float singleUnitPercent(Array<Body> bodies, float x_coord, float y_coord, float width, float height) {
//        for(Body body: bodies) {
//            if(body.getUserData() instanceof Block) {
//                float temp = overlapPercent(x_coord, y_coord, body.getPosition().x * 100f, body.getPosition().y * 100f,
//                        width, height,
//                        ((Block) body.getUserData()).width * 100f,
//                        ((Block) body.getUserData()).height * 100f);
//                if (temp > 0) return 1.0f;
//            }
//        }
////        System.out.println("res");
////        System.out.println(res);
//        return 0.0f;
//    }

//    public float overlapPercent(float x_a, float y_a, float x_b, float y_b,
//                                float width_a, float height_a, float width_b, float height_b) {
//        float a_left = x_a - width_a / 2;
//        float a_right = x_a + width_a / 2;
//        float a_bot = y_a - height_a / 2;
//        float a_top = y_a + height_a / 2;
//        float b_left = x_b - width_b / 2;
//        float b_right = x_b + width_b / 2;
//        float b_bot = y_b - height_b / 2;
//        float b_top = y_b + height_b / 2;
//        float x_overlap = Math.max(0, Math.min(a_right, b_right) - Math.max(a_left, b_left));
//        float y_overlap = Math.max(0, Math.min(a_top, b_top) - Math.max(a_bot, b_bot));
//        float overlapArea = x_overlap * y_overlap;
//        float unitArea = width_a * height_a;
//        return overlapArea / unitArea;
//    }

//    public void test() {
//        PolygonShape pat = new PolygonShape();
//    }

    public void getVertices(PolygonShape s) {
        Array<Vector2> verts = new Array<Vector2>();
        Vector2 tmp = new Vector2();
        for (int i = 0; i < s.getVertexCount(); i++) {
            // fill tmp with the vertex
            s.getVertex(i, tmp);
            verts.add(new Vector2(tmp));
        }

    }
}

