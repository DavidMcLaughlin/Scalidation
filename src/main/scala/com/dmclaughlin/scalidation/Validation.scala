package com.dmclaughlin.scalidation

trait Validation {
  private val fails = new Failures

  def failures:Option[Failures] = {
    if(fails.isEmpty) None else Some(fails)
  }

  def validate:Option[Failures]

  def check[T](fieldName: String, validator: T => Boolean, msg: String = "Error on field."):Boolean = {
    if(validator(getV(fieldName).asInstanceOf[T]))
      true
    else {
      fails.set(fieldName, msg)
      false
    }    
  }

  private def getV(name: String): Any = this.getClass.getMethods.find(_.getName == name).get.invoke(this)
}
