package com.dmclaughlin.scalidation

import scala.collection.mutable.Map

class Failures {
  val failures = Map.empty[String,List[String]]
  def isEmpty:Boolean = failures.size == 0
  def set(fieldName: String, msg: String):Unit = {
    if(failures.contains(fieldName)) {
      // want to preserve order so we append here..e
      failures(fieldName) = failures(fieldName) ::: List(msg)
    } else 
      failures(fieldName) = List(msg)
  }
  def get(fieldName: String):Option[List[String]] = 
    failures.get(fieldName)

  def getFields:List[String] = 
    failures.keys.toList
}
