package com.dmclaughlin.scalidation

class Failures(private var failures: Map[String,List[String]] = Map.empty[String,List[String]]) {
  def isEmpty:Boolean = failures.size == 0
  def set(fieldName: String, msg: String):Unit = {
    if(failures.contains(fieldName)) {
      // want to preserve order so we append here
      failures = failures.updated(fieldName, failures(fieldName) ::: List(msg))
    } else 
      failures += (fieldName -> List(msg))
  }
  def get(fieldName: String):Option[List[String]] = 
    failures.get(fieldName)

  def getFields:List[String] = 
    failures.keys.toList
}

object Failures {
  def apply() = new Failures
  def apply(fails: Map[String,List[String]]) = new Failures(fails)
}
