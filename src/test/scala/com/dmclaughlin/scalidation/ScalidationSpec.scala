package com.dmclaughlin.scalidation

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class ScalidationSpec extends FlatSpec with ShouldMatchers {

  "A validator" should "have pass validation" in {
    class Test(val name: String) extends Validation {
      override def validate:Option[Failures] = {
        check("name", { s:String =>  s != null && s.trim.length > 0 }, "Name must have a value.")
        failures
      }
    }

    (new Test("David").validate match {
      case Some(e) => false
      case _ => true
    }) should equal (true)
  }

  "A validator" should "catch failures" in {
    class Test(val name: String) extends Validation {
      override def validate:Option[Failures] = {
        check("name", { s: String => s != null && s.trim.length > 0 }, "Name must have a value.")
        failures
      }
    }

    (new Test(" ").validate match {
      case Some(e) => e.get("name").get.head
      case _ => "fail"
    }) should equal ("Name must have a value.")
  }

  "A validator" should "handle multiple fields of multiple types" in { 
    class Test(val name: String, val age: Int) extends Validation {
      override def validate = {
        check("name", { s: String => s != null && s.trim.length > 0 }, "Name must have a value.")
        check("age", { a: Int => a > 0 }, "Age must be positive.")
        failures
      }
    }

    (new Test("David",26).validate match {
      case Some(e) => false
      case _ => true
    }) should equal (true)

    val failures = new Test("David", -5).validate match {
      case Some(e) => e
      case _ => throw new Exception("Something went wrong")
    }

    failures.getFields should equal (List("age"))
    failures.get("age").get.head should equal ("Age must be positive.")
  }

  "A validator" should "support multiple errors for one field and preserve error msg order" in {
    
    class Test(val email: String) extends Validation {
      override def validate = {
        val emailRe = """(\w+@\w+)""".r
        check("email", { s: String => s != null && s.trim.length > 0}, "Email must have a value.")
        check("email", { s: String => s match { case emailRe(s) => true; case _ => false } }, "Invalid format for email.")
        failures
      }
    }

    val failures = (new Test(" ").validate match {
      case Some(e) => e
      case _ => throw new Exception("Test failed.")
    })
    
    failures.getFields should equal (List("email"))
    failures.get("email").get should equal (List("Email must have a value.", "Invalid format for email."))
  }
}
