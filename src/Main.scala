import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape._
import javafx.scene.transform.{Rotate, Translate}
import javafx.scene.{Group, Node}
import javafx.stage.Stage
import javafx.geometry.Pos
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.{PerspectiveCamera, Scene, SceneAntialiasing, SubScene}
import scala.io.Source

class Main extends Application {

  //Auxiliary types
  type Point = (Double, Double, Double)
  type Size = Double
  type Placement = (Point, Size) //1st point: origin, 2nd point: size

  //Shape3D is an abstract class that extends javafx.scene.Node
  //Box and Cylinder are subclasses of Shape3D
  type Section = (Placement, List[Node])  //example: ( ((0.0,0.0,0.0), 2.0), List(new Cylinder(0.5, 1, 10)))


  /*
    Additional information about JavaFX basic concepts (e.g. Stage, Scene) will be provided in week7
   */
  override def start(stage: Stage): Unit = {

    //Get and print program arguments (args: Array[String])
    val params = getParameters
    println("Program arguments:" + params.getRaw)

    //Materials to be applied to the 3D objects
    val redMaterial = new PhongMaterial()
    redMaterial.setDiffuseColor(Color.rgb(150,0,0))

    val greenMaterial = new PhongMaterial()
    greenMaterial.setDiffuseColor(Color.rgb(0,255,0))

    val blueMaterial = new PhongMaterial()
    blueMaterial.setDiffuseColor(Color.rgb(0,0,150))

    //3D objects
    val lineX = new Line(0, 0, 200, 0)
    lineX.setStroke(Color.GREEN)

    val lineY = new Line(0, 0, 0, 200)
    lineY.setStroke(Color.YELLOW)

    val lineZ = new Line(0, 0, 200, 0)
    lineZ.setStroke(Color.LIGHTSALMON)
    lineZ.getTransforms().add(new Rotate(-90, 0, 0, 0, Rotate.Y_AXIS))

    val camVolume = new Cylinder(10, 50, 10)
    camVolume.setTranslateX(1)
    camVolume.getTransforms().add(new Rotate(45, 0, 0, 0, Rotate.X_AXIS))
    camVolume.setMaterial(blueMaterial)
    camVolume.setDrawMode(DrawMode.LINE)

    val wiredBox = new Box(32, 32, 32)
    wiredBox.setTranslateX(16)
    wiredBox.setTranslateY(16)
    wiredBox.setTranslateZ(16)
    wiredBox.setMaterial(redMaterial)
    wiredBox.setDrawMode(DrawMode.LINE)

    val cylinder1 = new Cylinder(0.5, 1, 10)
    cylinder1.setTranslateX(2)
    cylinder1.setTranslateY(2)
    cylinder1.setTranslateZ(2)
    cylinder1.setScaleX(2)
    cylinder1.setScaleY(2)
    cylinder1.setScaleZ(2)
    cylinder1.setMaterial(greenMaterial)

    val box1 = new Box(1, 1, 1)  //
    box1.setTranslateX(5)
    box1.setTranslateY(5)
    box1.setTranslateZ(5)
    box1.setMaterial(greenMaterial)



    // 3D objects (group of nodes - javafx.scene.Node) that will be provide to the subScene
    val worldRoot:Group = new Group(wiredBox, camVolume, lineX, lineY, lineZ, cylinder1, box1)

    // Camera
    val camera = new PerspectiveCamera(true)

    val cameraTransform = new CameraTransformer
    cameraTransform.setTranslate(0, 0, 0)
    cameraTransform.getChildren.add(camera)
    camera.setNearClip(0.1)
    camera.setFarClip(10000.0)

    camera.setTranslateZ(-500)
    camera.setFieldOfView(20)
    cameraTransform.ry.setAngle(-45.0)
    cameraTransform.rx.setAngle(-45.0)
    worldRoot.getChildren.add(cameraTransform)

    // SubScene - composed by the nodes present in the worldRoot
    val subScene = new SubScene(worldRoot, 800, 600, true, SceneAntialiasing.BALANCED)
    subScene.setFill(Color.DARKSLATEGRAY)
    subScene.setCamera(camera)

    // CameraView - an additional perspective of the environment
    val cameraView = new CameraView(subScene)
    cameraView.setFirstPersonNavigationEabled(true)
    cameraView.setFitWidth(350)
    cameraView.setFitHeight(225)
    cameraView.getRx.setAngle(-45)
    cameraView.getT.setZ(-100)
    cameraView.getT.setY(-500)
    cameraView.getCamera.setTranslateZ(-50)
    cameraView.startViewing



      // Position of the CameraView: Right-bottom corner
      StackPane.setAlignment(cameraView, Pos.BOTTOM_RIGHT)
      StackPane.setMargin(cameraView, new Insets(5))

    // Scene - defines what is rendered (in this case the subScene and the cameraView)
    val root = new StackPane(subScene, cameraView)
    subScene.widthProperty.bind(root.widthProperty)
    subScene.heightProperty.bind(root.heightProperty)

    val scene = new Scene(root, 810, 610, true, SceneAntialiasing.BALANCED)




    //setup and start the Stage
    stage.setTitle("PPM Project 21/22")
    stage.setScene(scene)
    stage.show

    //T1
    val graphicalModels = List[(String, Color, Int, Int, Int, Float, Float, Float)]()
    //val graphicalModels = List[String]()
    val cylinder2 = new Cylinder(0.5, 1, 10)

    def readFromFileToList (file: String) = {
      val bufferedSource = Source.fromFile(file)
      for (line <- bufferedSource.getLines){
        val str = line.toUpperCase.split(" ")
        str(0) match {
          case "CYLINDER" =>
            cylinder2.setTranslateX(str(2).toInt)
            cylinder2.setTranslateY(str(3).toInt)
            cylinder2.setTranslateZ(str(4).toInt)
            cylinder2.setScaleX(str(5).toFloat)
            cylinder2.setScaleY(str(6).toFloat)
            cylinder2.setScaleZ(str(7).toFloat)
            cylinder2.setMaterial(redMaterial)
          case "BOX" =>
            val b2 = new Box(1,1,1)
            b2.setTranslateX(str(2).toInt)
            b2.setTranslateY(str(3).toInt)
            b2.setTranslateZ(str(4).toInt)
            b2.setMaterial(redMaterial)
        }
      }
      bufferedSource.close
    }

    readFromFileToList("C:/Users/Paulo Ara??jo/IdeaProjects/Base_Project2Share/src/graphical_model.txt")
    println(cylinder2.getTranslateX)


    //oct1 - example of an Octree[Placement] that contains only one Node (i.e. cylinder1)
    //In case of difficulties to implement task T2 this octree can be used as input for tasks T3, T4 and T5

    val placement1: Placement = ((0, 0, 0), 8.0)
    val sec1: Section = (((0.0,0.0,0.0), 4.0), List(cylinder1.asInstanceOf[Node]))
    val ocLeaf1 = OcLeaf(sec1)
    val oct1:Octree[Placement] = OcNode[Placement](placement1, ocLeaf1, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)



    //example of bounding boxes (corresponding to the octree oct1) added manually to the world
    val b2 = new Box(8,8,8)
    //translate because it is added by defaut to the coords (0,0,0)
    b2.setTranslateX(8/2)
    b2.setTranslateY(8/2)
    b2.setTranslateZ(8/2)
    b2.setMaterial(redMaterial)
    b2.setDrawMode(DrawMode.LINE)

    val b3 = new Box(4,4,4)
    //translate because it is added by defaut to the coords (0,0,0)
    b3.setTranslateX(4/2)
    b3.setTranslateY(4/2)
    b3.setTranslateZ(4/2)
    b3.setMaterial(redMaterial)
    b3.setDrawMode(DrawMode.LINE)

    //adding boxes b2 and b3 to the world
    worldRoot.getChildren.add(b2)
    worldRoot.getChildren.add(b3)

    //T3
  def isContained(n1:Node):Boolean = {
    n1.getBoundsInParent().intersects(camVolume.getLayoutBounds()) || camVolume.getLayoutBounds().contains(n1.getBoundsInParent())
  }


  def changeColor (tree:Octree[Placement]):Octree[Placement] = tree match {
    case OcEmpty => tree
    case OcLeaf(s:Section) => if (isContained(s._2(0))) OcLeaf(s._2.head.asInstanceOf[Shape3D].setMaterial(redMaterial)) else OcLeaf(s)
    case OcNode(coords: Placement, up_00, up_01, up_10, up_11, down_00, down_01, down_10, down_11) =>
      OcNode(coords, changeColor(up_00), changeColor(up_01), changeColor(up_10), changeColor(up_11),
                     changeColor(down_00), changeColor(down_01), changeColor(down_10), changeColor(down_11))
  }


/*
  def changeColor (tree:Octree[Any], f:Section => Section):Octree[Any] = {
    def aux(tree:Octree[A]): Octree[A] = tree match {
      case OcEmpty => tree
      case OcLeaf(s:Section) => OcLeaf(f(s))
      case OcNode(coords: A, up_00, up_01, up_10, up_11, down_00, down_01, down_10, down_11) =>
        OcNode(coords, aux(up_00), aux(up_01), aux(up_10), aux(up_11), aux(down_00), aux(down_01), aux(down_10), aux(down_11))
    }
  }
*/

    //Mouse left click interaction
    scene.setOnMouseClicked((event) => {
      camVolume.setTranslateX(camVolume.getTranslateX + 2)
      worldRoot.getChildren.removeAll()
      changeColor(oct1)
    })

  }



  override def init(): Unit = {
    println("init")
  }

  override def stop(): Unit = {
    println("stopped")
  }

}

object FxApp {

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main], args: _*)

  }
}

