import sbt._

class ScalidationProject(info: ProjectInfo) extends DefaultProject(info) {
  override def artifactID = "scalidation"
  val scalatest = "org.scalatest" % "scalatest" % "1.3" % "compile"
}
