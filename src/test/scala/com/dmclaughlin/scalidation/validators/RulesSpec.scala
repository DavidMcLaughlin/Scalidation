package com.dmclaughlin.scalidation
package validators

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class RulesSpec extends FlatSpec with ShouldMatchers {

  "hasValue StringRule" should "catch empty strings" in {
    class Test(val name: String) extends Validation {
      override def validate = {
        check("name", StringRules.hasValue, "Name must have a value.")
        failures
      }
    }

    (new Test(" ").validate match {
      case Some(e) => e.get("name").get.head
      case _ => throw new Exception("Failed.")
    }) should equal ("Name must have a value.")
  }

  "hasValue StringRule" should "let good values pass" in {
    class Test(val name: String) extends Validation {
      override def validate = {
        check("name", StringRules.hasValue, "Name must have a value.")
        failures
      }
    }

    (new Test("David").validate match {
      case Some(e) => throw new Exception("Failed.")
      case _ => true
    }) should equal (true)
  }

  "maxLength StringRule" should "enforce length on value" in {
    class Test(val name: String) extends Validation {
      override def validate = {
        check("name", StringRules.maxLength(5), "Max length of name is 5 characters.")
        failures
      }
    }

    (new Test("David McLaughlin").validate match {
      case Some(e) => e.get("name").get.head
      case _ => throw new Exception("Failed.")
    }) should equal ("Max length of name is 5 characters.")
  }

  "isEmail StringRule" should "enforce email format on a field" in {
    class Test(val email: String) extends Validation {
      override def validate = {
        check("email", StringRules.isEmail, "Email format is invalid.")
        failures
      }
    }

    (new Test("David").validate match {
      case Some(e) => e.get("email").get.head
      case _ => throw new Exception("Failed")
    }) should equal ("Email format is invalid.")
  }

  "isEmail StringRule" should "let valid email addresses pass" in {
    class Test(val email: String) extends Validation {
      override def validate = {
        check("email", StringRules.isEmail, "Email format is invalid.")
        failures
      }
    }

    (new Test("david@dmclaughlin.com").validate match {
      case Some(e) => throw new Exception("failed.")
      case _ => true
    }) should equal (true)

    (new Test("david+spam@dmclaughlin.com").validate match {
      case Some(e) => throw new Exception("failed.")
      case _ => true
    }) should equal (true)
  }
}
