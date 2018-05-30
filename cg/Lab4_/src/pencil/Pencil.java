package pencil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.*;
import javax.swing.Timer;
import javax.vecmath.*;


/**
 * Created by Nadine on 14.04.2018.
 */
public class Pencil implements ActionListener {

    private float upperEyeLimit = 5.0f; // 5.0
    private float lowerEyeLimit = 1.0f; // 1.0
    private float farthestEyeLimit = 6.0f; // 6.0
    private float nearestEyeLimit = 3.0f; // 3.0

    private TransformGroup viewingTransformGroup;
    private TransformGroup treeTransformGroup;
    private Transform3D treeTransform3D = new Transform3D();
    private Transform3D viewingTransform = new Transform3D();

    private float angle = 0;
    private float eyeHeight;
    private float eyeDistance;
    private boolean descend = true;
    private boolean approaching = true;

    public static void main(String[] args) {
        new Pencil();
    }

    private Pencil() {
        Timer timer = new Timer(50, this);
        SimpleUniverse universe = new SimpleUniverse();

        viewingTransformGroup = universe.getViewingPlatform().getViewPlatformTransform();
        universe.addBranchGraph(createSceneGraph());

        eyeHeight = upperEyeLimit;
        eyeDistance = farthestEyeLimit;
        timer.start();
    }

    private BranchGroup createSceneGraph() {
        BranchGroup objRoot = new BranchGroup();
        // створюємо об'єкт, що будемо додавати до групи
        treeTransformGroup = new TransformGroup();
        treeTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        buildPencilSkeleton();
        objRoot.addChild(treeTransformGroup);

        Background background = new Background(new Color3f(Color.BLUE));
        BoundingSphere sphere = new BoundingSphere(new Point3d(0,0,0), 100);
        background.setApplicationBounds(sphere);
        objRoot.addChild(background);

        // налаштовуємо освітлення
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),100.0);
        Color3f light1Color = new Color3f(0.8f, 1.1f, 0.1f);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        objRoot.addChild(light1);

        // встановлюємо навколишнє освітлення
        Color3f ambientColor = new Color3f(1.0f, 1.0f, 1.0f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLightNode);
        return objRoot;

    }

    private void buildPencilSkeleton() {
        Cylinder pencilRubber = PensilBody.getPencilRubber(0.2f);
        Transform3D pencilRubberT = new Transform3D();
        pencilRubberT.setTranslation(new Vector3f());
        pencilRubberT.setRotation(new AxisAngle4d(1, 0, 0, Math.toRadians(90)));
        TransformGroup pencilRubberTG = new TransformGroup();
        pencilRubberTG.setTransform(pencilRubberT);
        pencilRubberTG.addChild(pencilRubber);
        treeTransformGroup.addChild(pencilRubberTG);

        setCylinders();
    }

    private void setCylinders() {
        TransformGroup cylinderTower1 = PensilBody.getCylinders(1.5f, 0.0f, 0.0f);
        treeTransformGroup.addChild(cylinderTower1);

    }


    // ActionListener interface
    @Override
    public void actionPerformed(ActionEvent e) {
        float delta = 0.03f;

        // rotation of the castle
        treeTransform3D.rotZ(angle);
        treeTransformGroup.setTransform(treeTransform3D);
        angle += delta;

        // change of the camera position up and down within defined limits
        if (eyeHeight > upperEyeLimit){
            descend = true;
        }else if(eyeHeight < lowerEyeLimit){
            descend = false;
        }
        if (descend){
            eyeHeight -= delta;
        }else{
            eyeHeight += delta;
        }

        // change camera distance to the scene
        if (eyeDistance > farthestEyeLimit){
            approaching = true;
        }else if(eyeDistance < nearestEyeLimit){
            approaching = false;
        }
        if (approaching){
            eyeDistance -= delta;
        }else{
            eyeDistance += delta;
        }

        Point3d eye = new Point3d(eyeDistance, eyeDistance, eyeHeight); // spectator's eye
        Point3d center = new Point3d(.0f, .0f ,0.5f); // sight target
        Vector3d up = new Vector3d(.0f, .0f, 1.0f);; // the camera frustum
        viewingTransform.lookAt(eye, center, up);
        viewingTransform.invert();
        viewingTransformGroup.setTransform(viewingTransform);
    }

}
