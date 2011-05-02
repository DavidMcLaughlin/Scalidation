package com.dmclaughlin.scalidation
package validators

import scala.util.matching.Regex

object StringRules {
  def hasValue(s: String): Boolean = 
    s != null && s.trim.length > 0

  def maxLength(n: Int)(s: String): Boolean =
    s.length <= n

  def minLength(n: Int)(s: String): Boolean =
    s.length >= n

  def lengthRange(from: Int, to: Int)(s: String): Boolean =
    minLength(from)(s) && maxLength(to)(s)

  def matches(r: Regex)(s: String): Boolean = {
    s match {
      case r(s) => true
      case _ => false
    }
  }

  def isEmail: String => Boolean = matches("""(?i)([A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4})""".r)_
}
