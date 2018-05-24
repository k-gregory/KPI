package cglabs.lab6;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.vecmath.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Hashtable;

public class BouncingBall extends Applet implements KeyListener {

    static TextureLoader loader = new TextureLoader("model/texture.jpg",
            "RGP", new Container());
    static Texture texture = loader.getTexture();

    private SimpleUniverse universe = null;
    private Canvas3D canvas = null;
    private TransformGroup viewtrans = null;

    private TransformGroup tg = null;
    private Transform3D t3d = null;
    private Transform3D t3dstep = new Transform3D();
    private Matrix4d matrix = new Matrix4d();

    private Shape3D[] legs = new Shape3D[6];

    public BouncingBall() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse
                .getPreferredConfiguration();

        canvas = new Canvas3D(config);
        add("Center", canvas);
        universe = new SimpleUniverse(canvas);

        BranchGroup scene = createSceneGraph();
        universe.getViewingPlatform().setNominalViewingTransform();

        universe.getViewer().getView().setBackClipDistance(100.0);

        canvas.addKeyListener(this);

        universe.addBranchGraph(scene);
    }

    public static void main(String[] args) {
        BouncingBall applet = new BouncingBall();
        Frame frame = new MainFrame(applet, 800, 600);
    }

    private BranchGroup createSceneGraph() {
        BranchGroup objRoot = new BranchGroup();

        BoundingSphere bounds = new BoundingSphere(new Point3d(), 10000.0);

        viewtrans = universe.getViewingPlatform().getViewPlatformTransform();

        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(viewtrans);
        keyNavBeh.setSchedulingBounds(bounds);
        PlatformGeometry platformGeom = new PlatformGeometry();
        platformGeom.addChild(keyNavBeh);
        universe.getViewingPlatform().setPlatformGeometry(platformGeom);

        objRoot.addChild(createLadybird());

        Background background = new Background();
        background.setColor(0.75f, 0.69f, 0.680f);
        background.setApplicationBounds(bounds);
        objRoot.addChild(background);

        return objRoot;
    }

    private BranchGroup createLadybird() {

        BranchGroup objRoot = new BranchGroup();
        tg = new TransformGroup();
        t3d = new Transform3D();

        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        t3d.setTranslation(new Vector3d(-0.15, -0.3, -5.0));
        t3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
        t3d.setScale(1.0);

        tg.setTransform(t3d);

        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        Appearance ap = new Appearance();
        ap.setTexture(texture);
        ap.setTextureAttributes(texAttr);
        int primflags = Primitive.GENERATE_NORMALS
                + Primitive.GENERATE_TEXTURE_COORDS;
        ObjectFile loader = new ObjectFile(ObjectFile.RESIZE);


        Scene s = null;

        File file = new java.io.File("model/ladybug.obj");

        try {
            s = loader.load(file.toURI().toURL());
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }

        Hashtable namedObjects = s.getNamedObjects();
        for (int i = 1; i <= 6; i++) {
            legs[i - 1] = (Shape3D) namedObjects.get("leg" + i);
        }
        Shape3D ladybug = (Shape3D) namedObjects.get("ladybug");
        ladybug.setAppearance(ap);

        /*ROTATE LEGS*/
        Transform3D hourArrowRotationAxis = new Transform3D();
        hourArrowRotationAxis.rotZ(Math.PI / 2);

        for (int i = 0; i < 6; i++) {
            TransformGroup tgmHourArrow = new TransformGroup();
            Shape3D hour_arrow = legs[i];
            tgmHourArrow.addChild(hour_arrow.cloneTree());
            legs[i].removeAllGeometries();

            int timeStart = 0; //стрілка почне рух через 2 секунди після запуску програми
            int noRotationsHour = 2000; //кількість обертів
            int timeRotationHour = 1000;//час одного оберту
//Alpha для руху годинної стрілки
            Alpha hourRotationAlpha = new Alpha(noRotationsHour,
                    Alpha.INCREASING_ENABLE,
                    timeStart,
                    0, timeRotationHour, 0, 0, 0, 0, 0);
//обертання годинної стрілки
            RotationInterpolator hourArrRotation = new RotationInterpolator(
                    hourRotationAlpha, tgmHourArrow,
                    hourArrowRotationAxis, (float) Math.PI * 2, 0.0f);
            BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);
            hourArrRotation.setSchedulingBounds(bounds);
            tgmHourArrow.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            tgmHourArrow.addChild(hourArrRotation);

            tg.addChild(tgmHourArrow);
        }
        /*Rotate legs end*/

        tg.addChild(s.getSceneGroup());

        objRoot.addChild(tg);
        objRoot.addChild(createLight());

        objRoot.compile();

        return objRoot;

    }

    private Light createLight() {
        DirectionalLight light = new DirectionalLight(true, new Color3f(1.0f,
                1.0f, 1.0f), new Vector3f(-0.3f, 0.2f, -1.0f));

        light.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));

        return light;
    }

    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();

        if (key == 'd') {
            t3dstep.set(new Vector3d(0.0, 0.0, 0.1));
            tg.getTransform(t3d);
            t3d.mul(t3dstep);
            tg.setTransform(t3d);
        }

        if (key == 's') {

            t3dstep.rotY(Math.PI / 32);
            tg.getTransform(t3d);
            t3d.get(matrix);
            t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            t3d.mul(t3dstep);
            t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            tg.setTransform(t3d);

        }

        if (key == 'f') {

            t3dstep.rotY(-Math.PI / 32);
            tg.getTransform(t3d);
            t3d.get(matrix);
            t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            t3d.mul(t3dstep);
            t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            tg.setTransform(t3d);

        }

        if (key == 'r') {

            t3dstep.rotX(Math.PI / 32);
            tg.getTransform(t3d);
            t3d.get(matrix);
            t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            t3d.mul(t3dstep);
            t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            tg.setTransform(t3d);

        }

        if (key == 'v') {

            t3dstep.rotX(-Math.PI / 32);
            tg.getTransform(t3d);
            t3d.get(matrix);
            t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            t3d.mul(t3dstep);
            t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            tg.setTransform(t3d);

        }

        if (key == 'e') {
            t3dstep.set(new Vector3d(0.0, 0.1, 0.0));
            tg.getTransform(t3d);
            t3d.mul(t3dstep);
            tg.setTransform(t3d);
        }

        if (key == 'c') {
            t3dstep.set(new Vector3d(0.0, -0.1, 0.0));
            tg.getTransform(t3d);
            t3d.mul(t3dstep);
            tg.setTransform(t3d);
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }
}